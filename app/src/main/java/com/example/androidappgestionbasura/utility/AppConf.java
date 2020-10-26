package com.example.androidappgestionbasura.utility;

import android.app.Application;

import com.example.androidappgestionbasura.model.Dispositivos;
import com.example.androidappgestionbasura.model.DispositivosLista;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivos;

public class AppConf extends Application {

    private Usuario usuario;
    public Dispositivos dispositivos = new DispositivosLista();
    @Override public void onCreate() {
        super.onCreate();
    }

    public AdaptadorDispositivos adaptador = new AdaptadorDispositivos(dispositivos);


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
