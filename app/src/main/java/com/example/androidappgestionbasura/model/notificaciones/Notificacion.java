package com.example.androidappgestionbasura.model.notificaciones;


public class Notificacion {

    private String idDispositivo, nombreDispositivo,id;
    private long fecha;
    private TipoNotificacion tipo;

    public Notificacion(String dispositivo, String nombreDispositivo, TipoNotificacion tipo, String id) {
        this.nombreDispositivo = nombreDispositivo;
        this.id = id;
        fecha = System.currentTimeMillis();
        this.idDispositivo = dispositivo;
        this.tipo = tipo;

    }

    public Notificacion() {
    }


    public String getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(String idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }


    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreDispositivo() {
        return nombreDispositivo;
    }

    public void setNombreDispositivo(String nombreDispositivo) {
        this.nombreDispositivo = nombreDispositivo;
    }
}
