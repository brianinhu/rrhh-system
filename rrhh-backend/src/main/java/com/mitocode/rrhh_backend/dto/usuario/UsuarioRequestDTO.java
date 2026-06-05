package com.mitocode.rrhh_backend.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UsuarioRequestDTO(
        @NotBlank @Email
        String email,

        @NotBlank
        String password,

        @NotNull
        Integer idEmpleado,

        @NotEmpty
        List<Integer> idsRoles
) {
}
