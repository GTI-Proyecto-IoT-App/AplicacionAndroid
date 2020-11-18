package com.example.androidappgestionbasura.model.basura_municipal;

import com.example.androidappgestionbasura.model.GeoPunto;

import java.util.ArrayList;
import java.util.List;

public class ListaBasurasMunicipales {


    List<BasuraMunicipal> basuraMunicipalList;

    public ListaBasurasMunicipales(List<BasuraMunicipal> basuraMunicipalList) {
        this.basuraMunicipalList = basuraMunicipalList;
    }

    public ListaBasurasMunicipales() {
        this.basuraMunicipalList = new ArrayList<>();
    }

    public List<BasuraMunicipal> getBasuraMunicipalList() {
        return basuraMunicipalList;
    }

    public void setBasuraMunicipalList(List<BasuraMunicipal> basuraMunicipalList) {
        this.basuraMunicipalList = basuraMunicipalList;
    }
}
