package com.example.androidappgestionbasura.datos.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * @author Ruben Pardo Casanova
 * contiene la referencia a nuestra base de datos.
 */
public class FirebaseReferences {

    private static FirebaseReferences INSTANCIA = new FirebaseReferences();

    private final FirebaseFirestore DATABASE = FirebaseFirestore.getInstance();
    private final FirebaseAuth FIREBASE_AUTH = FirebaseAuth.getInstance();

    public static FirebaseReferences getInstancia() {
        return INSTANCIA;
    }


    private FirebaseReferences() {
    }

    public FirebaseFirestore getDATABASE() {
        return DATABASE;
    }

    public FirebaseAuth getFIREBASE_AUTH() {
        return FIREBASE_AUTH;
    }
}
