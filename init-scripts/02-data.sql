-- 1. Conexión a la base de datos de negocio
\c rrhh_core_db;

-- 2. Tablas Independientes (Nivel 1)
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(100)
);

CREATE TABLE menus (
    id SERIAL PRIMARY KEY,
    icono VARCHAR(50) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    url VARCHAR(200) NOT NULL
);

CREATE TABLE cargos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE departamentos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200)
);

CREATE TABLE proyectos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    estado VARCHAR(20) NOT NULL DEFAULT 'PLANIFICACION'
);

CREATE TABLE curriculums (
    id SERIAL PRIMARY KEY,
    nivel_estudios VARCHAR(255) NOT NULL,
    especialidad VARCHAR(255) NOT NULL,
    url_archivo VARCHAR(255)
);

-- 3. Tabla de EMPLEADOS (Nivel 2 - Incluye Auditoría Embeddable)
CREATE TABLE empleados (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50),
    id_cargo INT NOT NULL,
    id_departamento INT NOT NULL,
    id_curriculum INT UNIQUE,
    sueldo DOUBLE PRECISION NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    -- Campos de Auditoria.java
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    -- Constraints
    CONSTRAINT FK_EMPLEADO_CARGO FOREIGN KEY (id_cargo) REFERENCES cargos(id),
    CONSTRAINT FK_EMPLEADO_DEPARTAMENTO FOREIGN KEY (id_departamento) REFERENCES departamentos(id),
    CONSTRAINT FK_EMPLEADO_CURRICULUM FOREIGN KEY (id_curriculum) REFERENCES curriculums(id)
);

-- 4. Tabla de USUARIOS (Nivel 3)
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    keycloak_id VARCHAR(255) NOT NULL UNIQUE,
    id_empleado INT UNIQUE NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    -- Constraints
    CONSTRAINT fk_usuario_empleado FOREIGN KEY (id_empleado) REFERENCES empleados(id)
);

-- 5. Tablas Intermedias (Relaciones Many-to-Many)
CREATE TABLE menu_rol (
    id_menu INT REFERENCES menus(id),
    id_rol INT REFERENCES roles(id),
    PRIMARY KEY (id_menu, id_rol)
);

CREATE TABLE empleado_proyecto (
    id_empleado INT REFERENCES empleados(id),
    id_proyecto INT REFERENCES proyectos(id),
    PRIMARY KEY (id_empleado, id_proyecto)
);

-- 6. Seed Data

INSERT INTO roles (nombre, descripcion) VALUES 
('ADMIN', 'Acceso total al sistema'),
('EXECUTIVE', 'Visualización estratégica y proyectos'),
('HR_BP', 'Gestión de colaboradores y proyectos'),
('USER', 'Acceso básico al dashboard');
INSERT INTO menus (nombre, icono, url) VALUES 
('Dashboard', 'dashboard', '/dashboard'),
('Colaboradores', 'group', '/empleados'),
('Proyectos', 'assignment', '/proyectos'),
('Seguridad', 'security', '/seguridad');

-- 7. Matriz de permisos (menu_rol)
-- ADMIN (ID 1): Todo
INSERT INTO menu_rol (id_menu, id_rol) VALUES (1, 1), (2, 1), (3, 1), (4, 1);

-- EXECUTIVE (ID 2): Dashboard y Proyectos
INSERT INTO menu_rol (id_menu, id_rol) VALUES (1, 2), (3, 2);

-- HR_BP (ID 3): Dashboard, Colaboradores y Proyectos
INSERT INTO menu_rol (id_menu, id_rol) VALUES (1, 3), (2, 3), (3, 3);

-- USER (ID 4): Solo Dashboard
INSERT INTO menu_rol (id_menu, id_rol) VALUES (1, 4);

INSERT INTO cargos (nombre, descripcion) VALUES
('Cargo 1', 'Descripción del Cargo 1'),
('Cargo 2', 'Descripción del Cargo 2'),
('Cargo 3', 'Descripción del Cargo 3'),
('Cargo 4', 'Descripción del Cargo 4'),
('Cargo 5', 'Descripción del Cargo 5');
INSERT INTO departamentos (nombre, descripcion) VALUES 
('Departamento 1', 'Descripción del Departamento 1'),
('Departamento 2', 'Descripción del Departamento 2'),
('Departamento 3', 'Descripción del Departamento 3'),
('Departamento 4', 'Descripción del Departamento 4'),
('Departamento 5', 'Descripción del Departamento 5');
INSERT INTO curriculums (nivel_estudios, especialidad) VALUES
('Licenciatura', 'Ingeniería de Sistemas'),
('Maestría', 'Administración de Empresas'),
('Doctorado', 'Ciencias de la Computación'),
('Licenciatura', 'Psicología Organizacional'),
('Maestría', 'Recursos Humanos');
INSERT INTO empleados (nombre, apellido, id_cargo, id_departamento, id_curriculum, sueldo, estado) VALUES ('John', 'Doe', 1, 1, 1, 1200.0, 'ACTIVO');

INSERT INTO usuarios (email, keycloak_id, id_empleado, enabled) 
VALUES ('jdoe@ripley.com.pe', '90127546-231a-4536-a89b-1277823dc6e8', 1, TRUE);