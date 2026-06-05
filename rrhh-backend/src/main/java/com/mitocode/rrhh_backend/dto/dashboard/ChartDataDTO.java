package com.mitocode.rrhh_backend.dto.dashboard;

// Para los gráficos de departamentos, cargos y los proyectos.
public record ChartDataDTO(
        String label,
        Number value
) {}
