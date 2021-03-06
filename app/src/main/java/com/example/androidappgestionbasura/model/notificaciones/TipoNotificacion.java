package com.example.androidappgestionbasura.model.notificaciones;

import com.example.androidappgestionbasura.R;

public enum TipoNotificacion {

    DESCONECTADO ("DESCONECTADO",R.drawable.ic_smart_trash_no_conectado),
    CONECTADO("CONECTADO",R.drawable.ic_smart_trash_conectado );

    private final String texto;
    private final int recurso;
    TipoNotificacion(String texto, int recurso) {
        this.texto = texto;
        this.recurso = recurso;
    }
    public String getTexto() { return texto; }
    public int getRecurso() { return recurso; }
}
