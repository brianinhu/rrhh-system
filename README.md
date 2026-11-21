# RRHH System

Sistema integral de recursos humanos que permite administrar empleados, proyectos, usuarios, estructura organizacional y indicadores del personal desde una sola plataforma web.

## Stack tecnológico

- **Frontend:** Angular 21, TypeScript 5.9, Angular Material 21, Tailwind CSS 3.4 y RxJS 7.8
- **Backend:** Java 25 y Spring Boot 4.0.1
- **Persistencia:** PostgreSQL 16 y Spring Data JPA
- **Seguridad:** Keycloak 26.1.4, OAuth2/JWT y Spring Security
- **Integraciones:** AWS SDK S3 para currículos y archivos
- **Arquitectura y despliegue:** Maven, Docker y Docker Compose

## Características principales

- Gestión completa de empleados, con búsqueda, paginación, estados y asignación a proyectos.
- Administración de proyectos y equipos con permisos por rol.
- Dashboard ejecutivo con métricas, gráficos y nómina total.
- Gestión de usuarios, roles, departamentos, cargos y estructura organizacional.
- Acceso seguro mediante Keycloak, autenticación OAuth2 y control de rutas por roles.

## Instalación rápida

Requisitos: Docker y Docker Compose.

```bash
git clone https://github.com/brianinhu/rrhh-system.git
cd rrhh-system
docker compose up --build -d
```

Abre la aplicación en [http://localhost:4200](http://localhost:4200). Los servicios adicionales quedan disponibles en Keycloak (`http://localhost:8180`) y el backend (`http://localhost:8080/api`).
