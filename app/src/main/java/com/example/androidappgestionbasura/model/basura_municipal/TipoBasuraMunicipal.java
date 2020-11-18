package com.example.androidappgestionbasura.model.basura_municipal;

import com.example.androidappgestionbasura.R;
/**
 * Diferentes tipos de basuras municipales
 * @author Ruben Pardo
 * Fecha: 16/11/2020
 */
public enum TipoBasuraMunicipal {


    ORGANICO ("Basura", R.drawable.ic_smart_trash),
    PLASTICO ("Consumo eléctrico", R.drawable.ic_control_de_energia),
    VIDRIO ("Agua", R.drawable.ic_control_de_agua),
    PAPEL ("Agua", R.drawable.ic_control_de_agua);

    private final String texto;
    private final int recurso;
    TipoBasuraMunicipal(String texto, int recurso) {
        this.texto = texto;
        this.recurso = recurso;
    }
    public String getTexto() { return texto; }
    public int getRecurso() { return recurso; }

}
