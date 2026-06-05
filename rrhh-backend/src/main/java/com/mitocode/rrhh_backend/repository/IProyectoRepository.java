package com.mitocode.rrhh_backend.repository;

import com.mitocode.rrhh_backend.dto.dashboard.ChartDataDTO;
import com.mitocode.rrhh_backend.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProyectoRepository extends JpaRepository<Proyecto, Integer> {

    @Query("SELECT new com.mitocode.rrhh_backend.dto.dashboard.ChartDataDTO(CAST(p.estado AS string), COUNT(p)) " +
            "FROM Proyecto p GROUP BY p.estado")
    List<ChartDataDTO> getProyectosStatusCount();

}
