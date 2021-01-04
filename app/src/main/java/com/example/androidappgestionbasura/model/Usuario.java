package com.example.androidappgestionbasura.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Ruben Pardo Casanova
 */
public class Usuario implements Serializable {

    private String name;
    private boolean isEmailVerified;
    private String email;
    private String uid;
    private String key;

    private double consumoElectrico;
    private double consumoDeAgua;
    private double consumoCO2;


    public Usuario(String email,String uid, boolean isEmailVerified){
        this.email = email;
        this.uid = uid;
        this.isEmailVerified = isEmailVerified;
    }

    public Usuario(String name, String uid,String email, boolean isEmailVerified) {
        this.name = name;
        this.uid = uid;
        this.email = email;
        this.uid = uid;
        this.isEmailVerified = isEmailVerified;
    }

    public Usuario(String name, boolean isEmailVerified, String email, String uid, String key, double consumoElectrico, double consumoDeAgua) {
        this.name = name;
        this.isEmailVerified = isEmailVerified;
        this.email = email;
        this.uid = uid;
        this.key = key;
        this.consumoElectrico = consumoElectrico;
        this.consumoDeAgua = consumoDeAgua;
    }

    public Usuario(String name, boolean isEmailVerified, String email, String uid, String key, double consumoElectrico, double consumoDeAgua, double consumoCO2) {
        this.name = name;
        this.isEmailVerified = isEmailVerified;
        this.email = email;
        this.uid = uid;
        this.key = key;
        this.consumoElectrico = consumoElectrico;
        this.consumoDeAgua = consumoDeAgua;
        this.consumoCO2 = consumoCO2;
    }

    public Usuario() { }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getConsumoElectrico() {
        return consumoElectrico;
    }

    public void setConsumoElectrico(double consumoElectrico) {
        this.consumoElectrico = consumoElectrico;
    }

    public double getConsumoDeAgua() {
        return consumoDeAgua;
    }

    public void setConsumoDeAgua(double consumoDeAgua) {
        this.consumoDeAgua = consumoDeAgua;
    }

    public double getConsumoCO2() {
        return consumoCO2;
    }

    public void setConsumoCO2(double consumoCO2) {
        this.consumoCO2 = consumoCO2;
    }

    /**
     * @author Ruben Pardo
     * @return hashmap para la gestion de update de firestore
     */
    @Exclude
    public HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", getEmail());
        map.put("emailVerified", isEmailVerified());
        map.put("name", getName());
        map.put("uid", getUid());
        map.put("consumoDeAgua",getConsumoDeAgua());
        map.put("consumoElectrico",getConsumoElectrico());
        return map;
    }

}
