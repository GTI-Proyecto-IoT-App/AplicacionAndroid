package com.example.androidappgestionbasura.repository.impl;

import android.util.Log;

import com.example.androidappgestionbasura.datos.firebase.FirebaseReferences;
import com.example.androidappgestionbasura.datos.firebase.FirebaseRepository;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.basura_municipal.BasuraMunicipal;
import com.example.androidappgestionbasura.model.basura_municipal.ListaBasurasMunicipales;
import com.example.androidappgestionbasura.repository.BasuraMunicipalRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import static com.example.androidappgestionbasura.datos.firebase.constants.FirebaseConstants.TABLA_BASURAS_MUNICIPALES;

public class BasuraMunicipalRepositoryRepositoyImpl extends FirebaseRepository implements BasuraMunicipalRepository {

    private CollectionReference basurasMunicipalesCollectionReference;

    public BasuraMunicipalRepositoryRepositoyImpl(){
        basurasMunicipalesCollectionReference = FirebaseReferences.getInstancia().getDATABASE().collection(TABLA_BASURAS_MUNICIPALES);
    }



    @Override
    public void readBasurasMunicipales(final CallBack callback) {


        readCollection(basurasMunicipalesCollectionReference, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    callback.onSuccess(getDataFromQuerySnapshot(object));

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
    }


    public ListaBasurasMunicipales getDataFromQuerySnapshot(Object object) {
        ListaBasurasMunicipales basuras = new ListaBasurasMunicipales();
        QuerySnapshot queryDocumentSnapshots = (QuerySnapshot) object;
        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
            Log.d("TAG-PRUEBA","un snapshot");
            Log.d("TAG-PRUEBA",snapshot.getData().toString());

            BasuraMunicipal basuraMunicipal = snapshot.toObject(BasuraMunicipal.class);
//            Log.d("TAG-PRUEBA",basuraMunicipal.getTipoBasuraMunicipal().getTexto());
            basuras.getBasuraMunicipalList().add(basuraMunicipal);
        }
        return basuras;
    }
}
