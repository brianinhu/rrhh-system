package com.mitocode.rrhh_backend.controller;

import com.mitocode.rrhh_backend.dto.*;
import com.mitocode.rrhh_backend.dto.GenericResponse;
import com.mitocode.rrhh_backend.service.IAuthService;
import com.mitocode.rrhh_backend.service.IUsuarioService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;
    private final IUsuarioService usuarioService;

    @GetMapping("/perfil")
    public ResponseEntity<UserProfileDTO> obtenerPerfil(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        UserProfileDTO perfil = usuarioService.obtenerPerfil(email);
        return ResponseEntity.ok(perfil);
    }
}