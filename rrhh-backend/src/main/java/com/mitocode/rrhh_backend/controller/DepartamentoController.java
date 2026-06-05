package com.mitocode.rrhh_backend.controller;

import com.mitocode.rrhh_backend.model.Departamento;
import com.mitocode.rrhh_backend.service.IDepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/departamentos")
@RequiredArgsConstructor
public class DepartamentoController {

    private final IDepartamentoService departamentoService;

    @GetMapping
    public List<Departamento> getAll() {
        return departamentoService.getAll();
    }
}
