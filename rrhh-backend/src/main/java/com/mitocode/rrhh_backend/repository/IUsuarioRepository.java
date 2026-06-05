package com.mitocode.rrhh_backend.repository;

import com.mitocode.rrhh_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findOneByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmpleadoId(Integer idEmpleado);

    @Modifying
    @Query("UPDATE Usuario u SET u.enabled = :status WHERE u.id = :id")
    void updateStatus(@Param("id") Integer id, @Param("status") boolean status);
}
