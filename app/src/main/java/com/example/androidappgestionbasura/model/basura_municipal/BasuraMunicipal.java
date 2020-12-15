package com.example.androidappgestionbasura.model.basura_municipal;

import com.example.androidappgestionbasura.model.GeoPunto;

import java.io.Serializable;

/**
 * Objeto que representa una basura municipal
 * @author Ruben Pardo
 * Fecha: 16/11/2020
 */
public class BasuraMunicipal implements Serializable {

    private GeoPunto posicion;
    private TipoBasuraMunicipal tipo;


    public BasuraMunicipal(){

    }

    public BasuraMunicipal(GeoPunto posicion, TipoBasuraMunicipal tipoBasuraMunicipal) {
        this.posicion = posicion;
        this.tipo = tipoBasuraMunicipal;
    }

    public TipoBasuraMunicipal getTipo() {
        return tipo;
    }

    public void setTipo(TipoBasuraMunicipal tipo) {
        this.tipo = tipo;
    }

    public GeoPunto getPosicion() {
        return posicion;
    }

    public void setPosicion(GeoPunto posicion) {
        this.posicion = posicion;
    }

}
