package com.mitocode.rrhh_backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice // Le dice a Spring "Oye, si algo explota en cualquier controller, maneja la excepción aquí"
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // --- Credenciales inválidas (Error 401) ---
    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "El usuario o la contraseña es incorrecta.");
        problemDetail.setTitle("Autenticación Fallida");
        problemDetail.setType(URI.create("https://rrhh-backend.com/errors/invalid-credentials"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    // --- Cuenta deshabilitada (Error 403) ---
    @ExceptionHandler(DisabledException.class)
    public ProblemDetail handleDisabledException() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Tu cuenta está desactivada. Contacta al administrador.");
        problemDetail.setTitle("Cuenta Deshabilitada");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    // --- Recurso ya existe (Error 409) ---
    @ExceptionHandler(ModeloConflictException.class)
    public ProblemDetail handleModeloConflictException(ModeloConflictException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Conflicto de recurso");
        problemDetail.setType(URI.create("https://rrhh-backend.com/errors/recurso-ya-existe"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    // --- Recurso no encontrado (Error 404) ---
    @ExceptionHandler(ModeloNotFoundException.class)
    public ProblemDetail handleModeloNotFoundException(ModeloNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Recurso no encontrado");
        problemDetail.setType(URI.create("https://example.com/problemas/recurso-no-encontrado"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    // --- Validaciones de @Valid en un DTO (Error 400) ---
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        String detalles = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detalles);

        problemDetail.setTitle("Error de Validación");
        problemDetail.setType(URI.create("https://example.com/problemas/error-validacion"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
