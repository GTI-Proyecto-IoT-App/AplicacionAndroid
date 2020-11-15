package com.example.androidappgestionbasura.repository.impl;

import com.example.androidappgestionbasura.datos.firebase.FirebaseReferences;
import com.example.androidappgestionbasura.datos.firebase.FirebaseRepository;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.repository.DispositivosRepository;
import com.example.androidappgestionbasura.utility.Utility;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.androidappgestionbasura.datos.firebase.constants.Constant.FAIL;
import static com.example.androidappgestionbasura.datos.firebase.constants.Constant.SUCCESS;
import static com.example.androidappgestionbasura.datos.firebase.constants.FirebaseConstants.TABLA_DISPOSITIVOS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DispositivosRepositoryImpl extends FirebaseRepository implements DispositivosRepository {
    private CollectionReference dispositivosCollectionReference;

    public DispositivosRepositoryImpl(){
        dispositivosCollectionReference = FirebaseReferences.getInstancia().getDATABASE().collection(TABLA_DISPOSITIVOS);
    }


    @Override
    public void updateDispositivo(String dispositivoId, HashMap<String, Object> map, final CallBack callback) {
        DocumentReference documentReference = dispositivosCollectionReference.document(dispositivoId);
        fireStoreUpdate(documentReference, map, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callback.onSuccess(SUCCESS);
            }
            @Override
            public void onError(Object object) {
                callback.onError(object);
            }
        });


    }
    public Query getDispositvosVinculados(String uidUsuario){
        return dispositivosCollectionReference.whereArrayContains("usuariosVinculados",uidUsuario);
    }



    @Override
    public void readDispostivoByID(String dispositivoId, final CallBack callback) {
        if(!Utility.isEmptyOrNull(dispositivoId)){
            // cogemos el dipositivo por id
            Query documentReference = dispositivosCollectionReference.whereEqualTo("id",dispositivoId);

            readQueryDocuments(documentReference, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    if (object != null) {
                        // creamos un dispositvo y lo enviamos por el callback
                        Dispositivo dispositivo = getDataFromQuerySnapshot(object);
                        callback.onSuccess(dispositivo);

                    } else{
                        // si no existe devolvemos null
                        callback.onSuccess(null);
                    }
                }

                @Override
                public void onError(Object object) {
                    callback.onError(object);
                }
            });


        }else {
            callback.onError(FAIL);
        }
    }
    public Dispositivo getDataFromQuerySnapshot(Object object) {
        List<Dispositivo> dispositivos = new ArrayList<>();
        QuerySnapshot queryDocumentSnapshots = (QuerySnapshot) object;
        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
            Dispositivo dispositivo = snapshot.toObject(Dispositivo.class);
            dispositivos.add(dispositivo);
        }
        return dispositivos.get(0);
    }

}
