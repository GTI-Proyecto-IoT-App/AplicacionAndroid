package com.example.androidappgestionbasura.model.bolsas_basura;

import java.util.ArrayList;
import java.util.List;

public class ListaBolsaBasura {

    List<BolsaBasura> bolsasBasuraList;
    private double kgC02Generados;
    public static final int KG_REDUCIDOS_POR_UN_ARBOL_AL_ANO = 30;

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


    // el organico suma y los demas restan
    public double getKgC02Generados(){
        kgC02Generados = 0;

        for (BolsaBasura bolsaBasura : bolsasBasuraList){
            if(bolsaBasura.getTipo().equals("organico")){
                kgC02Generados+= bolsaBasura.getKgCo2();
            }else{
                kgC02Generados-= bolsaBasura.getKgCo2();
            }
        }


        return (double)Math.round(kgC02Generados*100)/100;
    }

    // Un arbol absorbe 30 Kg C02 al año
    public int getArbolesPlantados(){

        int arboles;
        if(kgC02Generados<0){
            arboles = (int) kgC02Generados/KG_REDUCIDOS_POR_UN_ARBOL_AL_ANO;
        }else{
            arboles = 0;
        }

        return arboles;
    }

}
