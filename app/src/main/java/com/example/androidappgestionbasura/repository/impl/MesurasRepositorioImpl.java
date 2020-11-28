package com.example.androidappgestionbasura.repository.impl;

import android.util.Log;

import com.example.androidappgestionbasura.datos.firebase.FirebaseReferences;
import com.example.androidappgestionbasura.datos.firebase.FirebaseRepository;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.mesuras_dispositivos.ListaMesuras;
import com.example.androidappgestionbasura.model.mesuras_dispositivos.Mesura;
import com.example.androidappgestionbasura.repository.MesurasRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;

import static com.example.androidappgestionbasura.datos.firebase.constants.FirebaseConstants.TABLA_DISPOSITIVOS;


/**
 * 26/11/2020 Rubén Pardo
 * Clase que gestiona los mesuras de los dispositivos y un usuario
 */
public class MesurasRepositorioImpl extends FirebaseRepository implements MesurasRepository  {

    private CollectionReference dispositivosCollectionReferencia;

    public MesurasRepositorioImpl(){
        dispositivosCollectionReferencia = FirebaseReferences.getInstancia().getDATABASE().collection(TABLA_DISPOSITIVOS);
    }

    /**
     * Obtiene los datos de un año de las basuras de un usuario
     * @param uid del usuario
     * @param callBack return List<Mesuras> || error
     */
    @Override
    public void readMesurasAnualesByUID(String uid, final CallBack callBack) {
        // coger todas las colleciones "mediciones" de todos los dispositivos vinculados

        Query query = dispositivosCollectionReferencia.whereArrayContains("usuariosVinculados",uid);
        final ListaMesuras listaMesuras = new ListaMesuras();
        readQueryDocuments(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {// object = querySnapshot
                QuerySnapshot querySnapshot = (QuerySnapshot) object;
                final int[] contDispositivos = {querySnapshot.size()};// para saber cuantas veces se
                    // va a llamar a onComplete cuando sea 0 se ha completado la llamada a base de datos

                // por cada dispositivo vinculado obtener la collecion mediciones
                for(QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot){
                    queryDocumentSnapshot.getReference().collection("mediciones").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        QuerySnapshot q = task.getResult();
                                        for(DocumentSnapshot documentSnapshot : q.getDocuments()){
                                            listaMesuras.getMesuras().add(documentSnapshot.toObject(Mesura.class));
                                        }
                                        contDispositivos[0]--;// on complete terminado
                                        if(contDispositivos[0] == 0){
                                            // solo tiene que llamarse una vez y es cuando todos los
                                            // on complete se termine
                                            callBack.onSuccess(listaMesuras);
                                        }


                                    }
                                }
                            });
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });


    }
}
