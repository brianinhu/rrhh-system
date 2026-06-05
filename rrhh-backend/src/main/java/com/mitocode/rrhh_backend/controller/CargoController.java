package com.mitocode.rrhh_backend.controller;

import com.mitocode.rrhh_backend.model.Cargo;
import com.mitocode.rrhh_backend.service.ICargoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cargos")
@RequiredArgsConstructor
public class CargoController {

    private final ICargoService cargoService;

    @GetMapping
    public List<Cargo> getAll() {
        return cargoService.getAll();
    }
}
