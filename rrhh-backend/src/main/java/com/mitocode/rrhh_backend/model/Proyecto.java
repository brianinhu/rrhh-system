package com.mitocode.rrhh_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mitocode.rrhh_backend.model.enums.EstadoProyecto;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "proyectos")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoProyecto estado = EstadoProyecto.PLANIFICACION;

    // Relación inversa: Muchos proyectos tienen muchos empleados
    @ManyToMany(mappedBy = "proyectos")
    @JsonIgnore
    private Set<Empleado> empleados = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Proyecto)) return false;
        return id != null && id.equals(((Proyecto) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
