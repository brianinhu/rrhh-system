package com.mitocode.rrhh_backend.service;

import com.mitocode.rrhh_backend.dto.UserProfileDTO;
import com.mitocode.rrhh_backend.dto.usuario.UsuarioRequestDTO;
import com.mitocode.rrhh_backend.dto.usuario.UsuarioResponseDTO;

import java.util.List;

public interface IUsuarioService {

    UserProfileDTO obtenerPerfil(String email);

    List<UsuarioResponseDTO> listarTodos();

    UsuarioResponseDTO listarPorId(Integer id);

    UsuarioResponseDTO guardar(UsuarioRequestDTO usuarioRequestDTO);

    void cambiarEstado(Integer id, boolean nuevoEstado);

    void eliminar(Integer id);
}