package com.example.androidappgestionbasura.model.mesuras_dispositivos;


import java.util.ArrayList;
import java.util.List;

/**
 * 26/11/2020 Rubén Pardo
 */
public class ListaMesuras {

    List<Mesura> mesuras;

    public ListaMesuras(List<Mesura> mesuras) {
        this.mesuras = mesuras;
    }

    public ListaMesuras() {
        this.mesuras = new ArrayList<>();
    }

    public List<Mesura> getMesuras() {
        return mesuras;
    }

    public void setMesuras(List<Mesura> mesuras) {
        this.mesuras = mesuras;
    }

}
