-- ============================================================
-- PROYECTO: TU INMUEBLE SUROESTE
-- Evidencia: GA7-220501096-AA2-EV01
-- Datos de prueba para el módulo de inmuebles (CRUD con JDBC)
-- Ejecutar después de 01_Tu_Inmueble_Suroeste.sql
-- ============================================================

USE tu_inmueble_suroeste;

-- Usuario propietario de los inmuebles (datos ficticios de prueba)
INSERT INTO usuarios (nombre, apellido, correo, telefono, contrasena, rol)
VALUES ('Usuario', 'Prueba', 'propietario.prueba@ejemplo.com', '3000000000',
        'hash_de_prueba', 'cliente');

-- Inmuebles iniciales
INSERT INTO inmuebles (id_usuario, titulo, descripcion, tipo, operacion, precio,
                       direccion, municipio, estado)
VALUES
    (1, 'Casa campestre con vista al río Cauca',
     'Casa de 3 habitaciones, 2 baños y zona verde.', 'casa', 'venta',
     450000000.00, 'Vereda La Pradera', 'Santa Fe de Antioquia', 'disponible'),
    (1, 'Apartamento en el parque principal',
     'Apartamento de 2 habitaciones cerca al parque.', 'apartamento', 'arriendo',
     1200000.00, 'Calle 10 # 5-20', 'Jericó', 'disponible');

SELECT * FROM inmuebles;
