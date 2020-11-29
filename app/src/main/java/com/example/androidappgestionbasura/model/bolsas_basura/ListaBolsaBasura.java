package com.example.androidappgestionbasura.model.bolsas_basura;

import java.util.ArrayList;
import java.util.List;

public class ListaBolsaBasura {

    List<BolsaBasura> bolsasBasuraList;

    public ListaBolsaBasura(List<BolsaBasura> basuraMunicipalList) {
        this.bolsasBasuraList = basuraMunicipalList;
    }

    public ListaBolsaBasura() {
        this.bolsasBasuraList = new ArrayList<>();
    }

    public List<BolsaBasura> getBolsasBasuraList() {
        return bolsasBasuraList;
    }

    public void setBolsasBasuraList(List<BolsaBasura> bolsasBasuraList) {
        this.bolsasBasuraList = bolsasBasuraList;
    }

}
