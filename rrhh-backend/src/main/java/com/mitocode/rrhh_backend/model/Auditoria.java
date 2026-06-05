package com.mitocode.rrhh_backend.model;

import com.mitocode.rrhh_backend.util.SecurityUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Embeddable // Indica que esta clase se puede incrustar en otras entidades.
public class Auditoria {

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "updated_at")
    private LocalDateTime fechaModificacion;

    @Column(name = "created_by", updatable = false)
    private String creadoPor;

    @Column(name = "updated_by")
    private String modificadoPor;

    @PrePersist // Antes de insertar
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.creadoPor = SecurityUtil.getCurrentUserEmail();
    }

    @PreUpdate // Antes de actualizar
    public void preUpdate() {
        this.fechaModificacion = LocalDateTime.now();
        this.modificadoPor = SecurityUtil.getCurrentUserEmail();
    }
}