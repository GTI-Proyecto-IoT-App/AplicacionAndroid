package com.example.androidappgestionbasura.repository;

import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;

public interface MesurasRepository {

    void readMesurasAnualesByUID(String uid, final CallBack callBack);
    void readBolsasBasurasMensualesByUID(String uid, final CallBack callBack);
    void readMesurasByID(String id, final CallBack callBack) ;

}
