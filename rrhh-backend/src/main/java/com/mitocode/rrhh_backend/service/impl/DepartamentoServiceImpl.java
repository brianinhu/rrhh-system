package com.mitocode.rrhh_backend.service.impl;

import com.mitocode.rrhh_backend.model.Departamento;
import com.mitocode.rrhh_backend.repository.IDepartamentoRepository;
import com.mitocode.rrhh_backend.service.IDepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartamentoServiceImpl implements IDepartamentoService {

    private final IDepartamentoRepository departamentoRepository;

     public List<Departamento> getAll() {
         return departamentoRepository.findAll();
     }
}
