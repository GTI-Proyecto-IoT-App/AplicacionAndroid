package com.example.androidappgestionbasura.model.mesuras_dispositivos;

/**
 * 26/11/2020 Rubén Pardo
 * POJO Mesura basura
 */
public class Mesura {

    private String tipoMedida;//plastico|vidrio|organico|papel
    private long unixTime;
    private double llenado;
    private double peso;

    public Mesura(){}

    public Mesura(String tipoMedida, long fecha, double llenado, double peso) {
        this.tipoMedida = tipoMedida;
        this.unixTime = fecha;
        this.llenado = llenado;
        this.peso = peso;
    }


    public String getTipoMedida() {
        return tipoMedida;
    }

    public void setTipoMedida(String tipoMedida) {
        this.tipoMedida = tipoMedida;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    public double getLlenado() {
        return llenado;
    }

    public void setLlenado(double llenado) {
        this.llenado = llenado;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}
