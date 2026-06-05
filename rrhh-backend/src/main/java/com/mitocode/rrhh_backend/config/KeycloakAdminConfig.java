package com.mitocode.rrhh_backend.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakAdminConfig {

    private final KeycloakAdminProperties properties;

    @Bean
    public Keycloak keycloakAdmin() {
        return KeycloakBuilder.builder()
                .serverUrl(properties.getServerUrl())
                .realm(properties.getAdminRealm())
                .clientId(properties.getAdminClientId())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientSecret(properties.getClientSecret())
                .resteasyClient(new org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)
                        .build())
                .build();
    }
}
