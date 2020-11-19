package com.example.androidappgestionbasura.model.basura_municipal;

import com.example.androidappgestionbasura.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.Serializable;

/**
 * Diferentes tipos de basuras municipales
 * @author Ruben Pardo
 * Fecha: 16/11/2020
 */
public enum TipoBasuraMunicipal implements Serializable {


    ORGANICO ("organico",R.drawable.ic_icono_basura_organico,R.drawable.ic_icono_basura_organico_seleccionado),
    PLASTICO ("plastico", R.drawable.ic_icono_basura_plastico,R.drawable.ic_icono_basura_plastico_seleccionado),
    VIDRIO ("vidrio", R.drawable.ic_icono_basura_vidrio,R.drawable.ic_icono_basura_vidrio_seleccionado),
    PAPEL ("papel",R.drawable.ic_icono_basura_papel,R.drawable.ic_icono_basura_papel_seleccionado);

    private final String texto;
    private final int recurso;
    private final int recursoSeleccionado;
    TipoBasuraMunicipal(String texto, int recurso, int recursoSeleccionado) {
        this.texto = texto;
        this.recurso = recurso;
        this.recursoSeleccionado = recursoSeleccionado;
    }
    public String getTexto() { return texto; }
    public int getRecurso() { return recurso; }
    public int getRecursoSeleccionado() { return recursoSeleccionado; }



}
