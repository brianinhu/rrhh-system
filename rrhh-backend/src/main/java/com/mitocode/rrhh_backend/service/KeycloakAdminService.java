package com.mitocode.rrhh_backend.service;

import com.mitocode.rrhh_backend.config.KeycloakAdminProperties;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final Keycloak keycloakAdmin;
    private final KeycloakAdminProperties properties;

    public String crearUsuario(String email, String password, Set<String> roles, String nombre, String apellido) {
        RealmResource realmResource = keycloakAdmin.realm(properties.getRealm());
        UsersResource usersResource = realmResource.users();

        // 1) Crear usuario limpio (sin lista de credenciales)
        UserRepresentation usuario = new UserRepresentation();
        usuario.setEmail(email);
        usuario.setUsername(email);
        usuario.setFirstName(nombre);
        usuario.setLastName(apellido);
        usuario.setEnabled(true);
        usuario.setEmailVerified(true);

        // 2) Guardar usuario en Keycloak
        try (Response response = usersResource.create(usuario)) {
            int status = response.getStatus();

            if (status == 201) {
                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                log.info("[Keycloak] Usuario creado con UUID: {}", userId);

                // 3) Establecer la contraseña de forma aislada para evitar el bug de Jackson
                try {
                    CredentialRepresentation credencial = new CredentialRepresentation();
                    credencial.setTemporary(false);
                    credencial.setType(CredentialRepresentation.PASSWORD);
                    credencial.setValue(password);

                    realmResource.users().get(userId).resetPassword(credencial);
                    log.info("[Keycloak] Contraseña establecida con éxito para el usuario: {}", userId);
                } catch (jakarta.ws.rs.WebApplicationException e) {
                    // Log detallado
                    String errorBody = e.getResponse().readEntity(String.class);
                    log.error("[Keycloak] Error detallado asignando contraseña al usuario {}: Status {}, Body: {}",
                            userId, e.getResponse().getStatus(), errorBody);
                } catch (Exception e) {
                    log.error("[Keycloak] Error genérico asignando contraseña al usuario {}: {}", userId,
                            e.getMessage());
                }

                // 4) Asignar Roles
                try {
                    asignarRoles(realmResource, userId, roles);
                } catch (Exception e) {
                    log.error("[Keycloak] Error crítico asignando roles al usuario {}: {}", userId, e.getMessage());
                }

                return userId;
            } else {
                String errorMsg = response.readEntity(String.class);
                log.error("[Keycloak] Error creando usuario: Status {}, Body: {}", status, errorMsg);
                throw new RuntimeException("Error creando usuario en Keycloak: " + errorMsg);
            }
        } catch (WebApplicationException e) {
            log.error("[Keycloak] Error de comunicación: {}", e.getResponse().readEntity(String.class));
            throw e;
        }
    }

    public void actualizarEstado(String keycloakId, boolean enabled) {
        UserResource userResource = keycloakAdmin.realm(properties.getRealm()).users().get(keycloakId);

        // Obtenemos la representación actual para no perder otros datos
        UserRepresentation usuario = userResource.toRepresentation();
        usuario.setEnabled(enabled);

        userResource.update(usuario);
        log.info("[Keycloak] Estado del usuario {} actualizado a {}", keycloakId, enabled);
    }

    public void eliminarUsuario(String keycloakId) {
        keycloakAdmin.realm(properties.getRealm()).users().get(keycloakId).remove();
        log.info("[Keycloak] Usuario con ID {} eliminado", keycloakId);
    }

    private void asignarRoles(RealmResource realmResource, String userId, Set<String> roles) {
        if (roles == null || roles.isEmpty())
            return;

        List<RoleRepresentation> rolesList = roles.stream()
                .map(rol -> {
                    try {
                        return realmResource.roles().get(rol).toRepresentation();
                    } catch (jakarta.ws.rs.NotFoundException e) {
                        log.warn("[Keycloak] El rol '{}' no existe en el Realm. Saltando...", rol);
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .toList();

        if (!rolesList.isEmpty()) {
            realmResource.users().get(userId).roles().realmLevel().add(rolesList);
        }
    }
}
