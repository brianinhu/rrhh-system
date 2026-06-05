package com.mitocode.rrhh_backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "keycloak.admin")
public class KeycloakAdminProperties {
    private String serverUrl;
    private String realm;
    private String adminRealm;
    private String adminClientId;
    private String username;
    private String password;
    private String clientSecret;
}
