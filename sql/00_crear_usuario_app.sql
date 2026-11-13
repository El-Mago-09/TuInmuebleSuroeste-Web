-- ============================================================
-- PROYECTO: TU INMUEBLE SUROESTE
-- Evidencia: GA7-220501096-AA2-EV01
-- Usuario de MySQL exclusivo para la aplicación Java (no se usa root).
-- Ejecutar UNA VEZ en MySQL Workbench con la conexión de root.
-- Cambie la contraseña y escriba la misma en db.properties.
-- ============================================================

CREATE USER IF NOT EXISTS 'tuinmueble_app'@'localhost' IDENTIFIED BY 'CAMBIE_ESTA_CONTRASENA';
GRANT ALL PRIVILEGES ON tu_inmueble_suroeste.* TO 'tuinmueble_app'@'localhost';
FLUSH PRIVILEGES;
