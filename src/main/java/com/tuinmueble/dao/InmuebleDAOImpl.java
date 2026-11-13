package com.tuinmueble.dao;

import com.tuinmueble.conexion.ConexionBD;
import com.tuinmueble.modelo.Inmueble;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC del CRUD de inmuebles sobre MySQL.
 * Usa PreparedStatement para evitar inyección SQL y try-with-resources
 * para cerrar siempre la conexión, la sentencia y el resultado.
 *
 * @author Ometh Martinez Potes
 */
public class InmuebleDAOImpl implements InmuebleDAO {

    private static final String SQL_INSERTAR =
            "INSERT INTO inmuebles (id_usuario, titulo, descripcion, tipo, operacion, "
            + "precio, direccion, municipio, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_CONSULTAR_POR_ID =
            "SELECT * FROM inmuebles WHERE id_inmueble = ?";

    private static final String SQL_LISTAR_TODOS =
            "SELECT * FROM inmuebles ORDER BY id_inmueble";

    private static final String SQL_LISTAR_POR_MUNICIPIO =
            "SELECT * FROM inmuebles WHERE municipio = ? ORDER BY id_inmueble";

    private static final String SQL_ACTUALIZAR =
            "UPDATE inmuebles SET titulo = ?, descripcion = ?, tipo = ?, operacion = ?, "
            + "precio = ?, direccion = ?, municipio = ?, estado = ? WHERE id_inmueble = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM inmuebles WHERE id_inmueble = ?";

    @Override
    public int insertar(Inmueble inmueble) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(
                        SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setInt(1, inmueble.getIdUsuario());
            sentencia.setString(2, inmueble.getTitulo());
            sentencia.setString(3, inmueble.getDescripcion());
            sentencia.setString(4, inmueble.getTipo());
            sentencia.setString(5, inmueble.getOperacion());
            sentencia.setBigDecimal(6, inmueble.getPrecio());
            sentencia.setString(7, inmueble.getDireccion());
            sentencia.setString(8, inmueble.getMunicipio());
            sentencia.setString(9, inmueble.getEstado());
            sentencia.executeUpdate();

            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                if (llaves.next()) {
                    int idGenerado = llaves.getInt(1);
                    inmueble.setIdInmueble(idGenerado);
                    return idGenerado;
                }
            }
            throw new SQLException("No se obtuvo el id del inmueble insertado.");
        }
    }

    @Override
    public Optional<Inmueble> consultarPorId(int idInmueble) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(SQL_CONSULTAR_POR_ID)) {
            sentencia.setInt(1, idInmueble);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearInmueble(resultado));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Inmueble> listarTodos() throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(SQL_LISTAR_TODOS);
                ResultSet resultado = sentencia.executeQuery()) {
            return mapearLista(resultado);
        }
    }

    @Override
    public List<Inmueble> listarPorMunicipio(String municipio) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(SQL_LISTAR_POR_MUNICIPIO)) {
            sentencia.setString(1, municipio);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return mapearLista(resultado);
            }
        }
    }

    @Override
    public boolean actualizar(Inmueble inmueble) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {
            sentencia.setString(1, inmueble.getTitulo());
            sentencia.setString(2, inmueble.getDescripcion());
            sentencia.setString(3, inmueble.getTipo());
            sentencia.setString(4, inmueble.getOperacion());
            sentencia.setBigDecimal(5, inmueble.getPrecio());
            sentencia.setString(6, inmueble.getDireccion());
            sentencia.setString(7, inmueble.getMunicipio());
            sentencia.setString(8, inmueble.getEstado());
            sentencia.setInt(9, inmueble.getIdInmueble());
            return sentencia.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int idInmueble) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {
            sentencia.setInt(1, idInmueble);
            return sentencia.executeUpdate() > 0;
        }
    }

    private List<Inmueble> mapearLista(ResultSet resultado) throws SQLException {
        List<Inmueble> inmuebles = new ArrayList<>();
        while (resultado.next()) {
            inmuebles.add(mapearInmueble(resultado));
        }
        return inmuebles;
    }

    private Inmueble mapearInmueble(ResultSet resultado) throws SQLException {
        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(resultado.getInt("id_inmueble"));
        inmueble.setIdUsuario(resultado.getInt("id_usuario"));
        inmueble.setTitulo(resultado.getString("titulo"));
        inmueble.setDescripcion(resultado.getString("descripcion"));
        inmueble.setTipo(resultado.getString("tipo"));
        inmueble.setOperacion(resultado.getString("operacion"));
        inmueble.setPrecio(resultado.getBigDecimal("precio"));
        inmueble.setDireccion(resultado.getString("direccion"));
        inmueble.setMunicipio(resultado.getString("municipio"));
        inmueble.setEstado(resultado.getString("estado"));
        Timestamp fecha = resultado.getTimestamp("fecha_publicacion");
        if (fecha != null) {
            inmueble.setFechaPublicacion(fecha.toLocalDateTime());
        }
        return inmueble;
    }
}
