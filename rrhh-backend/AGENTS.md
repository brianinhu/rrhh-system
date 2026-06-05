# AGENTS.md

## Panorama rápido
- Backend **Spring Boot 4.0.1** con **Java 25** y paquete base `com.mitocode.rrhh_backend` (ver `HELP.md`).
- Arquitectura por capas: `controller -> service -> repository`, con `mapper` para transformación y `model` para JPA.
- Integraciones clave: **PostgreSQL**, **Keycloak JWT resource server**, **Keycloak Admin Client**, **AWS S3** y correo SMTP.

## Flujo y límites de capas
- Los controladores solo orquestan y validan; la lógica vive en `service`.
- Ejemplo: `EmpleadoController` usa `multipart/form-data`, `Pageable` y HATEOAS (`EntityModel`) para `/empleados/{id}`.
- `UsuarioServiceImpl` sincroniza identidad local + Keycloak: crea el usuario remoto, guarda `keycloakId` y actualiza estado/elimina en ambos lados.
- `SecurityConfig` protege todo excepto `/auth/enviar-recuperacion`, `/auth/verificar-token`, `/auth/cambiar-password` y `/error`.

## Mapeo y DTOs
- Usa **MapStruct** con `componentModel = SPRING`.
- Los mappers ignoran campos que el service resuelve: por ejemplo `IEmpleadoMapper` ignora `cargo`, `departamento`, `proyectos`, `curriculum`, `auditoria` y `estado`.
- `toDetalladoResponseDTO` arma campos anidados (`cargo.nombre`, `departamento.nombre`) y listas derivadas (`idsProyectos`, `nombresProyectos`).

## Persistencia y consultas
- JPA/Hibernate usa `ddl-auto=validate`; no asumas que Hibernate crea el esquema.
- Repositorios con JPQL/proyecciones son comunes: `IEmpleadoRepository` calcula nómina, conteos y dashboards con `new ...DTO(...)`.
- Las entidades relevantes son `Empleado`, `Usuario`, `Curriculum`, `Proyecto`, `Cargo`, `Departamento` y `Rol`.

## Seguridad e integraciones
- JWT viene de Keycloak; `AuthController` lee `email` desde `@AuthenticationPrincipal Jwt`.
- `SecurityUtil` normaliza roles quitando el prefijo `ROLE_` para consultar menús (`menuRepository.getMenusByRoles(...)`).
- CORS está definido en dos lugares (`security/SecurityConfig.java` y `config/CorsConfig.java`) y ambos apuntan a `http://localhost:4200`.
- S3 se configura con `S3Config`; `EmpleadoServiceImpl` sube/borra el CV y genera URL firmada al leer.

## Errores y respuestas
- Excepciones de negocio usan `ModeloNotFoundException` y `ModeloConflictException`.
- `GlobalExceptionHandler` responde con `ProblemDetail` para 400/401/403/404/409; conserva ese formato al agregar errores nuevos.

## Flujo de trabajo local
- Build y pruebas en Windows: `./mvnw.cmd test` y `./mvnw.cmd spring-boot:run`.
- Revisa `api-test.http` y `pruebas.http` para ejemplos reales de consumo manual de endpoints.
- Antes de tocar código, confirma configuración local: PostgreSQL en `localhost:5432`, Keycloak en `localhost:8180`, frontend en `http://localhost:4200`.

## Convenciones útiles
- Mantén nombres con prefijo `I` en repositorios/servicios/mappers (`IEmpleadoService`, `IUsuarioMapper`).
- Usa Lombok de forma consistente; evita mezclar estilos sin motivo.
- Si cambias autenticación, menús o roles, revisa juntos `SecurityConfig`, `SecurityUtil`, `UsuarioServiceImpl` y `MenuRepository`.



