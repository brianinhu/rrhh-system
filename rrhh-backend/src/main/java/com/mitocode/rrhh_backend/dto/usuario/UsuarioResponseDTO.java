package com.mitocode.rrhh_backend.dto.usuario;

import java.util.List;

public record UsuarioResponseDTO(
        Integer id,
        String email,
        boolean enabled,
        String nombreCompleto,
        List<String> nombresRoles
) {
}
