package com.example.androidappgestionbasura.utility;

import android.app.Application;

import androidx.annotation.Nullable;

import com.example.androidappgestionbasura.datos.preferences.SharedPreferencesHelper;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivosFirestoreUI;
import com.example.androidappgestionbasura.repository.impl.DispositivosRepositoryImpl;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class AppConf extends Application {

    private Usuario usuario;
    public DispositivosRepositoryImpl dispositivos;
    public AdaptadorDispositivosFirestoreUI adaptador;
    @Override public void onCreate() {
        super.onCreate();
        dispositivos= new DispositivosRepositoryImpl();
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        initAdaptador();
    }
    private void initAdaptador(){
        if(usuario != null){
            String uid = SharedPreferencesHelper.getInstance().getUID();
            Query query=dispositivos.getDispositvosVinculados(uid);

            FirestoreRecyclerOptions<Dispositivo> opciones = new FirestoreRecyclerOptions
                    .Builder<Dispositivo>().setQuery(query, Dispositivo.class).build();
            adaptador = new AdaptadorDispositivosFirestoreUI(opciones, this);
        }


    }
}
