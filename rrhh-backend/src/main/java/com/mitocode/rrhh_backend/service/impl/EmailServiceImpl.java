package com.mitocode.rrhh_backend.service.impl;

import com.mitocode.rrhh_backend.service.IEmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

// @Service
@RequiredArgsConstructor
public class EmailServiceImpl implements IEmailService {
    private final JavaMailSender javaMailSender;

    @Override
    public void enviarCorreo(String destinatario, String asunto, String templateHtml) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("brianincah10@gmail.com");
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(templateHtml, true); // El segundo parámetro indica que el contenido es HTML
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Fallo crítico en el servicio de mensajería", e);
        }
    }
}
