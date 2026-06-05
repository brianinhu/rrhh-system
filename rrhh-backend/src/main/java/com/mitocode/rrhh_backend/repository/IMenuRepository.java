package com.mitocode.rrhh_backend.repository;

import com.mitocode.rrhh_backend.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface IMenuRepository extends JpaRepository<Menu, Integer> {

    @Query("""
           SELECT DISTINCT m 
           FROM Menu m 
           JOIN m.roles r 
           WHERE r.nombre IN :nombresRoles
           """)
    List<Menu> getMenusByRoles(@Param("nombresRoles") Set<String> nombresRoles);
}
