package com.mitocode.rrhh_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false, unique = true,  length = 255)
    private String email;

    @Column(name = "keycloak_id", nullable = false, unique = true, length = 255)
    private String keycloakId;

    @OneToOne
    @JoinColumn(name = "id_empleado", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "FK_usuario_empleado"))
    private Empleado empleado;

    @Column(nullable = false)
    private boolean enabled = true;
}
