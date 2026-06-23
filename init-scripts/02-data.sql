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

-- 6. Seed Data Uneditable

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


-- 8. Seed Data Editable (Cargos, Departamentos, Curriculums, Empleados, Usuarios)
TRUNCATE TABLE cargos RESTART IDENTITY CASCADE;
INSERT INTO cargos (nombre, descripcion) VALUES
('Software Engineer', 'Ingeniero de Software Backend/Frontend'),
('Fullstack Developer', 'Desarrollador de aplicaciones de extremo a extremo'),
('Product Owner', 'Responsable de la definición y valor del producto'),
('QA Automation Engineer', 'Ingeniero de control de calidad automatizado'),
('Cloud & DevOps Architect', 'Arquitecto de infraestructura en la nube'),
('Scrum Master', 'Facilitador de metodologías ágiles'),
('HR Business Partner', 'Consultor estratégico de Recursos Humanos'),
('Talent Acquisition Specialist', 'Especialista en reclutamiento y selección'),
('UX/UI Designer', 'Diseñador de experiencia e interfaz de usuario'),
('Data Analyst', 'Analista de datos de negocio y rendimiento');

TRUNCATE TABLE departamentos RESTART IDENTITY CASCADE;
INSERT INTO departamentos (nombre, descripcion) VALUES 
('Tecnología e Innovación', 'TI / Desarrollo de Software corporativo'),
('Recursos Humanos', 'Gestión integral del talento y cultura'),
('Operaciones y Logística', 'Distribución, almacenes y Supply Chain'),
('Finanzas y Contabilidad', 'Control financiero y tesorería'),
('Marketing y Comercial', 'Estrategia de ventas, canales y marca'),
('Experiencia del Cliente', 'Diseño de servicios y atención omnicanal'),
('Analítica de Datos y BI', 'Business Intelligence y analítica avanzada'),
('Seguridad y Cumplimiento', 'Infraestructura, ciberseguridad y marco legal');

INSERT INTO proyectos (nombre, descripcion, estado) VALUES
('Portal Auto-gestión Ripley', 'Plataforma para que los empleados gestionen sus boletas y vacaciones', 'ACTIVO'),
('Core E-commerce 2.0', 'Migración de la plataforma principal de ventas en línea', 'ACTIVO'),
('App Ripley Express', 'Desarrollo móvil para envíos rápidos y tracking de pedidos', 'PLANIFICACION'),
('Sistema RRHH Core', 'Software interno de gestión de talento y asignaciones', 'ACTIVO'),
('Data Warehouse Corporativo', 'Centralización de datos para analítica de negocio masiva', 'PLANIFICACION'),
('Algoritmo de Crédito Tarjeta Ripley', 'Optimización del motor de riesgo financiero en tiempo real', 'PAUSADO'),
('Migración Cloud AWS 2026', 'Traspaso de servidores locales hacia infraestructura en la nube', 'FINALIZADO'),
('Automatización de Almacenes Lurín', 'Software para guiado de robots y stock automatizado', 'PAUSADO'),
('Plataforma Omni-canalidad', 'Integración de atención en tiendas físicas, web y WhatsApp', 'PLANIFICACION'),
('Pasarela de Pagos RipleyPay', 'Iniciativa de billetera digital integrada para retail', 'FINALIZADO'),
('Ripley Puntos Go', 'Renovación del sistema de fidelización de clientes', 'PAUSADO'),
('Ciberseguridad Zero Trust', 'Implementación de seguridad estricta en accesos internos corporativos', 'ACTIVO'),
('Célula Arquitectura Angular 21', 'Estandarización de componentes frontend para la empresa', 'ACTIVO'),
('Modernización Microservicios Spring Boot', 'Desacoplamiento del antiguo sistema monolítico', 'ACTIVO'),
('IA Asistente Servicio al Cliente', 'Implementación de inteligencia artificial conversacional para soporte', 'FINALIZADO');

TRUNCATE TABLE empleado_proyecto CASCADE;
TRUNCATE TABLE usuarios CASCADE;
TRUNCATE TABLE empleados RESTART IDENTITY CASCADE;
TRUNCATE TABLE curriculums RESTART IDENTITY CASCADE;

INSERT INTO curriculums (nivel_estudios, especialidad, url_archivo) VALUES
('Licenciatura', 'Ingeniería de Sistemas', 'default'),
('Maestría', 'Ciencias de la Computación', 'cv_carlos_mendoza.pdf'),
('Licenciatura', 'Ingeniería de Software', 'cv_ana_gomez.pdf'),
('Certificación', 'Scrum Alliance / Agile', 'cv_luis_torres.pdf'),
('Licenciatura', 'Diseño de Experiencia de Usuario', 'cv_sofia_castro.pdf'),
('Maestría', 'Administración de Empresas (MBA)', 'cv_jorge_ramirez.pdf'),
('Doctorado', 'Inteligencia Artificial y Big Data', 'cv_elena_paz.pdf'),
('Licenciatura', 'Psicología Organizacional', 'cv_ricardo_diaz.pdf'),
('Certificación', 'AWS Certified Solutions Architect', 'cv_marina_vega.pdf'),
('Licenciatura', 'Ingeniería de Sistemas', 'cv_pedro_soto.pdf'),
('Licenciatura', 'Marketing Digital', 'cv_lucia_martinez.pdf'),
('Maestría', 'Gestión del Talento Humano', 'cv_andrea_ruiz.pdf'),
('Licenciatura', 'Economía y Finanzas', 'cv_miguel_angel.pdf'),
('Certificación', 'ISTQB Advanced Test Automation', 'cv_gabriela_lopez.pdf'),
('Licenciatura', 'Ciencias de la Computación', 'cv_fernando_alonso.pdf'),
('Licenciatura', 'Ingeniería de Sistemas', 'cv_patricia_silva.pdf'),
('Maestría', 'Ciberseguridad y Redes', 'cv_roberto_guillen.pdf'),
('Licenciatura', 'Administración de Negocios', 'cv_claudia_morales.pdf'),
('Licenciatura', 'Diseño Gráfico Publicitario', 'cv_hugo_beltran.pdf'),   
('Licenciatura', 'Estadística e Informática', 'cv_valeria_rios.pdf');

