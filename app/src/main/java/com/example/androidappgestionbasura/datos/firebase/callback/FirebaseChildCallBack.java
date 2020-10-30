package com.example.androidappgestionbasura.datos.firebase.callback;

/**
 * @author Ruben Pardo Casanova
 * to handle child event listeners.
 */
public abstract class FirebaseChildCallBack {

    public abstract void onChildAdded(Object object);

    public abstract void onChildChanged(Object object);

    public abstract void onChildRemoved(Object object);

    public abstract void onCancelled(Object object);

}
