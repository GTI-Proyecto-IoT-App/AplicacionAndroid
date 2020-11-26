package com.example.androidappgestionbasura.repository.impl;

import com.example.androidappgestionbasura.datos.firebase.FirebaseReferences;
import com.example.androidappgestionbasura.datos.firebase.FirebaseRepository;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.repository.MesurasRepository;
import com.google.firebase.firestore.CollectionReference;

import static com.example.androidappgestionbasura.datos.firebase.constants.FirebaseConstants.TABLA_DISPOSITIVOS;


/**
 * 26/11/2020 Rubén Pardo
 * Clase que gestiona los mesuras de los dispositivos y un usuario
 */
public class MesurasRepositorioImpl extends FirebaseRepository implements MesurasRepository  {

    private CollectionReference mesurasCollectionReference;

    public MesurasRepositorioImpl(){
        mesurasCollectionReference = FirebaseReferences.getInstancia().getDATABASE().collection(TABLA_DISPOSITIVOS);
    }

    /**
     * Obtiene los datos de un año de las basuras de un usuario
     * @param uid del usuario
     * @param callBack return List<Mesuras> || error
     */
    @Override
    public void readMesurasAnualesByUID(String uid, CallBack callBack) {

    }
}
