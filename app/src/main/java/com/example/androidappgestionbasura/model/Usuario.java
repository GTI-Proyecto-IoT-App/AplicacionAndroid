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
        return map;
    }

}
