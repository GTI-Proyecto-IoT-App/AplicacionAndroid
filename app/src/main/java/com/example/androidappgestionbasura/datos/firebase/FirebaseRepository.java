package com.example.androidappgestionbasura.datos.firebase;

import android.app.Activity;
import android.content.Context;
import android.provider.DocumentsContract;
import android.util.Log;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.datos.firebase.callback.FirebaseChildCallBack;
import com.example.androidappgestionbasura.datos.firebase.constants.Constant;
import com.example.androidappgestionbasura.model.Usuario;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.firestore.model.Document;

import java.util.Map;

import javax.security.auth.callback.Callback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Ruben Pardo Casanova
 * Escribimos todos los oyentes de lectura y escritura
 * de Firebase Cloud Firestore en una clase para que sea fácil
 * de administrar y flexible para cambios futuros.
 */
public class FirebaseRepository {

    /**
     * Crea un usuario en firebase mediante email y contraseña
     * @param email del usuario
     * @param password del usuario
     * @param callback devolvera el Usuario con email, uid, isEmailVerirfied si succes, por otra parte el error si falla algo
     */
    public final void createUserWithPassAndEmail(final String email, final String password, final CallBack callback){
        final FirebaseAuth firebaseAuth = FirebaseReferences.getInstancia().getFIREBASE_AUTH();
        firebaseAuth
                .createUserWithEmailAndPassword(email,
                        password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(!firebaseAuth.getCurrentUser().isEmailVerified()){
                                firebaseAuth.getCurrentUser().sendEmailVerification();
                            }
                            Usuario user = new Usuario(email,
                                    firebaseAuth.getCurrentUser().getUid(),
                                    firebaseAuth.getCurrentUser().isEmailVerified());
                            callback.onSuccess(user);
                        }else{
                            callback.onError(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    /**
     * Inicia sesion con email y contraseña
     * @param email del usuario
     * @param password del usuario
     * @param callback devolvemos el uid en succes y el error en onError
     */
    public final void loginUserWithPassAndEmail(final String email, final String password, final CallBack callback){
        final FirebaseAuth firebaseAuth = FirebaseReferences.getInstancia().getFIREBASE_AUTH();
        firebaseAuth
                .signInWithEmailAndPassword(email,
                        password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            callback.onSuccess(firebaseAuth.getCurrentUser().getUid());
                        }else{
                            callback.onError(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    /**
     * Cierra la sesion del usuario activo
     * @param callBack onSucces o onError
     */
    public final void logOutUser(CallBack callBack) {
        final FirebaseAuth firebaseAuth = FirebaseReferences.getInstancia().getFIREBASE_AUTH();
        firebaseAuth.signOut();
        callBack.onSuccess(null);
    }

    /**
     *
     * @param account cuenta de google seleccionada
     * @param callback return succes:Task o error:String
     */
    public void loginWithCredential(GoogleSignInAccount account, final CallBack callback) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        FirebaseAuth firebaseAuth = FirebaseReferences.getInstancia().getFIREBASE_AUTH();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    callback.onSuccess(task);
                }else{
                    callback.onError(task.getException().getLocalizedMessage());
                }
            }
        });
    }

    /**
     * Renvia el correo de verificacion
     */
    public boolean resendVerificationEmail() {
        try{
            FirebaseReferences.getInstancia().getFIREBASE_AUTH().getCurrentUser().sendEmailVerification();
            return true;
        }catch (NullPointerException error){
            return false;
        }

    }

    /** Como se actualiza de forma externa a la app hay que recargar el usuario de firebase
     * @param callBack return T/F si el usuario esta verificado
     */
    public void checkIfUserisVerified(final CallBack callBack) {
        FirebaseAuth firebaseAuth = FirebaseReferences.getInstancia().getFIREBASE_AUTH();
        Task<Void> usertask = firebaseAuth.getCurrentUser().reload();
        usertask.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    if(FirebaseReferences.getInstancia().getFIREBASE_AUTH().getCurrentUser().isEmailVerified()){
                        callBack.onSuccess(null);
                    }else{
                        callBack.onError(null);
                    }

                }else{
                    callBack.onError(task.getException().getLocalizedMessage());
                }
            }

        });
    }

    public void sendEmailForgotPassword(final CallBack callBack){

    }


    /**
     * Insert data on FireStore
     *
     * @param documentReference Document reference of data to be add
     * @param model             Model to insert into Document
     * @param callback          callback for event handling
     */
    protected final void fireStoreCreate(final DocumentReference documentReference, final Object model, final CallBack callback) {
        documentReference.set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onSuccess(Constant.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onError(e);
            }
        });
    }

    /**
     * Update data to FireStore
     *
     * @param documentReference Document reference of data to update
     * @param map               Data map to update
     * @param callback          callback for event handling
     */
    protected final void fireStoreUpdate(final DocumentReference documentReference, final Map<String, Object> map, final CallBack callback) {
        documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onSuccess(Constant.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onError(e);
            }
        });
    }

    /**
     * FireStore Create or Merge
     *
     * @param documentReference Document reference of data to create update
     * @param model             Model to create or update into Document
     */
    protected final void fireStoreCreateOrMerge(final DocumentReference documentReference, final Object model, final CallBack callback) {
        documentReference.set(model, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onSuccess(Constant.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onError(e);
            }
        });
    }

    /**
     * Delete data from FireStore
     *
     * @param documentReference Document reference of data to delete
     * @param callback          callback for event handling
     */
    protected final void fireStoreDelete(final DocumentReference documentReference, final CallBack callback) {
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onSuccess(Constant.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onError(e);
            }
        });
    }

    /**
     * FireStore Batch write
     *
     * @param batch    Document reference of data to delete
     * @param callback callback for event handling
     */
    protected final void batchWrite(WriteBatch batch, final CallBack callback) {
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    callback.onSuccess(Constant.SUCCESS);
                else
                    callback.onError(Constant.FAIL);
            }
        });
    }

    /**
     * One time data fetch from FireStore with Document reference
     *
     * @param documentReference query of Document reference to fetch data
     * @param callBack          callback for event handling
     */
    protected final void readDocument(final DocumentReference documentReference, final CallBack callBack) {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        callBack.onSuccess(document);
                    } else {
                        callBack.onSuccess(null);
                    }
                } else {
                    callBack.onError(task.getException());
                }
            }
        });
    }


    protected final void readCollection(final CollectionReference collectionReference, final CallBack callBack) {
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                  callBack.onSuccess(task.getResult());
                }else{
                    callBack.onError(null);
                }
            }
        });
    }
    /**
     * Data fetch listener with Document reference
     *
     * @param documentReference to add childEvent listener
     * @param callBack          callback for event handling
     * @return EventListener
     */
    protected final ListenerRegistration readDocumentByListener(final DocumentReference documentReference, final CallBack callBack) {
        return documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    callBack.onError(e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    callBack.onSuccess(snapshot);
                } else {
                    callBack.onSuccess(null);
                }
            }
        });
    }

    /**
     * One time data fetch from FireStore with Query reference
     *
     * @param query    query of Document reference to fetch data
     * @param callBack callback for event handling
     */
    protected final void readQueryDocuments(final Query query, final CallBack callBack) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null ) {
                        callBack.onSuccess(querySnapshot);
                    } else {
                        callBack.onSuccess(null);
                    }
                } else {
                    callBack.onError(task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onError(e);
            }
        });
    }

    /**
     * Data fetch listener with Query reference
     *
     * @param query    query of Document reference to fetch data
     * @param callBack callback for event handling
     */
    protected final ListenerRegistration readQueryDocumentsByListener(final Query query, final CallBack callBack) {
        return query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    callBack.onError(e);
                    return;
                }
                callBack.onSuccess(value);
            }
        });
    }

    /**
     * Data fetch ChildEventListener with Query reference
     *
     * @param query    to add childEvent listener
     * @param callBack callback for event handling
     * @return ChildEventListener
     */
    protected final ListenerRegistration readQueryDocumentsByChildEventListener(final Query query, final FirebaseChildCallBack callBack) {
        return query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null || snapshots == null || snapshots.isEmpty()) {
                    callBack.onCancelled(e);
                    return;
                }
                for (DocumentChange documentChange : snapshots.getDocumentChanges()) {
                    switch (documentChange.getType()) {
                        case ADDED:
                            callBack.onChildAdded(documentChange.getDocument());
                            break;
                        case MODIFIED:
                            callBack.onChildChanged(documentChange.getDocument());
                            break;
                        case REMOVED:
                            callBack.onChildRemoved(documentChange.getDocument());
                            break;
                    }
                }
            }
        });
    }

    /**
     * REad offline data from FireBase
     *
     * @param query Document reference of data to create
     */
    protected final void fireStoreOfflineRead(final Query query, final CallBack callBack) {
        query.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    callBack.onError(e);
                    return;
                }
                callBack.onSuccess(querySnapshot);
            }
        });
    }


}
