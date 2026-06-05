package com.mitocode.rrhh_backend.service.impl;

import com.mitocode.rrhh_backend.dto.CambioPasswordDTO;
import com.mitocode.rrhh_backend.exception.ModeloNotFoundException;
import com.mitocode.rrhh_backend.model.Usuario;
import com.mitocode.rrhh_backend.repository.IUsuarioRepository;
import com.mitocode.rrhh_backend.service.IAuthService;
import com.mitocode.rrhh_backend.service.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final IUsuarioRepository usuarioRepository;
    // private final IEmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // ---------------------------------
    // Métodos privados o helper methods
    // ---------------------------------


    private void enviarEmailRecuperacion(String email, String token) {
        String enlace = frontendUrl + "/restablecer-password/" + token; // Ajusta según tu ruta real
        String asunto = "Recuperación de Contraseña";

        String htmlBody = """
                <h3>Recuperación de Cuenta</h3>
                <p>Hola %s,</p>
                <p>Has solicitado restablecer tu contraseña.</p>
                <p><a href='%s'>Haga clic aquí para cambiar su contraseña</a></p>
                <br><small>Este enlace expira en 15 minutos.</small>
                """.formatted(email, enlace);

        // emailService.enviarCorreo(email, asunto, htmlBody);
    }
}
