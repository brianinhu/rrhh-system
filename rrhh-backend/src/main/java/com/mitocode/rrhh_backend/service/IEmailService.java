package com.mitocode.rrhh_backend.service;

public interface IEmailService {
    void enviarCorreo(String destinatario, String asunto, String templateHtml);
}
