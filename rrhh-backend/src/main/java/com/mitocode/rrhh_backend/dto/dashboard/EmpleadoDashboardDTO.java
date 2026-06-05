package com.mitocode.rrhh_backend.dto.dashboard;

import java.time.LocalDateTime;

public record EmpleadoDashboardDTO(
        String nombreCompleto,
        String departamento,
        String cargo,
        Double sueldo,
        LocalDateTime fechaIngreso
) {}
