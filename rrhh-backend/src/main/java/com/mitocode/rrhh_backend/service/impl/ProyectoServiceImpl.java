package com.mitocode.rrhh_backend.service.impl;

import com.mitocode.rrhh_backend.dto.proyecto.ProyectoDetalleResponseDTO;
import com.mitocode.rrhh_backend.dto.proyecto.ProyectoRequestDTO;
import com.mitocode.rrhh_backend.dto.proyecto.ProyectoResponseDTO;
import com.mitocode.rrhh_backend.exception.ModeloNotFoundException;
import com.mitocode.rrhh_backend.model.Empleado;
import com.mitocode.rrhh_backend.model.Proyecto;
import com.mitocode.rrhh_backend.model.enums.EstadoProyecto;
import com.mitocode.rrhh_backend.repository.IEmpleadoRepository;
import com.mitocode.rrhh_backend.repository.IProyectoRepository;
import com.mitocode.rrhh_backend.service.IProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProyectoServiceImpl implements IProyectoService {

    private final IProyectoRepository proyectoRepository;
    private final IEmpleadoRepository empleadoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponseDTO> listarTodos() {
        return proyectoRepository.findAll()
                .stream()
                .map(p -> new ProyectoResponseDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getEstado().name() // Convertimos el Enum a String
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoDetalleResponseDTO listarPorId(Integer id) {
        Proyecto p = proyectoRepository.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("ID NO ENCONTRADO: " + id));

        List<Integer> idsEmpleados = p.getEmpleados().stream()
                .map(Empleado::getId)
                .toList();

        List<String> nombresEmpleados = p.getEmpleados().stream()
                .map(e -> e.getNombre() + " " + e.getApellido())
                .toList();

        return new ProyectoDetalleResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getEstado().name(),
                idsEmpleados,
                nombresEmpleados
        );
    }

    @Override
    @Transactional
    public ProyectoResponseDTO guardar(ProyectoRequestDTO dto) {
        Proyecto p = new Proyecto();
        p.setNombre(dto.nombre());
        p.setDescripcion(dto.descripcion());
        p.setEstado(EstadoProyecto.valueOf(dto.estado()));

        // 1. Guardamos el proyecto base
        Proyecto nuevo = proyectoRepository.save(p);

        // 2. Lógica de asignación: Buscamos a los empleados y les agregamos este proyecto
        if (dto.idsEmpleados() != null && !dto.idsEmpleados().isEmpty()) {
            List<Empleado> empleados = empleadoRepository.findAllById(dto.idsEmpleados());
            for (Empleado e : empleados) {
                e.getProyectos().add(nuevo);
            }
            // Guardamos los empleados para que JPA actualice la tabla intermedia
            empleadoRepository.saveAll(empleados);
        }

        return new ProyectoResponseDTO(nuevo.getId(), nuevo.getNombre(), nuevo.getDescripcion(), nuevo.getEstado().name());
    }

    @Override
    @Transactional
    public ProyectoResponseDTO actualizar(Integer id, ProyectoRequestDTO dto) {
        Proyecto p = proyectoRepository.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("ID NO ENCONTRADO: " + id));

        p.setNombre(dto.nombre());
        p.setDescripcion(dto.descripcion());
        p.setEstado(EstadoProyecto.valueOf(dto.estado()));

        // 1. Actualizamos los datos básicos del proyecto
        Proyecto actualizado = proyectoRepository.save(p);

        // 2. Lógica de actualización de asignaciones
        if (dto.idsEmpleados() != null) {

            // A. Primero quitamos este proyecto a TODOS los empleados que lo tenían
            Set<Empleado> empleadosActuales = actualizado.getEmpleados();
            for (Empleado e : empleadosActuales) {
                e.getProyectos().remove(actualizado);
            }
            empleadoRepository.saveAll(empleadosActuales);

            // B. Luego, agregamos el proyecto únicamente a los IDs nuevos seleccionados
            if (!dto.idsEmpleados().isEmpty()) {
                List<Empleado> nuevosEmpleados = empleadoRepository.findAllById(dto.idsEmpleados());
                for (Empleado e : nuevosEmpleados) {
                    e.getProyectos().add(actualizado);
                }
                empleadoRepository.saveAll(nuevosEmpleados);
            }
        }

        return new ProyectoResponseDTO(actualizado.getId(), actualizado.getNombre(), actualizado.getDescripcion(), actualizado.getEstado().name());
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        // 1. Verificación de existencia
        if (!proyectoRepository.existsById(id)) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO: " + id);
        }

        // 2. Borrado físico de la base de datos
        proyectoRepository.deleteById(id);
    }
}
