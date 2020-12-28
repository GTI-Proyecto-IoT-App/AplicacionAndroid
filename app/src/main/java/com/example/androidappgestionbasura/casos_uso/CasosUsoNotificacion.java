package com.example.androidappgestionbasura.casos_uso;

import android.app.Activity;

import com.example.androidappgestionbasura.model.notificaciones.Notificacion;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorNotificacionesFirestoreUI;
import com.example.androidappgestionbasura.repository.impl.NotificacionRepositoryImpl;
import com.example.androidappgestionbasura.utility.AppConf;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class CasosUsoNotificacion {

    private Activity actividad;
    private AdaptadorNotificacionesFirestoreUI adaptador;
    private final NotificacionRepositoryImpl notificacionRepository;// leer editar dispositivos

    public CasosUsoNotificacion(Activity activity){
        this.actividad = activity;
        String uid = ((AppConf) activity.getApplication()).getUsuario().getUid();
        notificacionRepository = new NotificacionRepositoryImpl(uid);
    }

    public FirestoreRecyclerOptions<Notificacion> getQueryNotificaciones(Activity activity) {

        Query query = notificacionRepository.readNotifiacionesDispositivosVinculadosByUID();

        return new FirestoreRecyclerOptions
                .Builder<Notificacion>().setQuery(query, Notificacion.class).build();
    }
}
