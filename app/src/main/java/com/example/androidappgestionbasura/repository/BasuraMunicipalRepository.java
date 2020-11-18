package com.example.androidappgestionbasura.repository;

import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;

public interface BasuraMunicipalRepository {
    void readBasurasMunicipales(final CallBack callback);
}
