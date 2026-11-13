-- ============================================================
-- PROYECTO: TU INMUEBLE SUROESTE
-- Evidencia: GA6-220501096-AA2-EV03
-- Script de base de datos del proyecto
-- ============================================================

-- Eliminar la base de datos si ya existe
DROP DATABASE IF EXISTS tu_inmueble_suroeste;

-- Crear la base de datos
CREATE DATABASE tu_inmueble_suroeste
CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;

-- Seleccionar la base de datos
USE tu_inmueble_suroeste;


-- ============================================================
-- 1. TABLA USUARIOS
-- ============================================================

CREATE TABLE usuarios (
    id_usuario INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    telefono VARCHAR(20) NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL DEFAULT 'cliente',
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_usuarios
        PRIMARY KEY (id_usuario),

    CONSTRAINT uq_usuarios_correo
        UNIQUE (correo),

    CONSTRAINT chk_usuarios_rol
        CHECK (rol IN ('cliente', 'administrador'))
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- ============================================================
-- 2. TABLA INMUEBLES
-- ============================================================

CREATE TABLE inmuebles (
    id_inmueble INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT NULL,
    tipo VARCHAR(50) NOT NULL,
    operacion VARCHAR(30) NOT NULL,
    precio DECIMAL(12,2) NOT NULL,
    direccion VARCHAR(200) NULL,
    municipio VARCHAR(100) NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'disponible',
    fecha_publicacion DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_inmuebles
        PRIMARY KEY (id_inmueble),

    CONSTRAINT fk_inmuebles_usuarios
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_inmuebles_precio
        CHECK (precio > 0),

    CONSTRAINT chk_inmuebles_operacion
        CHECK (operacion IN ('venta', 'arriendo')),

    CONSTRAINT chk_inmuebles_estado
        CHECK (
            estado IN (
                'disponible',
                'reservado',
                'vendido',
                'arrendado'
            )
        )
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- ============================================================
-- 3. TABLA FAVORITOS
-- ============================================================

CREATE TABLE favoritos (
    id_favorito INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    id_inmueble INT NOT NULL,
    fecha_agregado DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_favoritos
        PRIMARY KEY (id_favorito),

    CONSTRAINT uq_favorito_usuario_inmueble
        UNIQUE (id_usuario, id_inmueble),

    CONSTRAINT fk_favoritos_usuarios
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_favoritos_inmuebles
        FOREIGN KEY (id_inmueble)
        REFERENCES inmuebles(id_inmueble)
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- ============================================================
-- 4. TABLA SOLICITUDES
-- ============================================================

CREATE TABLE solicitudes (
    id_solicitud INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    id_inmueble INT NOT NULL,
    mensaje TEXT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'pendiente',
    fecha_solicitud DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_solicitudes
        PRIMARY KEY (id_solicitud),

    CONSTRAINT fk_solicitudes_usuarios
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_solicitudes_inmuebles
        FOREIGN KEY (id_inmueble)
        REFERENCES inmuebles(id_inmueble)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_solicitudes_estado
        CHECK (
            estado IN (
                'pendiente',
                'aprobada',
                'rechazada'
            )
        )
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- ============================================================
-- 5. TABLA CONTRATOS
-- ============================================================

CREATE TABLE contratos (
    id_contrato INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    id_inmueble INT NOT NULL,
    tipo_contrato VARCHAR(30) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NULL,
    valor DECIMAL(12,2) NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'activo',

    CONSTRAINT pk_contratos
        PRIMARY KEY (id_contrato),

    CONSTRAINT fk_contratos_usuarios
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_contratos_inmuebles
        FOREIGN KEY (id_inmueble)
        REFERENCES inmuebles(id_inmueble)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_contratos_tipo
        CHECK (
            tipo_contrato IN (
                'venta',
                'arriendo'
            )
        ),

    CONSTRAINT chk_contratos_valor
        CHECK (valor > 0),

    CONSTRAINT chk_contratos_estado
        CHECK (
            estado IN (
                'activo',
                'finalizado',
                'cancelado'
            )
        )
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- ============================================================
-- 6. TABLA PAGOS
-- ============================================================

CREATE TABLE pagos (
    id_pago INT NOT NULL AUTO_INCREMENT,
    id_contrato INT NOT NULL,
    fecha_pago DATE NOT NULL,
    valor DECIMAL(12,2) NOT NULL,
    metodo_pago VARCHAR(50) NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'pendiente',

    CONSTRAINT pk_pagos
        PRIMARY KEY (id_pago),

    CONSTRAINT fk_pagos_contratos
        FOREIGN KEY (id_contrato)
        REFERENCES contratos(id_contrato)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_pagos_valor
        CHECK (valor > 0),

    CONSTRAINT chk_pagos_estado
        CHECK (
            estado IN (
                'pendiente',
                'pagado',
                'rechazado'
            )
        )
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- ============================================================
-- VERIFICACIÓN DE LA CREACIÓN DE LAS TABLAS
-- ============================================================

SHOW TABLES;


-- ============================================================
-- VERIFICACIÓN DE RESTRICCIONES
-- ============================================================

SELECT
    TABLE_NAME AS tabla,
    CONSTRAINT_NAME AS restriccion,
    CONSTRAINT_TYPE AS tipo
FROM information_schema.TABLE_CONSTRAINTS
WHERE TABLE_SCHEMA = 'tu_inmueble_suroeste'
ORDER BY TABLE_NAME, CONSTRAINT_TYPE;