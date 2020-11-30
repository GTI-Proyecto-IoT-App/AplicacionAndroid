package com.example.androidappgestionbasura.model.bolsas_basura;

public class BolsaBasura {

    private static final double DENSIDAD_PLASTICO_KG_CM3 = 0.000052;
    private static final double DENSIDAD_ORGANICO_KG_CM3 = 0.000256;
    private static final double DENSIDAD_VIDRIO_KG_CM3 = 0.000260;
    private static final double DENSIDAD_PAPEL_KG_CM3 = 0.000050;
    private static final double VOLUMEN_CONTENEDOR_CM3 = 4500;//30cmx15cmx10cm

    private static final double RELACION_PESO_KG_CO2_ORGANICO = 4.2; // el organico no reduce el c02
    private static final double RELACION_PESO_KG_CO2_PLASTICO = 1.8; // por cada kg de plastico se genera
                                                                    // 3.7 kg de c02 pero si es reciclado se baja a 1.7
    private static final double RELACION_PESO_KG_CO2_VIDRIO = 0.3; // por cada kilo de vidrio se reduce 300 gramos de c02
    private static final double RELACION_PESO_KG_CO2_PAPEL = 0.89; // por cada kilo de papel se reduce 890 gramos de c02

    private double llenado;
    private long fecha;
    private String tipo;
    private double peso;

    private double kgCo2;// kg de co2 equivalente a su peso. El organico genera y los demas reducen


    public BolsaBasura(double llenado, long fecha,String tipo) {
        this.llenado = llenado;
        this.fecha = fecha;
        this.tipo = tipo;

        switch (tipo){
            case "vidrio":
                this.peso = DENSIDAD_VIDRIO_KG_CM3 * VOLUMEN_CONTENEDOR_CM3 * (llenado/100);
                this.kgCo2 = peso * RELACION_PESO_KG_CO2_VIDRIO;
                break;
            case "papel":
                this.peso = DENSIDAD_PAPEL_KG_CM3 * VOLUMEN_CONTENEDOR_CM3 * (llenado/100);
                this.kgCo2 = peso * RELACION_PESO_KG_CO2_PAPEL;
                break;
            case "plastico":
                this.peso = DENSIDAD_PLASTICO_KG_CM3 * VOLUMEN_CONTENEDOR_CM3 * (llenado/100);
                this.kgCo2 = peso * RELACION_PESO_KG_CO2_PLASTICO;
                break;
            case "organico":
                this.peso = DENSIDAD_ORGANICO_KG_CM3 * VOLUMEN_CONTENEDOR_CM3 * (llenado/100);
                this.kgCo2 = peso * RELACION_PESO_KG_CO2_ORGANICO;
                break;
        }



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

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getKgCo2() {
        return kgCo2;
    }

    public void setKgCo2(double kgCo2) {
        this.kgCo2 = kgCo2;
    }
}
