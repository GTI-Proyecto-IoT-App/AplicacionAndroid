package com.example.androidappgestionbasura.model.bolsas_basura;

public class BolsaBasura {

    double llenado;
    long fecha;
    String tipo;

    public BolsaBasura(double llenado, long fecha,String tipo) {
        this.llenado = llenado;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getLlenado() {
        return llenado;
    }

    public void setLlenado(double llenado) {
        this.llenado = llenado;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }
}
