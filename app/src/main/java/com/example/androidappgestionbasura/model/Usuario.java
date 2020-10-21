package com.example.androidappgestionbasura.model;

import java.io.Serializable;

/**
 * @author Ruben Pardo Casanova
 */
public class Usuario implements Serializable {

    private String name;
    private String uid;
    private String key;

    public Usuario(String name, String uid) {
        this.name = name;
        this.uid = uid;
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
}
