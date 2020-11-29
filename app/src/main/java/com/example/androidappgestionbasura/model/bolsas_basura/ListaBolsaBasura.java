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


    // el organico suma y los demas restan
    public double getKgC02Generados(){
        double kgC02 = 0;

        for (BolsaBasura bolsaBasura : bolsasBasuraList){
            if(bolsaBasura.getTipo().equals("organico")){
                kgC02+= bolsaBasura.getKgCo2();
            }else{
                kgC02-= bolsaBasura.getKgCo2();
            }
        }


        return (double)Math.round(kgC02*100)/100;
    }

}
