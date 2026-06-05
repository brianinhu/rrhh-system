package com.mitocode.rrhh_backend.dto.proyecto;

import java.util.List;

public record ProyectoDetalleResponseDTO(
        Integer id,
        String nombre,
        String descripcion,
        String estado,
        List<Integer> idsEmpleados,
        List<String> nombresEmpleados
) {
}
