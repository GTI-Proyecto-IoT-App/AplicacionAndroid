package com.example.androidappgestionbasura.repository;

import android.app.Notification;

import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.google.firebase.firestore.Query;

public interface NotificacionRepository {

    Query readNotifiacionesDispositivosVinculadosByUID();
    void addNotificacion(String idDispostivio, Notification notification, CallBack callBack);
    void deleteNotificacion(Notification notification, CallBack callBack);

}
