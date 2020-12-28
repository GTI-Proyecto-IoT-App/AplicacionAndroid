package com.example.androidappgestionbasura.repository.impl;

import android.app.Notification;
import android.util.Log;

import com.example.androidappgestionbasura.datos.firebase.FirebaseReferences;
import com.example.androidappgestionbasura.datos.firebase.FirebaseRepository;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.datos.firebase.constants.FirebaseConstants;
import com.example.androidappgestionbasura.repository.NotificacionRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;

import static com.example.androidappgestionbasura.datos.firebase.constants.FirebaseConstants.TABLA_USUARIOS;

public class NotificacionRepositoryImpl extends FirebaseRepository implements NotificacionRepository {


    private CollectionReference notificacionCollectionReferencia;

    public NotificacionRepositoryImpl(String uid){

        notificacionCollectionReferencia = FirebaseReferences.getInstancia()
                .getDATABASE().collection(TABLA_USUARIOS)
                .document(uid).collection(FirebaseConstants.TABLA_NOTIFICACION);

        Log.d("NOTIFICACIONES",notificacionCollectionReferencia.getPath());
    }


    @Override
    public Query readNotifiacionesDispositivosVinculadosByUID() {
        return notificacionCollectionReferencia;
    }

    @Override
    public void addNotificacion(String idDispostivio, Notification notification, CallBack callBack) {

    }

    @Override
    public void deleteNotificacion(Notification notification, CallBack callBack) {

    }
}
