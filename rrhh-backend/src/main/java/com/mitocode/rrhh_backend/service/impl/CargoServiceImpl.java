package com.mitocode.rrhh_backend.service.impl;

import com.mitocode.rrhh_backend.model.Cargo;
import com.mitocode.rrhh_backend.repository.ICargoRepository;
import com.mitocode.rrhh_backend.service.ICargoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CargoServiceImpl implements ICargoService {

    private final ICargoRepository cargoRepository;

    public List<Cargo> getAll() {
        return cargoRepository.findAll();
    }
}
