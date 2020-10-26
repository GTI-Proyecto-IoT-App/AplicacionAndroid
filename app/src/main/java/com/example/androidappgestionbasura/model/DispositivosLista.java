package com.example.androidappgestionbasura.model;

import java.util.ArrayList;
import java.util.List;

public class DispositivosLista implements Dispositivos {
    protected List<Disp> listaDispositivos;
    public DispositivosLista() {
        listaDispositivos = new ArrayList<Disp>();
        añadeEjemplos();
    }
    public Disp elemento(int id) {
        return listaDispositivos.get(id);
    }
    public void añade(Disp disp) {
        listaDispositivos.add(disp);
    }
    public int nuevo() {
        Disp disp = new Disp();
        listaDispositivos.add(disp);
        return listaDispositivos.size()-1;
    }
    public void borrar(int id) {
        listaDispositivos.remove(id);
    }
    public int tamaño() {
        return listaDispositivos.size();
    }
    public void actualiza(int id, Disp disp) {
        listaDispositivos.set(id, disp);
    }
    public void añadeEjemplos() {
        añade(new Disp("Sensor Basura",
                "Ubicado en la cocina", 4324234,
                TipoDispositivo.BASURA ));
        añade(new Disp("Sensor comsumo eléctrico",
                "Bombillas led del comedor",
                743242342,  TipoDispositivo.ELECTRICO));
        añade(new Disp("Sensor Agua",
                "En el grifo del cuarto de baño", 453453, TipoDispositivo.AGUA
                ));

    }
}
