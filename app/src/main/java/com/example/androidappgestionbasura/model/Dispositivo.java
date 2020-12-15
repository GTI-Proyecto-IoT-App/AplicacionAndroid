package com.example.androidappgestionbasura.model;

import com.google.firebase.firestore.DocumentReference;
import java.util.ArrayList;

public class Dispositivo {
    private String nombre;
    private String descripcion;
    private String id;
    private long fecha;
    private TipoDispositivo tipo;
    private int numeroPersonasUso;
    private ArrayList<String> usuariosVinculados;

    public Dispositivo(String id, String nombre, String descripcion, TipoDispositivo tipo, int numeroPersonasUso) {
        this.id = id;
        fecha = System.currentTimeMillis();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.numeroPersonasUso = numeroPersonasUso;

    }
    public Dispositivo(String id, String nombre, String descripcion, TipoDispositivo tipo, int numeroPersonasUso, ArrayList<String> usuariosVinculados) {

        fecha = System.currentTimeMillis();
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.numeroPersonasUso = numeroPersonasUso;
        this.usuariosVinculados = usuariosVinculados;
    }

    public Dispositivo() {
        fecha = System.currentTimeMillis();
        tipo = TipoDispositivo.BASURA;
        usuariosVinculados = new ArrayList<>();
    }

    public ArrayList<String> getUsuariosVinculados() {
        return usuariosVinculados;
    }

    public void setUsuariosVinculados(ArrayList<String> usuariosVinculados) {
        this.usuariosVinculados = usuariosVinculados;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNumeroPersonasUso() { return numeroPersonasUso; }

    public void setNumeroPersonasUso(int numeroPersonasUso) { this.numeroPersonasUso = numeroPersonasUso; }



    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }






}