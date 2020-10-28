package com.example.androidappgestionbasura.model;

import com.example.androidappgestionbasura.R;

public enum TipoDispositivo {

    BASURA ("Basura", R.drawable.basura),
    ELECTRICO ("Consumo eléctrico", R.drawable.electrico),
    AGUA ("Agua", R.drawable.agua);

    private final String texto;
    private final int recurso;
    TipoDispositivo(String texto, int recurso) {
        this.texto = texto;
        this.recurso = recurso;
    }
    public String getTexto() { return texto; }
    public int getRecurso() { return recurso; }

}