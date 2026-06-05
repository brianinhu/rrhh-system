package com.mitocode.rrhh_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ModeloConflictException extends RuntimeException {
    public ModeloConflictException(String mensaje) {
        super(mensaje);
    }
}
