package com.mitocode.rrhh_backend.dto.proyecto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProyectoRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 100)
        String nombre,

        @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
        String descripcion,

        @NotBlank(message = "El estado es obligatorio")
        String estado,

        List<Integer> idsEmpleados
) {
}
