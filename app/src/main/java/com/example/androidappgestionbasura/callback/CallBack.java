package com.example.androidappgestionbasura.callback;

/**
 * Autor Ruben Pardo Casanova
 * Fecha 18/10/2020
 *
 * Descripción: Los listeneres de Firebase Cloud Firestore son subprocesos asincrónicos,
 * por lo que debemos crear algunas clases de callback de llamada para manejar las devoluciones
 * de llamada del oyente de eventos.
 *
 */
public abstract class CallBack {

    public abstract void  onSuccess(Object object);
    public abstract void  onError(Object object);

}
