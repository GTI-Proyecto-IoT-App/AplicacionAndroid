package com.example.androidappgestionbasura.model;

import java.util.ArrayList;
import java.util.List;

public class ListaDispositivos implements InterfaceDispositivos {
    protected List<Dispositivo> listaDispositivos;
    public ListaDispositivos() {
        listaDispositivos = new ArrayList<Dispositivo>();
        añadeEjemplos();
    }
    public Dispositivo elemento(int id) {
        return listaDispositivos.get(id);
    }
    public void añade(Dispositivo dispositivo) {
        listaDispositivos.add(dispositivo);
    }
    public int nuevo() {
        Dispositivo dispositivo = new Dispositivo();
        listaDispositivos.add(dispositivo);
        return listaDispositivos.size()-1;
    }
    public void borrar(int id) {
        listaDispositivos.remove(id);
    }
    public int tamaño() {
        return listaDispositivos.size();
    }
    public void actualiza(int id, Dispositivo dispositivo) {
        listaDispositivos.set(id, dispositivo);
    }
    public void añadeEjemplos() {
        añade(new Dispositivo("","Sensor Basura",
                "Ubicado en la cocina",
                TipoDispositivo.BASURA, 2 ));
        añade(new Dispositivo("","Sensor comsumo eléctrico",
                "Bombillas led del comedor",  TipoDispositivo.ELECTRICO, 4));
        añade(new Dispositivo("","Sensor Agua",
                "En el grifo del cuarto de baño", TipoDispositivo.AGUA, 3
                ));

    }
}
