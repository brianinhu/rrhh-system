package com.mitocode.rrhh_backend.controller;

import com.mitocode.rrhh_backend.dto.dashboard.DashboardResponseDTO;
import com.mitocode.rrhh_backend.service.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IDashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getData() {
        return ResponseEntity.ok(dashboardService.obtenerDatosDashboard());
    }

}
