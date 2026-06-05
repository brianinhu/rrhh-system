package com.mitocode.rrhh_backend.service.impl;

import com.mitocode.rrhh_backend.dto.MenuDTO;
import com.mitocode.rrhh_backend.dto.UserProfileDTO;
import com.mitocode.rrhh_backend.dto.usuario.UsuarioRequestDTO;
import com.mitocode.rrhh_backend.dto.usuario.UsuarioResponseDTO;
import com.mitocode.rrhh_backend.exception.ModeloConflictException;
import com.mitocode.rrhh_backend.exception.ModeloNotFoundException;
import com.mitocode.rrhh_backend.mapper.IUsuarioMapper;
import com.mitocode.rrhh_backend.model.Empleado;
import com.mitocode.rrhh_backend.model.Rol;
import com.mitocode.rrhh_backend.model.Usuario;
import com.mitocode.rrhh_backend.repository.IEmpleadoRepository;
import com.mitocode.rrhh_backend.repository.IMenuRepository;
import com.mitocode.rrhh_backend.repository.IRolRepository;
import com.mitocode.rrhh_backend.repository.IUsuarioRepository;
import com.mitocode.rrhh_backend.service.IUsuarioService;
import com.mitocode.rrhh_backend.service.KeycloakAdminService;
import com.mitocode.rrhh_backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final IRolRepository rolRepository;
    private final IEmpleadoRepository empleadoRepository;
    private final IMenuRepository menuRepository;
    private final IUsuarioMapper usuarioMapper;
    private final KeycloakAdminService keycloakAdminService;

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO obtenerPerfil(String email) {
        Usuario usuario = usuarioRepository.findOneByEmail(email)
                .orElseThrow(() -> new ModeloNotFoundException("Usuario no encontrado con email: " + email));

        Set<String> rolesActivos = SecurityUtil.getCurrentUserRoles();

        // Los menús ahora se consultan por los roles que el usuario tiene activos.
        List<MenuDTO> menus = menuRepository.getMenusByRoles(rolesActivos).stream()
                .map(menu -> new MenuDTO(menu.getNombre(), menu.getIcono(), menu.getUrl()))
                .toList();

        String nombreCompleto = usuario.getEmpleado().getNombre() + " " + usuario.getEmpleado().getApellido();

        return new UserProfileDTO(usuario.getEmail(), nombreCompleto, rolesActivos, menus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO listarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResponseDTO)
                .orElseThrow(() -> new ModeloNotFoundException("ID NO ENCONTRADO: " + id));
    }

    @Override
    @Transactional
    public UsuarioResponseDTO guardar(UsuarioRequestDTO usuarioRequestDTO) {
        // 1) Validaciones del negocio
        if (usuarioRepository.findOneByEmail(usuarioRequestDTO.email()).isPresent()) {
            throw new ModeloConflictException("El email " + usuarioRequestDTO.email() + " ya está registrado");
        }
        if (usuarioRepository.existsByEmpleadoId(usuarioRequestDTO.idEmpleado())) {
            throw new ModeloConflictException("El empleado ya tiene un usuario vinculado.");
        }

        // 2) Obtener datos necesarios para la orquestación
        Empleado empleado = empleadoRepository.findById(usuarioRequestDTO.idEmpleado())
                .orElseThrow(() -> new ModeloNotFoundException("Empleado no encontrado"));

        List<Rol> rolesList = rolRepository.findAllById(usuarioRequestDTO.idsRoles());
        Set<String> nombresRoles = rolesList.stream().map(Rol::getNombre).collect(Collectors.toSet());

        // 3) Creación en Keycloak (Fuente de Verdad de Identidad)
        String keycloakId = keycloakAdminService.crearUsuario(
                usuarioRequestDTO.email(),
                usuarioRequestDTO.password(),
                nombresRoles,
                empleado.getNombre(),
                empleado.getApellido()
        );

        // 4) Persistencia local (sincronización)
        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDTO);
        usuario.setKeycloakId(keycloakId); // Vinculamos con el ID de Keycloak
        usuario.setEmpleado(empleado);
        usuario.setEnabled(true);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // 5) Respuesta
        UsuarioResponseDTO response = usuarioMapper.toResponseDTO(usuarioGuardado);
        return new UsuarioResponseDTO(
                response.id(),
                response.email(),
                response.enabled(),
                response.nombreCompleto(),
                nombresRoles.stream().toList()
        );
    }

    @Override
    @Transactional
    public void cambiarEstado(Integer id, boolean nuevoEstado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("Usuario no encontrado"));

        usuario.setEnabled(nuevoEstado);
        usuarioRepository.save(usuario);

        keycloakAdminService.actualizarEstado(usuario.getKeycloakId(), nuevoEstado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("ID NO ENCONTRADO: " + id));

        keycloakAdminService.eliminarUsuario(usuario.getKeycloakId());

        usuarioRepository.deleteById(id);
    }
}
