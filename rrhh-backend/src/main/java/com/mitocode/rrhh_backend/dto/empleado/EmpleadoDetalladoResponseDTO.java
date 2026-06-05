package com.mitocode.rrhh_backend.dto.empleado;

import java.time.LocalDateTime;
import java.util.List;

public record EmpleadoDetalladoResponseDTO(
        Integer id,
        String nombre,
        String apellido,
        Double sueldo,
        String cargoNombre,
        String cargoDescripcion,
        String departamentoNombre,
        String departamentoDescripcion,
        Integer idCargo,
        Integer idDepartamento,
        List<Integer> idsProyectos,
        String urlCurriculum,
        String nivelEstudios,
        String especialidad,
        LocalDateTime fechaRegistro,
        List<String> nombresProyectos
) {}
