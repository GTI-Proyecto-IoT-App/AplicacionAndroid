package com.example.androidappgestionbasura.casos_uso;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.datos.firebase.FirebaseRepository;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.datos.preferences.SharedPreferencesHelper;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.presentacion.AuthActivity;
import com.example.androidappgestionbasura.presentacion.HomeActivity;
import com.example.androidappgestionbasura.repository.impl.UsuariosRepositoryImpl;
import com.example.androidappgestionbasura.utility.AppConf;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import javax.security.auth.callback.Callback;

public class CasosUsoUsuario {

    // repositorios remotos
    private final UsuariosRepositoryImpl usuariosRepository;// leer crear usuarios
    private final FirebaseRepository firebaseRepository;//login

    // repositorios locales
    private final SharedPreferencesHelper sharedPreferencesHelper;// preferences

    // variables
    private Usuario usuario;
    private Activity actividad;

    public CasosUsoUsuario(Activity actividad){
        this.actividad = actividad;
        firebaseRepository = new FirebaseRepository();
        usuariosRepository = new UsuariosRepositoryImpl();
        usuario = ((AppConf) actividad.getApplicationContext()).getUsuario();
        SharedPreferencesHelper.initializeInstance(actividad);
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance();
    }

    /**
     *
     * Inicia sesion con correo y contraseña, si es correcto
     * obtendrá el objeto usuario asociado en la base de datos y guardará el uid
     * en preferences para saber mantener la sesion activa y guardamos el objeto
     * en appConf para tenerlo globalmente para posteriores usos
     * @param email del usuario a logearse
     * @param password del usuario a logearse
     * return Si da error devolverá el string
     */
    public void login(String email, String password, final CallBack callBack){
        // crear usuario con email y contraseña y añadir un on complete listener
        // para actuar cunado se cree el usuario en la BDD
        firebaseRepository.loginUserWithPassAndEmail(email, password,
                new CallBack() {
                    @Override
                    public void onSuccess(Object object) {// object = uid
                        String userUid = object.toString();
                        getUsuarioFirebase(userUid, new CallBack() {
                            @Override
                            public void onSuccess(Object object) {
                                callBack.onSuccess(null);
                                // guardamos el usuario en shared y en preferences
                                guardarUidUsuario((Usuario)object);
                                // nos movemos a home
                                showHome(false);
                            }

                            @Override
                            public void onError(Object object) {
                                // TODO PONER MEJOR TEXTO DE ERROR
                                callBack.onError(object.toString());

                            }
                        });
                    }

                    @Override
                    public void onError(Object object) {
                        callBack.onError(object.toString());
                    }
                });
    }
    /**
     * @author Ruben Pardo
     *  call back boton de registro
     *  comprueba si es valido el formulario
     *
     */
    public void signUp(String email, String password, final String nombre, final CallBack callBack){
        // crear usuario con email y contraseña y añadir un on complete listener
        // para actuar cunado se cree el usuario en la BDD
        firebaseRepository.createUserWithPassAndEmail(email, password,
                new CallBack() {
                    // SUCCER LOGIN
                    @Override
                    public void onSuccess(Object object) {  // object = uid
                        // si nos registramos con exito creamos en la base de datos un usuario
                        // nuevo
                        // cogemos el uid que se ha creado y el nombre
                        String uid = object.toString();
                        final Usuario user = new Usuario(nombre, uid);
                        usuariosRepository.createUsuario(user, new CallBack() {

                            // SUCCES CREAR USUARIO
                            @Override
                            public void onSuccess(Object object) {
                                guardarUidUsuario((Usuario) user);
                                showHome(false);
                            }

                            @Override
                            public void onError(Object object) {
                                // TODO cambiar a mejor texto de error
                                callBack.onError(object);
                            }
                        });
                    }

                    // ERROR LOGIN
                    @Override
                    public void onError(Object object) {
                        // TODO cambiar a mejor texto de error
                        callBack.onError(object);
                    }
                });
    }

