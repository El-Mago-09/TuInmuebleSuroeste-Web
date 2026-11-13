package com.tuinmueble.conexion;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

/**
 * Libera el driver de MySQL cuando Tomcat detiene o vuelve a desplegar la
 * aplicación, para evitar fugas de memoria y avisos en el registro.
 *
 * @author Ometh Martinez Potes
 */
@WebListener
public class CierreConexionesListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent evento) {
        ClassLoader cargadorAplicacion = Thread.currentThread().getContextClassLoader();
        for (Driver driver : Collections.list(DriverManager.getDrivers())) {
            if (driver.getClass().getClassLoader() == cargadorAplicacion) {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException e) {
                    evento.getServletContext().log("No se pudo liberar el driver JDBC", e);
                }
            }
        }
        AbandonedConnectionCleanupThread.checkedShutdown();
    }
}
