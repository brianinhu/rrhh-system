package com.mitocode.rrhh_backend.dto;

import lombok.Data;

@Data
public class CambioPasswordDTO {
    private String token;
    private String password;
    private String confirmPassword;
}
