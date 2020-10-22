package com.example.androidappgestionbasura.repository.impl;

import android.util.Log;

import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.datos.firebase.constants.Constant;
import com.example.androidappgestionbasura.datos.firebase.FirebaseReferences;
import com.example.androidappgestionbasura.datos.firebase.FirebaseRepository;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.repository.UsuariosRepository;
import com.example.androidappgestionbasura.utility.Utility;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.androidappgestionbasura.datos.firebase.constants.Constant.FAIL;
import static com.example.androidappgestionbasura.datos.firebase.constants.Constant.SUCCESS;
import static com.example.androidappgestionbasura.datos.firebase.constants.FirebaseConstants.TABLA_USUARIOS;

/**
 * @author Ruben Pardo Casanova
 * Escribimos la implementación de UsuarioRepository en una clase separada
 * llamada UsuariosRepositoryImpl.java que extiende FirebaseRepository e
 * implementa UsuariosRepository. En esta clase,
 * vamos a implementar todas las operaciones de lectura y escritura y
 * la lógica de manipulación de datos.
 */
public class UsuariosRepositoryImpl extends FirebaseRepository implements UsuariosRepository {

    private CollectionReference usuariosCollectionReference;

    // constructor
    // Recogemos de la instancia de la base de datos la collecion de usuarios
    public UsuariosRepositoryImpl(){
        usuariosCollectionReference = FirebaseReferences.getInstancia().getDATABASE().collection(TABLA_USUARIOS);
    }


    // Se usa al registrarse
    @Override
    public void createUsuario(Usuario user, final CallBack callback) {
        // cogemos el key del document
        String pushKey = usuariosCollectionReference.document().getId();
        if(user!=null && !Utility.isEmptyOrNull(pushKey)){
            user.setKey(pushKey);
            DocumentReference documentReference = usuariosCollectionReference.document(pushKey);

            // intentara crear el usuario y al terminar llamará al callback de on succes o error
            fireStoreCreate(documentReference, user, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    callback.onSuccess(SUCCESS);
                }

                @Override
                public void onError(Object object) {
                    callback.onError(object);
                }
            });
        }else{
            callback.onError(FAIL);
        }

    }


    // Se usa al logearse
    @Override
    public void readUsuarioByKey(String userId, final CallBack callback) {
        if(!Utility.isEmptyOrNull(userId)){
            // cogemos el usuario por id
            Query documentReference = usuariosCollectionReference.whereEqualTo("uid",userId);

            readQueryDocuments(documentReference, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    if (object != null) {
                        // creamos un usuario y lo enviamos por el callbacl
                        Usuario usuario = getDataFromQuerySnapshot(object);
                        callback.onSuccess(usuario);

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


    // =================================
    // TODO IMPLEMENTAR DELETE USUARIO
    // =================================
    // actualizar perfil
    @Override
    public void updateUsuario(String userKey, HashMap map, final CallBack callback) {
        if (!Utility.isEmptyOrNull(userKey)) {
            DocumentReference documentReference = usuariosCollectionReference.document(userKey);
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
        } else {
            callback.onError(FAIL);
        }

    }

    // borrar cuenta
    @Override
    public void deleteUsuario(String userId, CallBack callback) {

    }


    /**
     * Convierte el snapshot en un objeto usuario
     * @param object data recivida de una query snapshot
     * @return va a ser siempre un usuario
     */
    public Usuario getDataFromQuerySnapshot(Object object) {
        List<Usuario> usuarios = new ArrayList<>();
        QuerySnapshot queryDocumentSnapshots = (QuerySnapshot) object;
        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
            Usuario employee = snapshot.toObject(Usuario.class);
            usuarios.add(employee);
        }
        return usuarios.get(0);
    }


}
