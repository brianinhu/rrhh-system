package com.mitocode.rrhh_backend.service;

import com.mitocode.rrhh_backend.dto.proyecto.ProyectoDetalleResponseDTO;
import com.mitocode.rrhh_backend.dto.proyecto.ProyectoRequestDTO;
import com.mitocode.rrhh_backend.dto.proyecto.ProyectoResponseDTO;

import java.util.List;

public interface IProyectoService {

    List<ProyectoResponseDTO> listarTodos();

    ProyectoDetalleResponseDTO listarPorId(Integer id);

    ProyectoResponseDTO guardar(ProyectoRequestDTO ProyectoRequestDTO);

    ProyectoResponseDTO actualizar(Integer id, ProyectoRequestDTO ProyectoRequestDTO);

    void eliminar(Integer id);

}
