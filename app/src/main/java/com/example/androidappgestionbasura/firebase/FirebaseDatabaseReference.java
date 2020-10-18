package com.example.androidappgestionbasura.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * @author Ruben Pardo Casanova
 * contiene la referencia a nuestra base de datos.
 */
public class FirebaseDatabaseReference {

    private FirebaseDatabaseReference() {
    }
    public static final FirebaseFirestore DATABASE = FirebaseFirestore.getInstance();

}
