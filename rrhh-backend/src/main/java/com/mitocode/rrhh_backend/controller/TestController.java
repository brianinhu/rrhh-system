package com.mitocode.rrhh_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "¡Hola, Administrador! Este es un mensaje secreto que un usuario normal nunca vería.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userOnly() {
        return "Este mensaje es para usuarios básicos.";
    }

    @GetMapping("/public")
    public String publicOnly() {
        return "Mensaje para cualquiera";
    }
}
