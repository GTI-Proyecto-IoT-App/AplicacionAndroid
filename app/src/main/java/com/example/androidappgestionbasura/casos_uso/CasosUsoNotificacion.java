package com.example.androidappgestionbasura.casos_uso;

import android.app.Activity;
import android.util.Log;

import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.notificaciones.Notificacion;
import com.example.androidappgestionbasura.presentacion.LoadingDialogActivity;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorNotificacionesFirestoreUI;
import com.example.androidappgestionbasura.repository.impl.NotificacionRepositoryImpl;
import com.example.androidappgestionbasura.utility.AppConf;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class CasosUsoNotificacion {

    private LoadingDialogActivity dialogActivity;
    private final NotificacionRepositoryImpl notificacionRepository;// leer editar dispositivos

    public CasosUsoNotificacion(String uid,Activity activity){
        dialogActivity = new LoadingDialogActivity(activity);
        notificacionRepository = new NotificacionRepositoryImpl(uid);
    }

    public FirestoreRecyclerOptions<Notificacion> getQueryNotificaciones() {

        Query query = notificacionRepository.readNotifiacionesDispositivosVinculadosByUID();

        return new FirestoreRecyclerOptions
                .Builder<Notificacion>().setQuery(query, Notificacion.class).build();
    }


    public void enviarNotificacionFirestore(Notificacion notificacion){


        notificacionRepository.addNotificacion(notificacion);

    }


    public void borrarNotificacion(Object idNotificacion) {
        notificacionRepository.deleteNotificacion(idNotificacion);
    }

    public void borrarTodasNotificaciones() {
        dialogActivity.startLoadingDialog();
        notificacionRepository.deleteTodasNotificaciones(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                dialogActivity.dismissDialog();
            }

            @Override
            public void onError(Object object) {
                dialogActivity.dismissDialog();
            }
        });
    }
}
