package com.mitocode.rrhh_backend.dto.proyecto;

public record ProyectoResponseDTO(
        Integer id,
        String nombre,
        String descripcion,
        String estado
) {
}
