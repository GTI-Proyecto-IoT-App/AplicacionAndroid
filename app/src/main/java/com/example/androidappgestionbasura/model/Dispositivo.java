package com.example.androidappgestionbasura.model;

import java.security.Key;

public class Dispositivo {
    private String nombre;
    private String descripcion;
    private String key;
    private long fecha;
    private String foto;
    private TipoDispositivo tipo;
    private int numeroPersonasUso;

    public Dispositivo(String key, String nombre, String descripcion, TipoDispositivo tipo, int numeroPersonasUso) {

        fecha = System.currentTimeMillis();

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.key = key;
        this.tipo = tipo;
        this.numeroPersonasUso = numeroPersonasUso;

    }

    public Dispositivo() {
        fecha = System.currentTimeMillis();
        tipo = TipoDispositivo.BASURA;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoDispositivo getTipo() {
        return tipo;
    }

    public void setTipo(TipoDispositivo tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getKey() { return key; }

    public void setKey(String key) {
        this.key = key;
    }

    public int getNumeroPersonasUso() { return numeroPersonasUso; }

    public void setNumeroPersonasUso(int numeroPersonasUso) { this.numeroPersonasUso = numeroPersonasUso; }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }







}