package com.example.androidappgestionbasura.utility;

import android.app.Application;

import androidx.annotation.Nullable;

import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.InterfaceDispositivos;
import com.example.androidappgestionbasura.model.ListaDispositivos;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivos;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivosFirestoreUI;
import com.example.androidappgestionbasura.repository.impl.DispositivosRepositoryImpl;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
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
        Query query=dispositivos.getDispositvosVinculados(usuario.getUid());
        //TODO XXX revisar una manera mejor de detacar si esta vacio o no... Esto solo te detecta la primera vez :/
        //añadir listener para saber si esata vacio o no
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value!=null && !value.isEmpty()){
                    adaptador.setEmpty(false);
                }else{
                    adaptador.setEmpty(true);
                }
            }
        });
        FirestoreRecyclerOptions<Dispositivo> opciones = new FirestoreRecyclerOptions
                .Builder<Dispositivo>().setQuery(query, Dispositivo.class).build();
        adaptador = new AdaptadorDispositivosFirestoreUI(opciones, this);

    }
}
