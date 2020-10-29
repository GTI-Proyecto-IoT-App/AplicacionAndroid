package com.example.androidappgestionbasura.utility;

import android.app.Application;

import com.example.androidappgestionbasura.model.InterfaceDispositivos;
import com.example.androidappgestionbasura.model.ListaDispositivos;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivos;

public class AppConf extends Application {

    private Usuario usuario;
    public InterfaceDispositivos listaDispositivos = new ListaDispositivos();
    @Override public void onCreate() {
        super.onCreate();
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
