# Sistema de Gestión Centralizada de Colaboradores y Proyectos

Plataforma web para equipos de Recursos Humanos, que centraliza la gestión del personal, los expedientes y la asignación de colaboradores a proyectos, con información ejecutiva para apoyar decisiones organizacionales.

## Stack tecnológico

- **Frontend:** Angular 21, TypeScript 5.9, Angular Material 21, Tailwind CSS 3.4 y RxJS 7.8
- **Backend:** Java 25 y Spring Boot 4.0.1
- **Persistencia:** PostgreSQL 16 y Spring Data JPA
- **Seguridad:** Keycloak 26.1.4, OAuth2/JWT y Spring Security
- **Integraciones:** AWS SDK S3 para currículos y archivos
- **Arquitectura y despliegue:** Maven, Docker y Docker Compose

## Características principales

- Identidad y autorización centralizadas: provisión de usuarios en Keycloak, asignación de roles, activación y baja sincronizadas, autenticación JWT y menús dinámicos por permisos.
- CRUD paginado de empleados con búsqueda por término, detalle enriquecido y datos laborales como cargo, departamento y estudios.
- Gestión de proyectos y equipos con estados configurables y sincronización muchos a muchos de las asignaciones entre empleados y proyectos.
- Dashboard ejecutivo basado en consultas agregadas: nómina activa, distribución por departamento y cargo, estados de proyectos y principales sueldos.
- Expedientes con currículos almacenados en AWS S3, enlaces temporales firmados y limpieza del archivo al reemplazarlo o eliminar al empleado.

## Instalación rápida

Requisitos: Docker y Docker Compose.

```bash
git clone https://github.com/brianinhu/rrhh-system.git
cd rrhh-system
docker compose up --build -d
```

Abre la aplicación en [http://localhost:4200](http://localhost:4200). 

Los servicios adicionales quedan disponibles en Keycloak (`http://localhost:8180`) y el backend (`http://localhost:8080/api`).
