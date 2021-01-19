package com.example.androidappgestionbasura.model.notificaciones;

import com.example.androidappgestionbasura.model.mesuras_dispositivos.Mesura;

import java.util.ArrayList;
import java.util.List;

public class ListaNotifiaciones {
    List<Notificacion> notificacions;

    public ListaNotifiaciones(List<Notificacion> mesuras) {
        this.notificacions = mesuras;
    }

    public ListaNotifiaciones() {
        this.notificacions = new ArrayList<>();
    }

    public List<Notificacion> getNotificacions() {
        return notificacions;
    }

    public void setNotificacions(List<Notificacion> notificacions) {
        this.notificacions = notificacions;
    }
}
