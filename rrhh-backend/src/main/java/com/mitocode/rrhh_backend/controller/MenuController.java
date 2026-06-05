package com.mitocode.rrhh_backend.controller;

import com.mitocode.rrhh_backend.dto.MenuDTO;
import com.mitocode.rrhh_backend.service.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

    private final IMenuService menuService;

    @GetMapping("/usuario")
    public ResponseEntity<List<MenuDTO>> listarPorUsuario() {
        return ResponseEntity.ok(menuService.listarMenusUsuarioLogueado());}
}
