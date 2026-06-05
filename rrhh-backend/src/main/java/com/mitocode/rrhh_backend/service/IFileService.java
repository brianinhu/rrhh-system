package com.mitocode.rrhh_backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    // Sube el archivo y retorna el "Key" (nombre único en S3)
    String uploadFile(MultipartFile file);

    // Borra un archivo usando su Key
    void deleteFile(String key);

    // Genera la URL pública para acceder al archivo
    String getFileUrl(String key);
}
