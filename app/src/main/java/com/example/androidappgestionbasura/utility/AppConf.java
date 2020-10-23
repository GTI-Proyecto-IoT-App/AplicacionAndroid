package com.example.androidappgestionbasura.utility;

import android.app.Application;
import android.os.SystemClock;

import com.example.androidappgestionbasura.model.Usuario;

public class AppConf extends Application {

    private Usuario usuario;


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
