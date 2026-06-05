package com.mitocode.rrhh_backend.dto;

import java.util.List;
import java.util.Set;

public record UserProfileDTO(
        String email,
        String nombreCompleto,
        Set<String> roles,
        List<MenuDTO> menus
) {}
