package com.example.androidappgestionbasura.utility;

import android.app.Application;

import com.example.androidappgestionbasura.model.InterfaceDispositivos;
import com.example.androidappgestionbasura.model.ListaDispositivos;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivos;

public class AppConf extends Application {
    private AdaptadorDispositivos.RecyclerViewClickListener listener;

    private Usuario usuario;
    public InterfaceDispositivos interfaceDispositivos = new ListaDispositivos();
    @Override public void onCreate() {
        super.onCreate();
    }

    public AdaptadorDispositivos adaptador = new AdaptadorDispositivos(interfaceDispositivos, listener );


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
