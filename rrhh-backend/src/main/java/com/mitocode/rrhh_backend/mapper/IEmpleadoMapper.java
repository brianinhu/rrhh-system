package com.mitocode.rrhh_backend.mapper;

import com.mitocode.rrhh_backend.dto.empleado.EmpleadoDetalladoResponseDTO;
import com.mitocode.rrhh_backend.dto.empleado.EmpleadoRequestDTO;
import com.mitocode.rrhh_backend.dto.empleado.EmpleadoResponseDTO;
import com.mitocode.rrhh_backend.model.Empleado;
import com.mitocode.rrhh_backend.model.Proyecto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IEmpleadoMapper {

    // 1) De DTO de Entrada a Entidad (Ignoramos lo que el Service debe gestionar)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cargo", ignore = true)
    @Mapping(target = "departamento", ignore = true)
    @Mapping(target = "proyectos", ignore = true)
    @Mapping(target = "curriculum", ignore = true) // Se crea/vincula en el Service
    @Mapping(target = "auditoria", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Empleado toEntity(EmpleadoRequestDTO request);

    // Actualización: Solo campos editables, el resto lo gestiona el Service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cargo", ignore = true)
    @Mapping(target = "departamento", ignore = true)
    @Mapping(target = "proyectos", ignore = true)
    @Mapping(target = "curriculum", ignore = true)
    @Mapping(target = "auditoria", ignore = true)
    @Mapping(target = "estado", ignore = true)
    void updateEntityFromDTO(EmpleadoRequestDTO dto, @MappingTarget Empleado entity);

    // 2) Respuesta simple
    EmpleadoResponseDTO toResponseDTO(Empleado entity);

    // 3) Respuesta detallada
    @Mapping(target = "cargoNombre", source = "cargo.nombre")
    @Mapping(target = "cargoDescripcion", source = "cargo.descripcion")
    @Mapping(target = "departamentoNombre", source = "departamento.nombre")
    @Mapping(target = "departamentoDescripcion", source = "departamento.descripcion")
    @Mapping(target = "idCargo", source = "cargo.id")
    @Mapping(target = "idDepartamento", source = "departamento.id")
    @Mapping(target = "urlCurriculum", source = "curriculum.urlArchivo")
    @Mapping(target = "nivelEstudios", source = "curriculum.nivelEstudios")
    @Mapping(target = "especialidad", source = "curriculum.especialidad")
    @Mapping(target = "fechaRegistro", source = "auditoria.fechaCreacion")
    // Mapeo de listas usando expresiones Java
    @Mapping(target = "idsProyectos", expression = "java(mapProyectosToIds(entity.getProyectos()))")
    @Mapping(target = "nombresProyectos", expression = "java(mapProyectosToNombres(entity.getProyectos()))")
    EmpleadoDetalladoResponseDTO toDetalladoResponseDTO(Empleado entity);

    // Métodos default para lógica de colecciones (Java 25 style)
    default List<Integer> mapProyectosToIds(Set<Proyecto> proyectos) {
        if (proyectos == null) return List.of();
        return proyectos.stream().map(Proyecto::getId).toList();
    }

    default List<String> mapProyectosToNombres(Set<Proyecto> proyectos) {
        if (proyectos == null) return List.of();
        return proyectos.stream().map(Proyecto::getNombre).toList();
    }
}
