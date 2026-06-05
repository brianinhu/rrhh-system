package com.mitocode.rrhh_backend.mapper;

import com.mitocode.rrhh_backend.dto.usuario.UsuarioRequestDTO;
import com.mitocode.rrhh_backend.dto.usuario.UsuarioResponseDTO;
import com.mitocode.rrhh_backend.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IUsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "empleado", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    Usuario toEntity(UsuarioRequestDTO request);

    @Mapping(target = "nombreCompleto",
            expression = "java(entity.getEmpleado().getNombre() + ' ' + entity.getEmpleado().getApellido())")
    @Mapping(target = "nombresRoles", ignore = true) // Los roles vendrán del SecurityContext/JWT
    UsuarioResponseDTO toResponseDTO(Usuario entity);
}
