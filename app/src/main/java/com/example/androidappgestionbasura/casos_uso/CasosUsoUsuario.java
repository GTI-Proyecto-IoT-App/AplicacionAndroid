package com.example.androidappgestionbasura.casos_uso;

import android.app.Activity;
import android.content.Intent;

import com.example.androidappgestionbasura.datos.firebase.FirebaseRepository;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.datos.firebase.constants.Constant;
import com.example.androidappgestionbasura.datos.preferences.SharedPreferencesHelper;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.presentacion.AuthActivity;
import com.example.androidappgestionbasura.presentacion.HomeActivityPackage.HomeActivity;
import com.example.androidappgestionbasura.presentacion.RatailerStartUpScreenActivity;
import com.example.androidappgestionbasura.presentacion.VerfiyEmailActivity;
import com.example.androidappgestionbasura.repository.impl.UsuariosRepositoryImpl;
import com.example.androidappgestionbasura.utility.AppConf;
import com.example.androidappgestionbasura.utility.Utility;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

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


    // =============================================================================
    // Metodos generales de acceso
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
        if(Utility.isConnected(actividad)){
            firebaseRepository.loginUserWithPassAndEmail(email, password,
                    new CallBack() {
                        @Override
                        public void onSuccess(Object object) {// object = uid
                            String userUid = object.toString();
                            getUsuarioFirebase(userUid, new CallBack() {
                                @Override
                                public void onSuccess(Object object) {
                                    callBack.onSuccess(null);
                                    usuarioAccedeCorrectamente((Usuario) object);
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
        }else{
            callBack.onError(Constant.CONNECTION_ERROR);
        }
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
                    public void onSuccess(Object object) {  // object = Ususario
                        // si nos registramos con exito creamos en la base de datos un usuario
                        // nuevo
                        // cogemos el uid que se ha creado y el nombre
                        final Usuario user = (Usuario) object;
                        user.setName(nombre);
                        usuariosRepository.createUsuario(user, new CallBack() {
                            // SUCCES CREAR USUARIO
                            @Override
                            public void onSuccess(Object object) {
                                usuarioAccedeCorrectamente(user);
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
        if(Utility.isConnected(actividad)){
            firebaseRepository.loginWithCredential(googleSignInAccount, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    Task<AuthResult> task = (Task<AuthResult>)object;
                    // parametros del usuario logeado
                    boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();

                    if(isNew){
                        String nombre = task.getResult().getUser().getDisplayName();
                        String email = task.getResult().getUser().getEmail();
                        String uid = task.getResult().getUser().getUid();
                        final boolean isEmailVerified = task.getResult().getUser().isEmailVerified();
                        final Usuario user = new Usuario(nombre, uid,email,isEmailVerified);

                        // creamos el usuario si se ha logeado por primera vez en la app
                        usuariosRepository.createUsuario(user, new CallBack() {
                            // SUCCES CREAR USUARIO
                            @Override
                            public void onSuccess(Object object) {
                                usuarioAccedeCorrectamente(user);
                            }
                            @Override
                            public void onError(Object object) {
                                // TODO cambiar a mejor texto de error
                                callback.onError(object);
                            }
                        });
                    }else{
                        String uid = task.getResult().getUser().getUid();
                        usuariosRepository.readUsuarioByUID(uid, new CallBack() {
                            @Override
                            public void onSuccess(Object object) {// object = user
                                usuarioAccedeCorrectamente((Usuario) object);
                            }

                            @Override
                            public void onError(Object object) {
                                callback.onError(object);
                            }
                        });
                    }
                }

                @Override
                public void onError(Object object) {
                    callback.onError(object);
                }
            });
        }else{
            callback.onError(Constant.CONNECTION_ERROR);
        }
    }
    /**
     * Metodo que decide a que pagina ir (home o verificar)
     * depende del usuario que accede
     * @param user current usuario
     */
    private void usuarioAccedeCorrectamente(Usuario user){
        guardarUidUsuario((Usuario) user);
        if(user.isEmailVerified()){
            showHome(false);
        }else{
            showVerifyActivity();
        }
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
     * @author Ruben pardo casanova
     * Llamar al metodo de firebase para renviar el email
     * @return true o false si se pudo enviar
     */
    public boolean resendVerificationEmail() {
        if(Utility.isConnected(actividad)){
           return firebaseRepository.resendVerificationEmail();
        }else{
            return false;
        }
    }

    /**
     * Comprueba si esta verficado el email, si lo esta es porque no estaba verificado y lo esta comprobando si lo esta
     * hay que modificar el campo de la base de datos
     * @param callBack null si no esta verficado, fail si el update el usuario no existe
     *                 string con el error si no se pudo actualizar
     */
    public void checkIsEmailVerifiedAndVerifyIt(final CallBack callBack) {
        // comprobar conexion
        if(Utility.isConnected(actividad)){
            firebaseRepository.checkIfUserisVerified(new CallBack() {
                @Override
                public void onSuccess(Object object) {

                    usuario.setEmailVerified(true);
                    usuariosRepository.updateUsuario(usuario.getKey(),usuario.getMap(),callBack);
                }

                @Override
                public void onError(Object object) {

                    callBack.onError(null);// no esta verficado
                }
            });
        }else{
            callBack.onError(Constant.CONNECTION_ERROR);
        }


    }

    // ============================================================================
    // Metodos gestion Usuario

    /**
     * Pillamos el usuario de firebase
     * @param uid del usuario a pedir
     * return usuario:Usuario | error:String
     */
    private void getUsuarioFirebase(String uid, final CallBack callBack){
        usuariosRepository.readUsuarioByUID(uid, new CallBack() {
            @Override
            public void onSuccess(Object object) {//object = Usuario
                if(object!=null){
                    callBack.onSuccess(object);
                }else{
                    // TODO PONER MEJOR TEXTO DE ERROR
                    callBack.onError(null);// devolvemos null si no existe
                }
            }

            @Override
            public void onError(Object object) {
                // TODO PONER MEJOR TEXTO DE ERROR
                // aqui lo mas probale es que sea un error de conexion
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
                    callBack.onSuccess(object);
                }
                @Override
                public void onError(Object object) {
                    callBack.onError(object);
                }
            });
        }else{
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
    public void guardarUidUsuario(Usuario user) {
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



    // ===================================================================
    // metodos para moverse entre activities

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
        Intent intentHome = new Intent(actividad, RatailerStartUpScreenActivity.class);
        actividad.startActivity(intentHome);
        actividad.finish();
    }
    public void showVerifyActivity() {
        Intent intent = new Intent(actividad, VerfiyEmailActivity.class);
        actividad.startActivity(intent);
        actividad.finish();
    }



}
