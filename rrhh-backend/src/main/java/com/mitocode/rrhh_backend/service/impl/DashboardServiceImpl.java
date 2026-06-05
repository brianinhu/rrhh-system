package com.mitocode.rrhh_backend.service.impl;

import com.mitocode.rrhh_backend.dto.dashboard.ChartDataDTO;
import com.mitocode.rrhh_backend.dto.dashboard.DashboardResponseDTO;
import com.mitocode.rrhh_backend.dto.dashboard.EmpleadoDashboardDTO;
import com.mitocode.rrhh_backend.repository.IEmpleadoRepository;
import com.mitocode.rrhh_backend.repository.IProyectoRepository;
import com.mitocode.rrhh_backend.service.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements IDashboardService {

    private final IEmpleadoRepository empleadoRepository;
    private final IProyectoRepository proyectoRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponseDTO obtenerDatosDashboard() {

        Long totalNomina = empleadoRepository.countTotalNomina();
        List<ChartDataDTO> distribucionDepartamento = empleadoRepository.getCountByDepartamento();
        List<ChartDataDTO> distribucionCargo = empleadoRepository.getCountByCargo();
        List<EmpleadoDashboardDTO> topSueldos = empleadoRepository.findTopSueldos(PageRequest.of(0, 5));
        List<ChartDataDTO> distribucionProyectos = proyectoRepository.getProyectosStatusCount();
        List<EmpleadoDashboardDTO> ultimosEmpleados = empleadoRepository.findUltimosRegistrados(PageRequest.of(0, 5));

        return new DashboardResponseDTO(
                totalNomina,
                distribucionDepartamento,
                distribucionCargo,
                topSueldos,
                distribucionProyectos,
                ultimosEmpleados
        );
    }
}
