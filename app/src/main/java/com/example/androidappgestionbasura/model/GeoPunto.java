package com.example.androidappgestionbasura.model;

/**
 * Almacena un punto geolocalizado
 * @author Ruben Pardo
 * Fecha: 16/11/2020
 */
public class GeoPunto {

    private double lat;
    private double lng;

    public GeoPunto(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
    public GeoPunto() {

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
