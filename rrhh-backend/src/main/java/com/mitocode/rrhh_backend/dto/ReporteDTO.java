package com.mitocode.rrhh_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Genera getters, setters, toString, equals, y hashCode automáticamente.
@AllArgsConstructor // Genera un constructor con todos los campos como parámetros.
@NoArgsConstructor // Genera un constructor sin parámetros.
public class ReporteDTO {
    private String name;
    private Number value;
}
