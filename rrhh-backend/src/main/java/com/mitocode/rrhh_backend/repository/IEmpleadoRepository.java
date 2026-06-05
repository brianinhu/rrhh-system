package com.mitocode.rrhh_backend.repository;

import com.mitocode.rrhh_backend.dto.ReporteDTO;
import com.mitocode.rrhh_backend.dto.dashboard.ChartDataDTO;
import com.mitocode.rrhh_backend.dto.dashboard.EmpleadoDashboardDTO;
import com.mitocode.rrhh_backend.model.Empleado;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmpleadoRepository extends JpaRepository<Empleado, Integer> {

    @NullMarked
    Page<Empleado> findAll(Pageable pageable);

    @Query("SELECT e FROM Empleado e WHERE " +
            "LOWER(e.nombre) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(e.apellido) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(e.cargo.nombre) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(e.departamento.nombre) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<Empleado> buscarPorTermino(@Param("term") String term, Pageable pageable);

    @Query("SELECT SUM(e.sueldo) FROM Empleado e")
    Double obtenerNominaTotal();

    @Query("SELECT COUNT(e) FROM Empleado e")
    Long contarTotalEmpleados();

    // -- Dashboard queries --

    // 1. Total en Nómina
    @Query("SELECT COUNT(e) FROM Empleado e WHERE e.estado NOT IN (" +
            "'DESPEDIDO', 'JUBILADO')")
    Long countTotalNomina();

    // 2. Distribución por Departamento
    @Query("SELECT new com.mitocode.rrhh_backend.dto.dashboard.ChartDataDTO(e.departamento.nombre, COUNT(e)) " +
            "FROM Empleado e GROUP BY e.departamento.nombre")
    List<ChartDataDTO> getCountByDepartamento();

    // 3. Distribución por Cargo
    @Query("SELECT new com.mitocode.rrhh_backend.dto.dashboard.ChartDataDTO(e.cargo.nombre, COUNT(e)) " +
            "FROM Empleado e GROUP BY e.cargo.nombre")
    List<ChartDataDTO> getCountByCargo();

    // 4. Top 5 Sueldos
    @Query("SELECT new com.mitocode.rrhh_backend.dto.dashboard.EmpleadoDashboardDTO(" +
            "CONCAT(e.nombre, ' ', e.apellido), e.departamento.nombre, e.cargo.nombre, e.sueldo, e.auditoria.fechaCreacion) " +
            "FROM Empleado e ORDER BY e.sueldo DESC")
    List<EmpleadoDashboardDTO> findTopSueldos(Pageable pageable);

    // 5. Últimos 5 Empleados Registrados
    @Query("SELECT new com.mitocode.rrhh_backend.dto.dashboard.EmpleadoDashboardDTO(" +
            "CONCAT(e.nombre, ' ', e.apellido), e.departamento.nombre, e.cargo.nombre, e.sueldo, e.auditoria.fechaCreacion) " +
            "FROM Empleado e ORDER BY e.id DESC")
    List<EmpleadoDashboardDTO> findUltimosRegistrados(Pageable pageable);


    @Query("SELECT ReporteDTO(e.cargo.nombre, AVG(e.sueldo)) " +
           "FROM Empleado e GROUP BY e.cargo")
    List<ReporteDTO> averageSueldoByCargo();

}
