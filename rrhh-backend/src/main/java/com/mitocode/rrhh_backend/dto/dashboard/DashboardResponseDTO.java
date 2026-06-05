package com.mitocode.rrhh_backend.dto.dashboard;

import java.util.List;

public record DashboardResponseDTO(
        Long totalEmpleadosActivos,
        List<ChartDataDTO> distribucionDepartamento,
        List<ChartDataDTO> distribucionCargo,
        List<EmpleadoDashboardDTO> topSueldos,
        List<ChartDataDTO> proyectosStatus,
        List<EmpleadoDashboardDTO> ultimosEmpleados
) {}
