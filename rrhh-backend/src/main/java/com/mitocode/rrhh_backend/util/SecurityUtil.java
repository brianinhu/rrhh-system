package com.mitocode.rrhh_backend.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUtil {

    private SecurityUtil() {
    }

    // Obtiene el Keycloak ID del usuario autenticado
    public static String getCurrentUserKeycloakId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getSubject(); // El claim 'sub' es el UUID de Keycloak
        }
        return null;
    }

    // Obtiene el email desde los claims del JWT
    public static String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getClaimAsString("email");
        }
        return null;
    }

    // Obtiene los roles del usuario actual procesados por Spring Security
    public static Set<String> getCurrentUserRoles() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(rol -> rol.replace("ROLE_", "")) // Estandarizamos eliminando el prefijo
                .collect(Collectors.toSet());
    }
}
