package com.mitocode.rrhh_backend.repository;

import com.mitocode.rrhh_backend.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDepartamentoRepository extends JpaRepository<Departamento, Integer> {
}
