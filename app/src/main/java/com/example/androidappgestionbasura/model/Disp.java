package com.example.androidappgestionbasura.model;

import java.security.Key;

public class Disp{
    private String nombre;
    private String descripcion;
    private int key;
    private long fecha;
    private String foto;
    private TipoDispositivo tipo;
    private int numeroPersonasUso;

    public Disp(String nombre, String descripcion, int key, TipoDispositivo tipo, int numeroPersonasUso) {

        fecha = System.currentTimeMillis();

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.key = key;
        this.tipo = tipo;
        this.numeroPersonasUso = numeroPersonasUso;

    }

    public Disp() {
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

    public int getKey() { return key; }

    public void setKey(int key) {
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