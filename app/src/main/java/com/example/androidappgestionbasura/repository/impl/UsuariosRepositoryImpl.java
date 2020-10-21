package com.example.androidappgestionbasura.repository.impl;

import android.app.Activity;
import android.util.Log;

import com.example.androidappgestionbasura.callback.CallBack;
import com.example.androidappgestionbasura.constants.Constant;
import com.example.androidappgestionbasura.firebase.FirebaseRepository;
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

import javax.security.auth.callback.Callback;

import static com.example.androidappgestionbasura.constants.Constant.FAIL;
import static com.example.androidappgestionbasura.firebase.FirebaseConstants.TABLA_USUARIOS;
import static com.example.androidappgestionbasura.firebase.FirebaseDatabaseReference.DATABASE;

/**
 * @author Ruben Pardo Casanova
 * Escribimos la implementación de UsuarioRepository en una clase separada
 * llamada UsuariosRepositoryImpl.java que extiende FirebaseRepository e
 * implementa UsuariosRepository. En esta clase,
 * vamos a implementar todas las operaciones de lectura y escritura y
 * la lógica de manipulación de datos.
 */
public class UsuariosRepositoryImpl extends FirebaseRepository implements UsuariosRepository {

    private Activity activity;
    private CollectionReference usuariosCollectionReference;

    // constructor
    // Recogemos de la instancia de la base de datos la collecion de usuarios
    public UsuariosRepositoryImpl(Activity activity){
        this.activity = activity;
        usuariosCollectionReference = DATABASE.collection(TABLA_USUARIOS);
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
                    callback.onSuccess(Constant.SUCCESS);
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

                    Log.d("PRUEBA DESDE IMPL", "ENTRO");
                    if (object != null) {
                        // creamos un usuario y lo enviamos por el callbacl
                        Usuario usuario = getDataFromQuerySnapshot(object);
                        Log.d("PRUEBA DESDE IMPL", usuario.getName());
                        callback.onSuccess(usuario);

                    } else{
                        Log.d("PRUEBA DESDE IMPL", "NO EXISTE");
                        // si no existe devolvemos null
                        callback.onSuccess(null);
                    }
                }

                @Override
                public void onError(Object object) {
                    Log.d("PRUEBA DESDE IMPL", "ERROR");
                    callback.onError(object);
                }
            });


        }else {
            callback.onError(FAIL);
        }
    }


    // =================================
    // TODO IMPLEMENTAR UDPATE USUARIO Y DELETE USUARIO
    // =================================
    // actualizar perfil
    @Override
    public void updateUsuario(String userId, HashMap map, CallBack callback) {
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
            Log.d("PRUEBA DESDE IMPL", snapshot.getData().toString());
            Usuario employee = snapshot.toObject(Usuario.class);
            usuarios.add(employee);
        }
        return usuarios.get(0);
    }


}
