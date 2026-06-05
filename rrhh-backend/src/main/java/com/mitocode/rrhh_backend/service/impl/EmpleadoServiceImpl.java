package com.mitocode.rrhh_backend.service.impl;

import com.mitocode.rrhh_backend.dto.empleado.EmpleadoDetalladoResponseDTO;
import com.mitocode.rrhh_backend.dto.empleado.EmpleadoRequestDTO;
import com.mitocode.rrhh_backend.dto.empleado.EmpleadoResponseDTO;
import com.mitocode.rrhh_backend.exception.ModeloNotFoundException;
import com.mitocode.rrhh_backend.mapper.IEmpleadoMapper;
import com.mitocode.rrhh_backend.model.Curriculum;
import com.mitocode.rrhh_backend.model.Empleado;
import com.mitocode.rrhh_backend.model.Proyecto;
import com.mitocode.rrhh_backend.repository.ICargoRepository;
import com.mitocode.rrhh_backend.repository.IDepartamentoRepository;
import com.mitocode.rrhh_backend.repository.IEmpleadoRepository;
import com.mitocode.rrhh_backend.repository.IProyectoRepository;
import com.mitocode.rrhh_backend.service.IEmpleadoService;
import com.mitocode.rrhh_backend.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements IEmpleadoService {

    private final IEmpleadoRepository empleadoRepository;
    private final ICargoRepository cargoRepository;
    private final IDepartamentoRepository departamentoRepository;
    private final IProyectoRepository proyectoRepository;
    private final IFileService fileService;
    private final IEmpleadoMapper empleadoMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<EmpleadoResponseDTO> listar(String term, Pageable pageable) {
        Page<Empleado> pagina;
        if (term == null || term.isEmpty()) {
            pagina = empleadoRepository.findAll(pageable);
        } else {
            pagina = empleadoRepository.buscarPorTermino(term, pageable);
        }
        return pagina.map(empleadoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Double obtenerNominaTotal() {
        Double total = empleadoRepository.obtenerNominaTotal();
        return (total != null) ? total : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarTotalEmpleados() {
        return empleadoRepository.contarTotalEmpleados();
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoDetalladoResponseDTO getById(Integer id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("ID NO ENCONTRADO: " + id));

        EmpleadoDetalladoResponseDTO empleadoDetalladoResponseDTO = empleadoMapper.toDetalladoResponseDTO(empleado);

        String signedUrl = (empleado.getCurriculum() != null && empleado.getCurriculum().getUrlArchivo() != null)
                ? fileService.getFileUrl(empleado.getCurriculum().getUrlArchivo())
                : null;

        return new EmpleadoDetalladoResponseDTO(
                empleadoDetalladoResponseDTO.id(),
                empleadoDetalladoResponseDTO.nombre(),
                empleadoDetalladoResponseDTO.apellido(),
                empleadoDetalladoResponseDTO.sueldo(),
                empleadoDetalladoResponseDTO.cargoNombre(),
                empleadoDetalladoResponseDTO.cargoDescripcion(),
                empleadoDetalladoResponseDTO.departamentoNombre(),
                empleadoDetalladoResponseDTO.departamentoDescripcion(),
                empleadoDetalladoResponseDTO.idCargo(),
                empleadoDetalladoResponseDTO.idDepartamento(),
                empleadoDetalladoResponseDTO.idsProyectos(),
                signedUrl,
                empleadoDetalladoResponseDTO.nivelEstudios(),
                empleadoDetalladoResponseDTO.especialidad(),
                empleadoDetalladoResponseDTO.fechaRegistro(),
                empleadoDetalladoResponseDTO.nombresProyectos()
        );
    }

    @Override
    @Transactional
    public EmpleadoResponseDTO guardar(EmpleadoRequestDTO dto, MultipartFile archivo) {
        // 1) Transformación limpia usando MapStruct
        Empleado empleado = empleadoMapper.toEntity(dto);

        // 2) Orquestación de relaciones (Cargo, Depto, Proyectos)
        this.asignarRelaciones(empleado, dto);

        // 3) Gestión del Curriculum (Entidad débil vinculada)
        Curriculum curriculum = new Curriculum();
        curriculum.setNivelEstudios(dto.getNivelEstudios());
        curriculum.setEspecialidad(dto.getEspecialidad());

        if (archivo != null && !archivo.isEmpty()) {
            String key = fileService.uploadFile(archivo);
            curriculum.setUrlArchivo(key);
        }

        curriculum.setEmpleado(empleado);
        empleado.setCurriculum(curriculum);

        // 4) Persistencia y respuesta
        Empleado entidadGuardada = empleadoRepository.save(empleado);
        return empleadoMapper.toResponseDTO(entidadGuardada);
    }

    @Override
    @Transactional
    public EmpleadoResponseDTO actualizar(Integer id, EmpleadoRequestDTO dto, MultipartFile archivo) {
        Empleado empleadoExistente = empleadoRepository.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("Id no encontrado: " + id));

        // 1) Actualización parcial (MappingTarget): Vuelca el DTO sobre la entidad persistida
        empleadoMapper.updateEntityFromDTO(dto, empleadoExistente);

        // 2) Sincronización de relaciones
        this.asignarRelaciones(empleadoExistente, dto);

        // 3) Gestión de Curriculum y Archivos en S3
        if (empleadoExistente.getCurriculum() == null) {
            Curriculum curriculum = new Curriculum();
            curriculum.setEmpleado(empleadoExistente);
            empleadoExistente.setCurriculum(curriculum);
        }

        empleadoExistente.getCurriculum().setNivelEstudios(dto.getNivelEstudios());
        empleadoExistente.getCurriculum().setEspecialidad(dto.getEspecialidad());

        if (archivo != null && !archivo.isEmpty()) {
            // Limpieza de S3: Borramos la versión anterior antes de subir la nueva
            String keyAnterior = empleadoExistente.getCurriculum().getUrlArchivo();
            if (keyAnterior != null && !keyAnterior.isEmpty()) {
                fileService.deleteFile(keyAnterior);
            }

            String nuevaKey = fileService.uploadFile(archivo);
            empleadoExistente.getCurriculum().setUrlArchivo(nuevaKey);
        }

        Empleado entidadActualizada = empleadoRepository.save(empleadoExistente);
        return empleadoMapper.toResponseDTO(entidadActualizada);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("ID NO ENCONTRADO: " + id));

        String key = (empleado.getCurriculum() != null) ? empleado.getCurriculum().getUrlArchivo() : null;

        empleadoRepository.delete(empleado);

        if (key != null && !key.isEmpty()) {
            try {
                fileService.deleteFile(key);
                System.out.println("✅ Archivo borrado de S3: " + key);
            } catch (Exception e) {
                System.err.println("❌ No se pudo borrar de S3: " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void asignarProyecto(Integer idEmpleado, Integer idProyecto) {
        // Buscamos al empleado
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new ModeloNotFoundException("Empleado no encontrado: " + idEmpleado));

        // Buscamos el proyecto
        Proyecto proyecto = proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new ModeloNotFoundException("Proyecto no encontrado: " + idProyecto));

        // Como 'proyectos' es un Set, Hibernate se encarga de no duplicar
        // y de insertar en la tabla 'empleado_proyecto' automáticamente al guardar.
        empleado.getProyectos().add(proyecto);

        empleadoRepository.save(empleado);
    }

    // Helper
    private void asignarRelaciones(Empleado empleado, EmpleadoRequestDTO empleadoRequestDTO) {
        empleado.setCargo(cargoRepository.findById(empleadoRequestDTO.getIdCargo())
                .orElseThrow(() -> new ModeloNotFoundException("Cargo no encontrado")));

        empleado.setDepartamento(departamentoRepository.findById(empleadoRequestDTO.getIdDepartamento())
                .orElseThrow(() -> new ModeloNotFoundException("Departamento no encontrado")));

        if (empleadoRequestDTO.getIdsProyectos() != null) {
            List<Proyecto> nuevosProyectos = proyectoRepository.findAllById(empleadoRequestDTO.getIdsProyectos());
            if (empleado.getProyectos() == null) {
                empleado.setProyectos(new HashSet<>(nuevosProyectos));
            } else {
                empleado.getProyectos().clear();
                empleado.getProyectos().addAll(nuevosProyectos);
            }
        }
    }
}
