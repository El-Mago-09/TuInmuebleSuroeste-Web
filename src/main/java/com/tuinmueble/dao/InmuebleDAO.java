package com.tuinmueble.dao;

import com.tuinmueble.modelo.Inmueble;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Operaciones CRUD del módulo de inmuebles.
 *
 * @author Ometh Martinez Potes
 */
public interface InmuebleDAO {

    /** Inserta un inmueble y devuelve el id generado por la base de datos. */
    int insertar(Inmueble inmueble) throws SQLException;

    /** Consulta un inmueble por su id. */
    Optional<Inmueble> consultarPorId(int idInmueble) throws SQLException;

    /** Lista todos los inmuebles ordenados por id. */
    List<Inmueble> listarTodos() throws SQLException;

    /** Lista los inmuebles de un municipio. */
    List<Inmueble> listarPorMunicipio(String municipio) throws SQLException;

    /** Actualiza todos los datos editables de un inmueble. */
    boolean actualizar(Inmueble inmueble) throws SQLException;

    /** Elimina un inmueble por su id. */
    boolean eliminar(int idInmueble) throws SQLException;
}
