package com.mitocode.rrhh_backend.controller;

import com.mitocode.rrhh_backend.dto.proyecto.ProyectoDetalleResponseDTO;
import com.mitocode.rrhh_backend.dto.proyecto.ProyectoRequestDTO;
import com.mitocode.rrhh_backend.dto.proyecto.ProyectoResponseDTO;
import com.mitocode.rrhh_backend.service.IProyectoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final IProyectoService proyectoService;

    @GetMapping
    public ResponseEntity<List<ProyectoResponseDTO>> getAll() {
         List<ProyectoResponseDTO> proyectos = proyectoService.listarTodos();
         return ResponseEntity.ok(proyectos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoDetalleResponseDTO> listarPorId(@PathVariable Integer id) {
        ProyectoDetalleResponseDTO proyecto = proyectoService.listarPorId(id);
        return ResponseEntity.ok(proyecto);
    }

    @PostMapping
    public ResponseEntity<ProyectoResponseDTO> guardar(@Valid @RequestBody ProyectoRequestDTO proyectoRequestDTO) {
        return new ResponseEntity<>(proyectoService.guardar(proyectoRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProyectoResponseDTO> actualizar(@PathVariable("id") Integer id, @Valid @RequestBody ProyectoRequestDTO proyectoRequestDTO) {
        return ResponseEntity.ok(proyectoService.actualizar(id, proyectoRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) {
        proyectoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
