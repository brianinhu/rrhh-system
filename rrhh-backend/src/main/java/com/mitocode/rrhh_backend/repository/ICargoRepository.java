package com.mitocode.rrhh_backend.repository;

import com.mitocode.rrhh_backend.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICargoRepository extends JpaRepository<Cargo, Integer> {
}
