package com.mitocode.rrhh_backend.service.impl;

import com.mitocode.rrhh_backend.dto.MenuDTO;
import com.mitocode.rrhh_backend.model.Menu;
import com.mitocode.rrhh_backend.repository.IMenuRepository;
import com.mitocode.rrhh_backend.service.IMenuService;
import com.mitocode.rrhh_backend.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements IMenuService {

    private final IMenuRepository menuRepository;

    @Override
    public List<MenuDTO> listarMenusUsuarioLogueado() {
        // Usamos nuestra nueva utilidad para obtener los roles del JWT
        Set<String> roles = SecurityUtil.getCurrentUserRoles();

        // Llamamos al nuevo método del repositorio
        return menuRepository.getMenusByRoles(roles).stream()
                .map(m -> new MenuDTO(m.getNombre(), m.getIcono(), m.getUrl()))
                .toList();
    }

    @Override
    public List<Menu> listarTodos() {
        return menuRepository.findAll();
    }

    @Override
    @Transactional
    public Menu registrar(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    @Transactional
    public Menu actualizar(Integer id, Menu menu) {
        menu.setId(id);
        return menuRepository.save(menu);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        menuRepository.deleteById(id);
    }
}
