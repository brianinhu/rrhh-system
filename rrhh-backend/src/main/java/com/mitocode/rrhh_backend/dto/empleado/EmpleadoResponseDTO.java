package com.mitocode.rrhh_backend.dto.empleado;

import com.mitocode.rrhh_backend.dto.CargoDTO;
import com.mitocode.rrhh_backend.dto.DepartamentoDTO;
import lombok.Data;

@Data
public class EmpleadoResponseDTO {
    private Integer id;
    private String nombre;
    private String apellido;
    private Double sueldo;
    private CargoDTO cargo;
    private DepartamentoDTO departamento;
}
