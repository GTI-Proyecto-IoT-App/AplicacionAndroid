package com.example.androidappgestionbasura.model;

import com.example.androidappgestionbasura.R;

public enum TipoDispositivo {

    BASURA ("Basura", R.drawable.ic_smart_trash),
    ELECTRICO ("Consumo eléctrico", R.drawable.ic_control_de_energia),
    AGUA ("Agua", R.drawable.ic_control_de_agua);

    private final String texto;
    private final int recurso;
    TipoDispositivo(String texto, int recurso) {
        this.texto = texto;
        this.recurso = recurso;
    }
    public String getTexto() { return texto; }
    public int getRecurso() { return recurso; }

}