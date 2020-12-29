package com.example.androidappgestionbasura.repository;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.notificaciones.Notificacion;
import com.google.firebase.firestore.Query;

public interface NotificacionRepository {

    Query readNotifiacionesDispositivosVinculadosByUID();
    void addNotificacion(Notificacion notification);
    void deleteNotificacion(Object idNotificacion);
    void deleteTodasNotificaciones(CallBack callBack);
}
