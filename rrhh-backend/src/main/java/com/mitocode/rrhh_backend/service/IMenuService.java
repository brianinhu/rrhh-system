package com.mitocode.rrhh_backend.service;

import com.mitocode.rrhh_backend.dto.MenuDTO;
import com.mitocode.rrhh_backend.model.Menu;

import java.util.List;

public interface IMenuService {
    List<MenuDTO> listarMenusUsuarioLogueado();

    List<Menu> listarTodos();

    Menu registrar(Menu menu);

    Menu actualizar(Integer id, Menu menu);

    void eliminar(Integer id);
}
