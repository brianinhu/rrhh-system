package com.mitocode.rrhh_backend.mapper;

import java.util.List;

public interface IGenericMapper<D, E> {
    D toDTO(E entity);

    E toEntity(D dto);

    List<D> toDTOList(List<E> entityList);

    List<E> toEntityList(List<D> dtoList);
}
