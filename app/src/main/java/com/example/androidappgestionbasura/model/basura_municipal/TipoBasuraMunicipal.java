package com.example.androidappgestionbasura.model.basura_municipal;

import com.example.androidappgestionbasura.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Diferentes tipos de basuras municipales
 * @author Ruben Pardo
 * Fecha: 16/11/2020
 */
public enum TipoBasuraMunicipal {


    ORGANICO ("organico",R.drawable.ic_icono_basura_organico),
    PLASTICO ("plastico", R.drawable.ic_icono_basura_plastico),
    VIDRIO ("vidrio", R.drawable.ic_icono_basura_vidrio),
    PAPEL ("papel",R.drawable.ic_icono_basura_papel);

    private final String texto;
    private final int recurso;
    TipoBasuraMunicipal(String texto, int recurso) {
        this.texto = texto;
        this.recurso = recurso;
    }
    public String getTexto() { return texto; }
    public int getRecurso() { return recurso; }



}
