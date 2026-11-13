package com.tuinmueble.controlador;

import com.tuinmueble.dao.InmuebleDAO;
import com.tuinmueble.dao.InmuebleDAOImpl;
import com.tuinmueble.modelo.Inmueble;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador web del módulo de inmuebles.
 *
 * GET  (consultas, no cambian datos): listar, buscar por municipio,
 *      formulario nuevo y formulario de edición.
 * POST (cambian datos): insertar, actualizar y eliminar.
 *
 * Después de cada POST se redirige a la lista (patrón Post/Redirect/Get)
 * para que al recargar la página no se repita la operación.
 *
 * @author Ometh Martinez Potes
 */
@WebServlet("/inmuebles")
public class InmuebleServlet extends HttpServlet {

    private static final String VISTA_LISTA = "/WEB-INF/vistas/lista.jsp";
    private static final String VISTA_FORMULARIO = "/WEB-INF/vistas/formulario.jsp";
    private static final String VISTA_ERROR = "/WEB-INF/vistas/error.jsp";
    private static final int ID_PROPIETARIO_PRUEBA = 1;

    private final InmuebleDAO inmuebleDao = new InmuebleDAOImpl();

    @Override
    protected void doGet(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        String accion = valorOVacio(solicitud.getParameter("accion"));
        try {
            switch (accion) {
                case "nuevo" -> mostrarFormulario(solicitud, respuesta, new Inmueble(), new ArrayList<>());
                case "editar" -> editarInmueble(solicitud, respuesta);
                case "buscar" -> buscarPorMunicipio(solicitud, respuesta);
                default -> listarInmuebles(solicitud, respuesta);
            }
        } catch (SQLException e) {
            mostrarError(solicitud, respuesta, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        String accion = valorOVacio(solicitud.getParameter("accion"));
        try {
            switch (accion) {
                case "insertar" -> guardarInmueble(solicitud, respuesta, false);
                case "actualizar" -> guardarInmueble(solicitud, respuesta, true);
                case "eliminar" -> eliminarInmueble(solicitud, respuesta);
                default -> respuesta.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        } catch (SQLException e) {
            mostrarError(solicitud, respuesta, e);
        }
    }

    private void listarInmuebles(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws SQLException, ServletException, IOException {
        solicitud.setAttribute("inmuebles", inmuebleDao.listarTodos());
        solicitud.getRequestDispatcher(VISTA_LISTA).forward(solicitud, respuesta);
    }

    private void buscarPorMunicipio(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws SQLException, ServletException, IOException {
        String municipio = valorOVacio(solicitud.getParameter("municipio"));
        List<Inmueble> inmuebles = municipio.isEmpty()
                ? inmuebleDao.listarTodos()
                : inmuebleDao.listarPorMunicipio(municipio);
        solicitud.setAttribute("inmuebles", inmuebles);
        solicitud.setAttribute("municipioBuscado", municipio);
        solicitud.getRequestDispatcher(VISTA_LISTA).forward(solicitud, respuesta);
    }

    private void editarInmueble(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws SQLException, ServletException, IOException {
        Integer idInmueble = leerEntero(solicitud.getParameter("id"));
        Optional<Inmueble> encontrado = idInmueble == null
                ? Optional.empty()
                : inmuebleDao.consultarPorId(idInmueble);
        if (encontrado.isEmpty()) {
            redirigirConMensaje(solicitud, respuesta, "El inmueble solicitado no existe.");
            return;
        }
        mostrarFormulario(solicitud, respuesta, encontrado.get(), new ArrayList<>());
    }

    private void guardarInmueble(HttpServletRequest solicitud, HttpServletResponse respuesta,
            boolean esActualizacion) throws SQLException, ServletException, IOException {
        List<String> errores = new ArrayList<>();
        Inmueble inmueble = leerFormulario(solicitud, errores);

        if (esActualizacion) {
            Integer idInmueble = leerEntero(solicitud.getParameter("idInmueble"));
            if (idInmueble == null) {
                errores.add("El id del inmueble no es válido.");
            } else {
                inmueble.setIdInmueble(idInmueble);
            }
        }
        if (!errores.isEmpty()) {
            mostrarFormulario(solicitud, respuesta, inmueble, errores);
            return;
        }

        if (esActualizacion) {
            boolean actualizado = inmuebleDao.actualizar(inmueble);
            redirigirConMensaje(solicitud, respuesta, actualizado
                    ? "Inmueble " + inmueble.getIdInmueble() + " actualizado."
                    : "El inmueble ya no existe; no se actualizó.");
        } else {
            inmueble.setIdUsuario(ID_PROPIETARIO_PRUEBA);
            int idGenerado = inmuebleDao.insertar(inmueble);
            redirigirConMensaje(solicitud, respuesta, "Inmueble registrado con id " + idGenerado + ".");
        }
    }

    private void eliminarInmueble(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws SQLException, IOException {
        Integer idInmueble = leerEntero(solicitud.getParameter("id"));
        boolean eliminado = idInmueble != null && inmuebleDao.eliminar(idInmueble);
        redirigirConMensaje(solicitud, respuesta, eliminado
                ? "Inmueble " + idInmueble + " eliminado."
                : "El inmueble no existe; no se eliminó.");
    }

    /** Lee y valida los campos del formulario; agrega a la lista los errores encontrados. */
    private Inmueble leerFormulario(HttpServletRequest solicitud, List<String> errores) {
        Inmueble inmueble = new Inmueble();
        inmueble.setTitulo(valorOVacio(solicitud.getParameter("titulo")));
        inmueble.setDescripcion(valorOVacio(solicitud.getParameter("descripcion")));
        inmueble.setTipo(valorOVacio(solicitud.getParameter("tipo")));
        inmueble.setOperacion(valorOVacio(solicitud.getParameter("operacion")));
        inmueble.setDireccion(valorOVacio(solicitud.getParameter("direccion")));
        inmueble.setMunicipio(valorOVacio(solicitud.getParameter("municipio")));
        inmueble.setEstado(valorOVacio(solicitud.getParameter("estado")));

        if (inmueble.getTitulo().isEmpty()) {
            errores.add("El título es obligatorio.");
        } else if (inmueble.getTitulo().length() > 150) {
            errores.add("El título no puede superar 150 caracteres.");
        }
        if (inmueble.getTipo().isEmpty()) {
            errores.add("Seleccione el tipo de inmueble.");
        }
        if (inmueble.getOperacion().isEmpty()) {
            errores.add("Seleccione la operación (venta o arriendo).");
        }
        if (inmueble.getMunicipio().isEmpty()) {
            errores.add("El municipio es obligatorio.");
        }
        if (inmueble.getEstado().isEmpty()) {
            inmueble.setEstado("disponible");
        }

        String precioTexto = valorOVacio(solicitud.getParameter("precio"));
        try {
            BigDecimal precio = new BigDecimal(precioTexto);
            if (precio.signum() <= 0) {
                errores.add("El precio debe ser mayor que cero.");
            }
            inmueble.setPrecio(precio);
        } catch (NumberFormatException e) {
            errores.add("Escriba un precio válido, solo números (ejemplo: 350000000).");
        }
        return inmueble;
    }

    private void mostrarFormulario(HttpServletRequest solicitud, HttpServletResponse respuesta,
            Inmueble inmueble, List<String> errores) throws ServletException, IOException {
        solicitud.setAttribute("inmueble", inmueble);
        solicitud.setAttribute("errores", errores);
        solicitud.getRequestDispatcher(VISTA_FORMULARIO).forward(solicitud, respuesta);
    }

    private void mostrarError(HttpServletRequest solicitud, HttpServletResponse respuesta,
            SQLException error) throws ServletException, IOException {
        log("Error de base de datos", error);
        solicitud.setAttribute("mensajeError", error.getMessage());
        solicitud.getRequestDispatcher(VISTA_ERROR).forward(solicitud, respuesta);
    }

    private void redirigirConMensaje(HttpServletRequest solicitud, HttpServletResponse respuesta,
            String mensaje) throws IOException {
        respuesta.sendRedirect(solicitud.getContextPath() + "/inmuebles?mensaje="
                + URLEncoder.encode(mensaje, StandardCharsets.UTF_8));
    }

    private static Integer leerEntero(String texto) {
        try {
            return Integer.valueOf(valorOVacio(texto));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String valorOVacio(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
