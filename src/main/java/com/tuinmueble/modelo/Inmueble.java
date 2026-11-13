package com.tuinmueble.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa un inmueble publicado en la plataforma Tu Inmueble Suroeste.
 * Corresponde a la tabla "inmuebles" de la base de datos.
 *
 * @author Ometh Martinez Potes
 */
public class Inmueble {

    private int idInmueble;
    private int idUsuario;
    private String titulo;
    private String descripcion;
    private String tipo;
    private String operacion;
    private BigDecimal precio;
    private String direccion;
    private String municipio;
    private String estado;
    private LocalDateTime fechaPublicacion;

    public Inmueble() {
    }

    public Inmueble(int idUsuario, String titulo, String descripcion, String tipo,
            String operacion, BigDecimal precio, String direccion, String municipio,
            String estado) {
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.operacion = operacion;
        this.precio = precio;
        this.direccion = direccion;
        this.municipio = municipio;
        this.estado = estado;
    }

    public int getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    @Override
    public String toString() {
        return String.format("[%d] %-40s | %-11s | %-8s | $%,15.2f | %-22s | %s",
                idInmueble, titulo, tipo, operacion, precio, municipio, estado);
    }
}
