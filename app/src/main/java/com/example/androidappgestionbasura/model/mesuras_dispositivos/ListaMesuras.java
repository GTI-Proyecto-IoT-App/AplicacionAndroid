package com.example.androidappgestionbasura.model.mesuras_dispositivos;


import android.util.Log;

import com.example.androidappgestionbasura.model.bolsas_basura.BolsaBasura;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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


    /**
     * se ordena por nombre para evitar problemas
     * comprueba cuando la basura baja de llenado
     * @return lista de bolsas de basura
     */
    public List<BolsaBasura> getBolsasBasura(){

        List<BolsaBasura> bolsaBasuras = new ArrayList<>();

        Collections.sort(mesuras, new Comparator<Mesura>() {
            @Override
            public int compare(Mesura o1, Mesura o2) {
                return o1.getTipoMedida().compareTo(o2.getTipoMedida());
            }
        });

        // ver cuando el llenado baja a 0
        // al estar ordenado por tipo hay que hacer la comprobacion cada
        // vez que cambie de tipo o baje el llenado drasticamente
        Log.d("hola","entra");
        if(!mesuras.isEmpty()){
            String lastTipo = mesuras.get(0).getTipoMedida();
            double lastLlenado = mesuras.get(0).getLlenado();
            Log.d("hola","entra2: " +mesuras.size());

            for(Mesura mesura : mesuras){
                Log.d("hola",mesura.getTipoMedida()+"");

                if(lastTipo.equals(mesura.getTipoMedida())){

                    // mismo tipo
                    // si el llenado nuevo es 0 y la diferncia es negativa (mas de -10) es que se ha quitado la basura
                    double diferenciaLlenado = mesura.getLlenado() - lastLlenado;
                    if(mesura.getLlenado() == 0 && diferenciaLlenado<-10){

                        Log.d("hola","añade bolsa de basura");

                        // se coge el ultimo llenado y la ultima fecha
                        bolsaBasuras.add(new BolsaBasura(lastLlenado,mesura.getUnixTime(),lastTipo));
                    }

                }
                lastTipo = mesura.getTipoMedida();
                lastLlenado = mesura.getLlenado();
            }


        }

        return bolsaBasuras;
    }

}
