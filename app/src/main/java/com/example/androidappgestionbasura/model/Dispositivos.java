package com.example.androidappgestionbasura.model;

import com.example.androidappgestionbasura.model.Disp;

public interface Dispositivos {

        Disp elemento(int id); //Devuelve el elemento dado su id
        void añade(Disp dispositivo); //Añade el elemento indicado
        int nuevo(); //Añade un elemento en blanco y devuelve su id
        void borrar(int id); //Elimina el elemento con el id indicado
        int tamaño(); //Devuelve el número de elementos
        void actualiza(int id, Disp dispositivo); //Reemplaza un elemento
    }

