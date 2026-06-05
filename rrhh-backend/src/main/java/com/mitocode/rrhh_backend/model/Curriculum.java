package com.mitocode.rrhh_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "curriculums")
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nivelEstudios; // e.g., "Licenciatura", "Maestría", etc.

    @Column(nullable = false)
    private String especialidad; // e.g., "Ingeniería de Software", "Administración de Empresas", etc.

    @Column(length = 255)
    private String urlArchivo;

    // Relación inversa: Un curriculum pertenece a un empleado
    @OneToOne(mappedBy = "curriculum")
    private Empleado empleado;
}