    /**
     * Nos logeamos con credenciales del proveedor de google a firebase
     * si la cuenta es nueva se creara el usuario
     * @param googleSignInAccount cuenta seleccionada con la activity de google
     * @param callback return String Error
     */
    public void loginGoogle(GoogleSignInAccount googleSignInAccount, final CallBack callback) {
        firebaseRepository.loginWithCredential(googleSignInAccount, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                Task<AuthResult> task = (Task<AuthResult>)object;
                boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                String nombre = task.getResult().getUser().getDisplayName();
                String uid = task.getResult().getUser().getUid();
                final Usuario user = new Usuario(nombre, uid);
                if(isNew){
                    // creamos el usuario si se ha logeado por primera vez en la app
                    usuariosRepository.createUsuario(user, new CallBack() {
                        // SUCCES CREAR USUARIO
                        @Override
                        public void onSuccess(Object object) {
                            guardarUidUsuario((Usuario) user);
                            showHome(false);
                        }
                        @Override
                        public void onError(Object object) {
                            // TODO cambiar a mejor texto de error
                            callback.onError(object);
                        }
                    });
                }else{
                    guardarUidUsuario((Usuario) user);
                    showHome(false);
                }
            }

            @Override
            public void onError(Object object) {
                callback.onError(object);
            }
        });
    }

    /**
     * @author Ruben Pardo Casanova
     * Cerramos sesion y volvemos a auth activity
     * Borramos el uid del preference y de app
     */
    public void cerrarSesion() {

        firebaseRepository.logOutUser(
                new CallBack(){
                    @Override
                    public void onSuccess(Object object) {
                        borrarUsuario();
                        showAuthActivity();
                    }

                    @Override
                    public void onError(Object object) {}
        });
    }


    /**
     * Pillamos el usuario de firebase
     * @param uid del usuario a pedir
     * return usuario:Usuario | error:String
     */
    private void getUsuarioFirebase(String uid, final CallBack callBack){
        usuariosRepository.readUsuarioByKey(uid, new CallBack() {
            @Override
            public void onSuccess(Object object) {//object = Usuario
                if(object!=null){
                    callBack.onSuccess(object);
                }else{
                    // TODO PONER MEJOR TEXTO DE ERROR
                    callBack.onError("Error inesperado");
                }
            }

            @Override
            public void onError(Object object) {
                // TODO PONER MEJOR TEXTO DE ERROR
                callBack.onError(object.toString());
            }
        });
    }

    /**
     * Las activities que necesiten hacer uso del objeto usuario
     * llamarán a este metodo, puede que no este guardado en memoria
     * como por ejemplo si se cierra la app y se entra con la uid de
     * cache ( sharedpreferences)
     * @param callBack usuario:Usuario | error:String
     */
    public void getUsuarioSiExisteSinoPedirlo(final CallBack callBack){
        if(usuario==null){
            getUsuarioFirebase(sharedPreferencesHelper.getUID(), new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    guardarUidUsuario((Usuario) object);
                    callBack.onSuccess(object);
                }
                @Override
                public void onError(Object object) {
                    callBack.onError(object);
                }
            });
        }else{
            Log.d("GETUSER","EXISTE");
            callBack.onSuccess(usuario);
        }
    }
    public Usuario getUsuario(){
        return usuario;
    }
    public boolean isUsuarioLogeado(){
        return !sharedPreferencesHelper.getUID().equals("");
    }

    /**
     *  guardamos el usuario en variable globar y en shared prefences
     * @author Ruben Pardo Casanova
     * @param user usuario logeado que se guardará
     */
    private void guardarUidUsuario(Usuario user) {
        // lo guardamos para tener acceso al usuario globalmente
        ((AppConf) actividad.getApplicationContext()).setUsuario(user);
        usuario = user;
        sharedPreferencesHelper.setUID(user.getUid());
    }
    /**
     * Borramos de memoria el uid y el usuario
     */
    private void borrarUsuario() {
        // lo guardamos para tener acceso al usuario globalmente
        ((AppConf) actividad.getApplicationContext()).setUsuario(null);
        usuario = null;
        sharedPreferencesHelper.setUID("");
    }





    /**
     *
     * @author Ruben Pardo Casanova
     * Abre la home activity
     * @param error si se lanza desde la splash activity y no pudo obtener
     *              el usuario ir al home con error
     */
    public void showHome(boolean error){
        Intent intentHome = new Intent(actividad, HomeActivity.class);
        intentHome.putExtra("error",error);
        actividad.startActivity(intentHome);
        actividad.finish();
    }


    public void showAuthActivity() {
        Intent intentHome = new Intent(actividad, AuthActivity.class);
        actividad.startActivity(intentHome);
        actividad.finish();
    }



}
