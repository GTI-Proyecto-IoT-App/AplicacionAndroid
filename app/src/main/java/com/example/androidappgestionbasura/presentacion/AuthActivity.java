package com.example.androidappgestionbasura.presentacion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.example.androidappgestionbasura.datos.firebase.FirebaseReferences;
import com.example.androidappgestionbasura.datos.firebase.FirebaseRepository;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class AuthActivity extends AppCompatActivity {

    // controladors registro
    private EditText etRegisterNombre,etRegisterEmail,etRegisterContra,etRegisterRepetirContra;
    // controladores login
    private EditText etLoginNombreEmail, etLoginContra;

    private LoadingDialogActivity loadingDialogActivity;
    private CasosUsoUsuario casosUsoUsuario;

    private static final int CODE_ACTIVITY_RESULT_GOOGLE = 1234;

    private Button btnLanzarRegistro;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        casosUsoUsuario = new CasosUsoUsuario(this);
        setContentView(R.layout.activity_auth);
        btnLanzarRegistro =findViewById(R.id.botonLanzarRegistro);
        btnLanzarRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarRegistro(null);
            }
        });
        setUp(savedInstanceState);





    }

    /**
     * @author Ruben Pardo Casanova
     * inicializa la logica de auth acitivity
     * @param savedInstanceState estado anterior
     */
    private void setUp(Bundle savedInstanceState) {
/*
        etRegisterNombre = findViewById(R.id.editTextRegisterName);
        etRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        etRegisterContra = findViewById(R.id.editTextRegisterPass);
        etRegisterRepetirContra = findViewById(R.id.editTextRegisterRepeatPass);

 */

        etLoginNombreEmail = findViewById(R.id.editTextLoginUsuarioEmail);
        etLoginContra = findViewById(R.id.editTextLoginPass);

        loadingDialogActivity = new LoadingDialogActivity(this);

        recueprarEstadoSiEsPosible(savedInstanceState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_ACTIVITY_RESULT_GOOGLE){
           try{
               Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
               GoogleSignInAccount googleSignInAccount =  googleSignInAccountTask.getResult();
               if(googleSignInAccount!=null){
                   logInWithGoogle(googleSignInAccount);
               }
           }catch (Exception ignored){ }
        }
    }

    //===============================================================================
    // CALLBACKS
    //===============================================================================

    /**
     * @author Ruben Pardo
     *  call back boton de registro
     *  comprueba si es valido el formulario
     *
     */
    /*
    public void signUp(View v){
        hideErrors();// esconder por si estaba mostrado de antes
       if(isValidFormRegister()){
           showCarga(true);
           casosUsoUsuario.signUp(etRegisterEmail.getText().toString(),
                   etRegisterContra.getText().toString(),
                   etRegisterNombre.getText().toString(),
                   new CallBack() {

                       @Override
                       public void onSuccess(Object object) {
                           showCarga(false);
                       }

                       @Override
                       public void onError(Object object) {
                            showCarga(false);
                           showRegisterError(object.toString());
                       }
                   });
       }
    }

     */

    /** @author Ruben Pardo
     * call back boton de registro
     * comprueba si es valido el formulario
    */
    public void logIn(View v){
        hideErrors();// esconder por si estaba mostrado de antes
        if(isValidFormLogin()){
            showCarga(true);
            casosUsoUsuario.login(etLoginNombreEmail.getText().toString(),
                    etLoginContra.getText().toString(), new CallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            // parar el progres dialog
                            showCarga(false);
                        }

                        @Override
                        public void onError(Object object) {
                            showCarga(false);
                            showLoginError(object.toString());
                        }
                    });
        }
    }

    /**
     *
     * Se lanzará un activity for result de google, el return
     * es on activity result de tipo GoogleAcount
     */
    public void startGoogleLogin(View v){
        GoogleSignInOptions googleCong = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this,googleCong);
        googleSignInClient.signOut();
       startActivityForResult(googleSignInClient.getSignInIntent(),CODE_ACTIVITY_RESULT_GOOGLE);
    }

    /**
     * Iniciar sesion con los credenciales de google
     */
    private void logInWithGoogle(GoogleSignInAccount account){
        showCarga(true);
        casosUsoUsuario.loginGoogle(account, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                showCarga(false);

            }
            @Override
            public void onError(Object object) {
                showCarga(false);
                showLoginError(object.toString());
            }
        });
    }
    //===============================================================================
    // LOGICA VISUAL
    //===============================================================================

    /**
     * @author Ruben Pardo Casanova
     * Muestra el error en caso de fallar el registro
     */
    /*
    public void showRegisterError(String error){
        TextView tv = findViewById(R.id.tvRegisterError);
        tv.setVisibility(View.VISIBLE);
        tv.setText(error);
    }
    */

    /**
     * @author Ruben Pardo Casanova
     * Muestra el error en caso de fallar el inicio de sesion
     */
    public void showLoginError(String error){
        TextView tv = findViewById(R.id.tvRegisterError);
        tv.setVisibility(View.VISIBLE);
        tv.setText(error);
    }

    /**
     * @author Ruben Pardo Casanova
     * Esconde los errores de la vista
     */
    private void hideErrors(){
        //TextView tvR = findViewById(R.id.tvRegisterError);
        TextView tvL = findViewById(R.id.tvLoginError);
        //tvR.setVisibility(View.GONE);
        tvL.setVisibility(View.GONE);
    }

    /**
     *
     * @return T/F -> si el formulario de registro
     * es valido
     */
    /*
    private boolean isValidFormRegister(){

        boolean isValid = true;

        String nombre = etRegisterNombre.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String contra = etRegisterContra.getText().toString();
        String contraRepeat = etRegisterRepetirContra.getText().toString();

        //-----------------------------------------
        // nombre no vacio
        if(nombre.length()==0){
            etRegisterNombre.setError(getString(R.string.error_campo_vacio));
            isValid = false;
        }
        //-----------------------------------------
        // email no vacio y email valido
        if(email.length()==0){
            etRegisterEmail.setError(getString(R.string.error_campo_vacio));
            isValid = false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etRegisterEmail.setError(getString(R.string.error_correo_valido));
            isValid = false;
        }

        //-----------------------------------------
        // contraseña
        if(contra.length()<8){
            isValid = false;
            etRegisterContra.setError(getString(R.string.error_contr_no_valida));

        }
        else{
            boolean mayuscula = false;
            boolean minuscula = false;
            boolean numero = false;
            boolean caracter_raro = false;

            // comprobar que tiene una mayusucula, minuscula, numero y caracter especial
            for(int i = 0;i<contra.length();i++) {
                if(((int) contra.charAt(i)) >= 65 && (int) contra.charAt(i) <= 90)
                {
                    mayuscula = true;
                }
                else if((int) contra.charAt(i) >= 97 && (int) contra.charAt(i) <= 122)
                {
                    minuscula = true;
                }
                else if((int)contra.charAt(i) >= 48 && (int)contra.charAt(i) <= 57)
                {
                    numero = true;
                }
                else
                {
                    caracter_raro = true;
                }
            }

            if(!mayuscula || !minuscula || !caracter_raro || !numero)
            {
                isValid = false;
                etRegisterContra.setError(getString(R.string.error_contr_no_valida));
            }

        }

        if(contraRepeat.length()==0){
            etRegisterRepetirContra.setError(getString(R.string.error_campo_vacio));
        }
        else if(!contraRepeat.equals(contra)){
            isValid = false;
            etRegisterRepetirContra.setError(getString(R.string.error_contra_no_igual));
        }


        return isValid;
    }*/
    /**
     * Ningun campo vacío
     * @return T/F si el forumlario del login es valido
     */
    private boolean isValidFormLogin() {
        boolean isValid = true;

        String email = etLoginNombreEmail.getText().toString().trim();
        String contra = etLoginContra.getText().toString();

        if(email.length()==0){
            isValid = false;
            etLoginNombreEmail.setError(getString(R.string.error_campo_vacio));
        }
        if(contra.length()==0){
            etLoginContra.setError(getString(R.string.error_campo_vacio));
        }

        return isValid;

    }


    private void showCarga(boolean show){
        if(show){
            loadingDialogActivity.startLoadingDialog();
        }else{
            // quitar el progress dialog
            loadingDialogActivity.dismissDialog();
        }
    }

    /**
     *
     * METODO PARA LANZAR LA ACTIVIDAD REGISTRO
     * **/

    public void lanzarRegistro(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    //================================================================================
    // MANEJAR CICLO DE VIDA
    //================================================================================

    // guardar estado
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        /*
        outState.putString("registro-nombre",etRegisterNombre.getText().toString());
        outState.putString("registro-correo",etRegisterEmail.getText().toString());
        outState.putString("registro-contra",etRegisterContra.getText().toString());
        outState.putString("registro-repetir-contra",etRegisterRepetirContra.getText().toString());
        */



        outState.putString("login-correo",etLoginNombreEmail.getText().toString());
        outState.putString("login-contra",etLoginContra.getText().toString());

    }

    // recuperar estado
    private void recueprarEstadoSiEsPosible(Bundle savedInstanceState) {

        if(savedInstanceState!=null){
            /*
            etRegisterNombre.setText(savedInstanceState.getString("registro-nombre",""));
            etRegisterEmail.setText(savedInstanceState.getString("registro-correo",""));
            etRegisterContra.setText(savedInstanceState.getString("registro-contra",""));
            etRegisterRepetirContra.setText(savedInstanceState.getString("registro-repetir-contra",""));
            */
            etLoginContra.setText(savedInstanceState.getString("login-contra",""));
            etLoginNombreEmail.setText(savedInstanceState.getString("login-correo",""));

        }

    }


}