package com.example.androidappgestionbasura.repository;

import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;

import java.util.HashMap;

public interface DispositivosRepository {
    void updateDispositivo(String dispositivoId, HashMap<String,Object> map,final CallBack callback);
    void readDispostivoByID(String dispositivoId, final CallBack callback);
}