-- Empleados
INSERT INTO empleados (nombre, apellido, id_cargo, id_departamento, id_curriculum, sueldo, estado, created_by) VALUES 
('John', 'Doe', 1, 1, 1, 4500.0, 'ACTIVO', 'system_seed');

INSERT INTO empleados (nombre, apellido, id_cargo, id_departamento, id_curriculum, sueldo, estado, created_by) VALUES
('Carlos', 'Mendoza', 1, 1, 2, 5200.0, 'ACTIVO', 'system_seed'),     -- Software Engineer / Tecnología
('Ana', 'Gómez', 2, 1, 3, 4800.0, 'ACTIVO', 'system_seed'),          -- Fullstack Dev / Tecnología
('Luis', 'Torres', 6, 1, 4, 5500.0, 'ACTIVO', 'system_seed'),        -- Scrum Master / Tecnología
('Sofía', 'Castro', 9, 6, 5, 4200.0, 'ACTIVO', 'system_seed'),       -- UX/UI Designer / Experiencia Cliente
('Jorge', 'Ramírez', 3, 5, 6, 7000.0, 'ACTIVO', 'system_seed'),      -- Product Owner / Marketing y Comercial
('Elena', 'Paz', 10, 7, 7, 6000.0, 'ACTIVO', 'system_seed'),         -- Data Analyst / Analítica de Datos
('Ricardo', 'Díaz', 7, 2, 8, 5800.0, 'ACTIVO', 'system_seed'),       -- HR Business Partner / RRHH
('Marina', 'Vega', 5, 1, 9, 8500.0, 'ACTIVO', 'system_seed'),        -- Cloud Architect / Tecnología
('Pedro', 'Soto', 1, 1, 10, 5000.0, 'ACTIVO', 'system_seed'),        -- Software Engineer / Tecnología
('Lucía', 'Martínez', 10, 7, 11, 4100.0, 'ACTIVO', 'system_seed'),   -- Data Analyst / Analítica de Datos
('Andrea', 'Ruiz', 8, 2, 12, 3800.0, 'ACTIVO', 'system_seed'),       -- Talent Acquisition / RRHH
('Miguel', 'Ángel', 3, 4, 13, 7200.0, 'ACTIVO', 'system_seed'),      -- Product Owner / Finanzas
('Gabriela', 'López', 4, 1, 14, 4600.0, 'ACTIVO', 'system_seed'),    -- QA Automation / Tecnología
('Fernando', 'Alonso', 2, 1, 15, 4900.0, 'ACTIVO', 'system_seed'),   -- Fullstack Dev / Tecnología
('Patricia', 'Silva', 1, 1, 16, 5100.0, 'ACTIVO', 'system_seed'),    -- Software Engineer / Tecnología
('Roberto', 'Guillén', 5, 8, 17, 8200.0, 'ACTIVO', 'system_seed'),   -- Cloud Architect / Seguridad
('Claudia', 'Morales', 7, 2, 18, 5600.0, 'ACTIVO', 'system_seed'),   -- HR Business Partner / RRHH
('Hugo', 'Beltrán', 9, 6, 19, 4000.0, 'ACTIVO', 'system_seed'),      -- UX/UI Designer / Experiencia Cliente
('Valeria', 'Ríos', 4, 1, 20, 4500.0, 'ACTIVO', 'system_seed');      -- QA Automation / Tecnología

INSERT INTO usuarios (email, keycloak_id, id_empleado, enabled) 
VALUES ('jdoe@ripley.com.pe', '90127546-231a-4536-a89b-1277823dc6e8', 1, TRUE);

INSERT INTO empleado_proyecto (id_empleado, id_proyecto) VALUES
(1, 4),  -- John Doe -> Sistema RRHH Core
(2, 2),  -- Carlos Mendoza -> Core E-commerce 2.0
(2, 14), -- Carlos Mendoza -> Modernización Microservicios Spring Boot (Multiproyecto)
(3, 2),  -- Ana Gómez -> Core E-commerce 2.0
(3, 13), -- Ana Gómez -> Célula Arquitectura Angular 21 (Multiproyecto)
(4, 2),  -- Luis Torres -> Core E-commerce 2.0
(4, 14), -- Luis Torres -> Modernización Microservicios Spring Boot (Multiproyecto)
(5, 1),  -- Sofía Castro -> Portal Auto-gestión Ripley
(5, 9),  -- Sofía Castro -> Plataforma Omni-canalidad (Multiproyecto)
(6, 2),  -- Jorge Ramírez -> Core E-commerce 2.0
(7, 5),  -- Elena Paz -> Data Warehouse Corporativo
(9, 7),  -- Marina Vega -> Migración Cloud AWS 2026
(9, 12), -- Marina Vega -> Ciberseguridad Zero Trust (Multiproyecto)
(10, 4), -- Pedro Soto -> Sistema RRHH Core
(14, 10),-- Miguel Ángel -> Pasarela de Pagos RipleyPay
(15, 2), -- Gabriela López -> Core E-commerce 2.0
(17, 13),-- Patricia Silva -> Célula Arquitectura Angular 21
(18, 12);-- Roberto Guillén -> Ciberseguridad Zero Trust