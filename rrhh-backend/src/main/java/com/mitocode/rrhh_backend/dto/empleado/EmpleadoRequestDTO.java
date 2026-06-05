package com.mitocode.rrhh_backend.dto.empleado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class EmpleadoRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 a 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    private String apellido;

    @NotNull(message = "El sueldo no puede ser nulo")
    @Positive(message = "El sueldo debe ser un valor positivo")
    private Double sueldo;

    @NotNull(message = "El ID del cargo no puede ser nulo")
    @Positive
    private Integer idCargo;

    @NotNull(message = "El ID del departamento no puede ser nulo")
    @Positive
    private Integer idDepartamento;

    @NotBlank(message = "El nivel de estudios es obligatorio")
    private String nivelEstudios;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    private List<Integer> idsProyectos;
}
