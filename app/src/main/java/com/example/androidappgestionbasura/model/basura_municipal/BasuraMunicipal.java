package com.example.androidappgestionbasura.model.basura_municipal;

import com.example.androidappgestionbasura.model.GeoPunto;

/**
 * Objeto que representa una basura municipal
 * @author Ruben Pardo
 * Fecha: 16/11/2020
 */
public class BasuraMunicipal {

    private GeoPunto posicion;
    private TipoBasuraMunicipal tipoBasuraMunicipal;


    public BasuraMunicipal(){

    }

    public BasuraMunicipal(GeoPunto posicion, TipoBasuraMunicipal tipoBasuraMunicipal) {
        this.posicion = posicion;
        this.tipoBasuraMunicipal = tipoBasuraMunicipal;
    }

    public TipoBasuraMunicipal getTipoBasuraMunicipal() {
        return tipoBasuraMunicipal;
    }

    public void setTipoBasuraMunicipal(TipoBasuraMunicipal tipoBasuraMunicipal) {
        this.tipoBasuraMunicipal = tipoBasuraMunicipal;
    }

    public GeoPunto getPosicion() {
        return posicion;
    }

    public void setPosicion(GeoPunto posicion) {
        this.posicion = posicion;
    }
}
