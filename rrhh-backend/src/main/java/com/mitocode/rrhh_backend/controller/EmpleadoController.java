package com.mitocode.rrhh_backend.controller;

import com.mitocode.rrhh_backend.dto.empleado.EmpleadoDetalladoResponseDTO;
import com.mitocode.rrhh_backend.dto.empleado.EmpleadoRequestDTO;
import com.mitocode.rrhh_backend.dto.empleado.EmpleadoResponseDTO;
import com.mitocode.rrhh_backend.service.IEmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final IEmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<Page<EmpleadoResponseDTO>> getAll(@RequestParam(name = "term", required = false)String term, Pageable pageable) {
        Page<EmpleadoResponseDTO> empleadosPage = empleadoService.listar(term, pageable);
        return ResponseEntity.ok(empleadosPage);
    }

    @GetMapping("/nomina-total")
    public ResponseEntity<Double> obtenerNominaTotal() {
        Double total = empleadoService.obtenerNominaTotal();
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    @GetMapping("/total-count")
    public ResponseEntity<Long> contarTotalEmpleados() {
        return ResponseEntity.ok(empleadoService.contarTotalEmpleados());
    }

    @GetMapping("/{id}")
    public EntityModel<EmpleadoDetalladoResponseDTO> getById(@PathVariable Integer id) {
        EmpleadoDetalladoResponseDTO empleadoDetalladoResponseDTO = empleadoService.getById(id);

        // HATEOAS (Nivel 3 Richardson)
        EntityModel<EmpleadoDetalladoResponseDTO> modelo = EntityModel.of(empleadoDetalladoResponseDTO);
        WebMvcLinkBuilder linkSelf = linkTo(methodOn(this.getClass()).getById(id));
        WebMvcLinkBuilder linkAll = linkTo(methodOn(this.getClass()).getAll(null, null));

        modelo.add(linkSelf.withSelfRel());
        modelo.add(linkAll.withRel("lista-completa"));

        return modelo;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmpleadoResponseDTO> create(
            @RequestPart("empleado") @Valid EmpleadoRequestDTO empleadoRequestDTO,
            @RequestPart(value = "curriculum", required = false) MultipartFile archivo) {

        // El controlador solo orquestra: recibe, valida y delega.
        EmpleadoResponseDTO nuevoEmpleado = empleadoService.guardar(empleadoRequestDTO, archivo);

        return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmpleadoResponseDTO> update(
            @PathVariable Integer id,
            @RequestPart("empleado") @Valid EmpleadoRequestDTO empleadoRequestDTO,
            @RequestPart(value = "curriculum", required = false) MultipartFile archivo) {

        EmpleadoResponseDTO empleadoActualizado = empleadoService.actualizar(id, empleadoRequestDTO, archivo);
        return ResponseEntity.ok(empleadoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idEmpleado}/proyectos/{idProyecto}")
    public ResponseEntity<Void> asignarProyecto(
            @PathVariable Integer idEmpleado,
            @PathVariable Integer idProyecto) {

        empleadoService.asignarProyecto(idEmpleado, idProyecto);
        return ResponseEntity.noContent().build();
    }

}