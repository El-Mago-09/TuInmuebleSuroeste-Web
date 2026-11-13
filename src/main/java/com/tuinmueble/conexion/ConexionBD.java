package com.tuinmueble.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Administra la conexión JDBC con la base de datos MySQL del proyecto
 * Tu Inmueble Suroeste.
 *
 * Los datos de acceso se leen del archivo db.properties, que se copia en
 * WEB-INF/classes al construir la aplicación. Ese archivo no se sube al
 * repositorio para no dejar credenciales escritas en el código.
 *
 * @author Ometh Martinez Potes
 */
public final class ConexionBD {

    private static final String ARCHIVO_CONFIGURACION = "db.properties";
    private static final String DRIVER_MYSQL = "com.mysql.cj.jdbc.Driver";

    private static String url;
    private static String usuario;
    private static String contrasena;

    private ConexionBD() {
        // Clase utilitaria: no se instancia.
    }

    /**
     * Abre una nueva conexión con la base de datos.
     *
     * @return conexión abierta; quien la usa debe cerrarla (try-with-resources)
     * @throws SQLException si no es posible conectarse
     */
    public static synchronized Connection obtenerConexion() throws SQLException {
        if (url == null) {
            cargarConfiguracion();
        }
        return DriverManager.getConnection(url, usuario, contrasena);
    }

    private static void cargarConfiguracion() throws SQLException {
        Properties propiedades = new Properties();
        try (InputStream entrada = ConexionBD.class.getClassLoader()
                .getResourceAsStream(ARCHIVO_CONFIGURACION)) {
            if (entrada == null) {
                throw new SQLException("No se encontró " + ARCHIVO_CONFIGURACION
                        + " en WEB-INF/classes. Cópielo desde db.properties.example.");
            }
            propiedades.load(entrada);
        } catch (IOException e) {
            throw new SQLException("No se pudo leer " + ARCHIVO_CONFIGURACION + ".", e);
        }
        try {
            Class.forName(DRIVER_MYSQL);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el conector JDBC de MySQL en WEB-INF/lib.", e);
        }
        url = propiedades.getProperty("db.url");
        usuario = propiedades.getProperty("db.usuario");
        contrasena = propiedades.getProperty("db.contrasena", "");
    }
}
