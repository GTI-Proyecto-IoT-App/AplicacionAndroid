package com.example.androidappgestionbasura.casos_uso;

import android.app.Activity;
import android.content.Intent;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.model.Dispositivos;
import com.example.androidappgestionbasura.model.TipoDispositivo;
import com.example.androidappgestionbasura.presentacion.DispositivoDetallesActivity;

public class CasosUsoDispositivo {
    private Activity actividad;
    private Dispositivos dispositivos;
    public CasosUsoDispositivo(Activity actividad, Dispositivos dispositivos) {
        this.actividad = actividad;
        this.dispositivos = dispositivos;

    }
    // OPERACIONES BÁSICAS
    public void mostrar(int pos) {
        Intent i = new Intent(actividad, DispositivoDetallesActivity.class);
        i.putExtra("pos", pos);
        actividad.startActivity(i);
    }
    public void crear(TipoDispositivo tipo){
        switch(tipo) {
            case BASURA:

                break;
            case ELECTRICO:  break;
            case AGUA:  break;
        }
    }
}
