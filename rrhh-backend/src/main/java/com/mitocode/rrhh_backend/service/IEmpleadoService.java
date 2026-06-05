package com.mitocode.rrhh_backend.service;

import com.mitocode.rrhh_backend.dto.empleado.EmpleadoDetalladoResponseDTO;
import com.mitocode.rrhh_backend.dto.empleado.EmpleadoRequestDTO;
import com.mitocode.rrhh_backend.dto.empleado.EmpleadoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface IEmpleadoService {

    Page<EmpleadoResponseDTO> listar(String term, Pageable pageable);

    Double obtenerNominaTotal();

    Long contarTotalEmpleados();

    EmpleadoDetalladoResponseDTO getById(Integer id);

    EmpleadoResponseDTO guardar(EmpleadoRequestDTO empleadoRequestDTO, MultipartFile archivo);

    EmpleadoResponseDTO actualizar(Integer id, EmpleadoRequestDTO empleadoRequestDTO, MultipartFile archivo);

    void eliminar(Integer id);

    void asignarProyecto(Integer idEmpleado, Integer idProyecto);
}
