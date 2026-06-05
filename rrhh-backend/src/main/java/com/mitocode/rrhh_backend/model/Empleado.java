package com.mitocode.rrhh_backend.model;

import com.mitocode.rrhh_backend.model.enums.EstadoEmpleado;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 50)
    private String apellido;

    @ManyToOne
    @JoinColumn(name = "id_cargo", nullable = false, foreignKey = @ForeignKey(name = "FK_EMPLEADO_CARGO"))
    private Cargo cargo;

    @Column(nullable = false)
    private Double sueldo;

    @ManyToOne
    @JoinColumn(name = "id_departamento", nullable = false, foreignKey = @ForeignKey(name = "FK_EMPLEADO_DEPARTAMENTO"))
    private Departamento departamento;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_curriculum", foreignKey = @ForeignKey(name = "FK_EMPLEADO_CURRICULUM"))
    private Curriculum curriculum;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "empleado_proyecto",
        joinColumns = @JoinColumn(name = "id_empleado", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_proyecto", referencedColumnName = "id")
    )
    private Set<Proyecto> proyectos = new HashSet<>();

    @Embedded
    private Auditoria auditoria;

    public Auditoria getAuditoria() {
        if (this.auditoria == null) {
            this.auditoria = new Auditoria();
        }
        return this.auditoria;
    }

    // Metodo para validar y establecer la auditoría antes de persistir
    @PrePersist
    public void validarAuditoria() {
        if (this.auditoria == null) {
            this.auditoria = new Auditoria();
        }

        if (this.auditoria.getFechaCreacion() == null) {
            this.auditoria.setFechaCreacion(LocalDateTime.now());
        }
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20) default 'ACTIVO'")
    private EstadoEmpleado estado = EstadoEmpleado.ACTIVO; // Valor por defecto

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empleado)) return false;
        return id != null && id.equals(((Empleado) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
