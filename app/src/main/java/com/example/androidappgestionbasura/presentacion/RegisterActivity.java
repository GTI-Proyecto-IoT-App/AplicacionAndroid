package com.example.androidappgestionbasura.presentacion;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.datos.firebase.constants.Constant;
import com.example.androidappgestionbasura.utility.Utility;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public class RegisterActivity extends AppCompatActivity {

    private static final int CODE_ACTIVITY_RESULT_GOOGLE = 1234;

    // controladors registro
    private EditText etRegisterNombre,etRegisterEmail,etRegisterContra,etRegisterRepetirContra;

    private LoadingDialogActivity loadingDialogActivity;
    private CasosUsoUsuario casosUsoUsuario;
    private ImageView btnVolverRatailer;
    private Button btnVolverLogin , btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        //full window para la acividad
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        //la orientacion siempre será vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        casosUsoUsuario = new CasosUsoUsuario(this);



        setUp(savedInstanceState);

    }
    /**
     * @author Ruben Pardo Casanova
     * inicializa la logica de auth acitivity
     * @param savedInstanceState estado anterior
     */
    private void setUp(Bundle savedInstanceState) {

        etRegisterNombre = findViewById(R.id.editTextRegisterName);
        etRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        etRegisterContra = findViewById(R.id.editTextRegisterPass);
        etRegisterRepetirContra = findViewById(R.id.editTextRegisterRepeatPass);



        //codigo para el boton de volver al login

        btnVolverLogin = findViewById(R.id.btnVolverLogin);
        btnVolverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarLogin(null);
            }
        });

        //subrayado del boton de volver al login
        btnVolverLogin = (Button) this.findViewById(R.id.btnVolverLogin);
        btnVolverLogin.setPaintFlags(btnVolverLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        //codigo para el boton de volver al ratailer
        btnVolverRatailer = findViewById(R.id.botonRetrocederLogin);
        btnVolverRatailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarRatailer(null);
            }
        });



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
                showRegisterError(object.toString());
            }
        });
    }

    //===============================================================================
    // CALLBACKS
    //===============================================================================


    /**
     *
     * Se lanzará un activity for result de google, el return
     * es on activity result de tipo GoogleAcount
     */
    public void startGoogleLogin(View v){
        hideErrors();// esconder por si estaba mostrado de antes
        if(Utility.isConnected(this)){
            GoogleSignInOptions googleCong = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this,googleCong);
            googleSignInClient.signOut();
            startActivityForResult(googleSignInClient.getSignInIntent(),CODE_ACTIVITY_RESULT_GOOGLE);
        }
        else{
            showRegisterError(Constant.CONNECTION_ERROR);
        }
    }

    /**
     * @author Ruben Pardo
     *  call back boton de registro
     *  comprueba si es valido el formulario
     *
     */
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

    /**
     * @author Ruben Pardo Casanova
     * Muestra el error en caso de fallar el registro
     */
    public void showRegisterError(String error){
        TextView tv = findViewById(R.id.tvRegisterError);
        tv.setVisibility(View.VISIBLE);
        if(error.contains(Constant.CONNECTION_ERROR)) {
            tv.setText(getString(R.string.error_de_conexion));
        }else if(error.contains("The email address is already in use by another account")){
            tv.setText(getString(R.string.error_email_already_in_use));
        }else {
            tv.setText(R.string.error_inesperado);
        }
        //TODO YA EXISTE ES CORREO ELECTRONICO
    }

    /**
     * @author Ruben Pardo Casanova
     * Esconde los errores de la vista
     */
    private void hideErrors(){
        TextView tvR = findViewById(R.id.tvRegisterError);
        tvR.setVisibility(View.GONE);
    }

    /**
     *
     * @return T/F -> si el formulario de registro
     * es valido
     */
    private boolean isValidFormRegister(){

        boolean isValid = true;

        String nombre = etRegisterNombre.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String contra = etRegisterContra.getText().toString();
        String contraRepeat = etRegisterRepetirContra.getText().toString();

        //-----------------------------------------
        // nombre no vacio
        if(nombre.length()==0){
            Utility.setError(this,getString(R.string.error_campo_vacio),etRegisterNombre);

            isValid = false;
        }
        //-----------------------------------------
        // email no vacio y email valido
        if(email.length()==0){
            Utility.setError(this,getString(R.string.error_campo_vacio),etRegisterEmail);
            isValid = false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Utility.setError(this,getString(R.string.error_correo_valido),etRegisterEmail);
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
     * @author Sergi Sirvent
     * METODO PARA VOLVER AL RATAILER
     * **/
    public void lanzarRatailer(View view){
        Intent i = new Intent(this,RatailerStartUpScreenActivity.class);
        startActivity(i);
    }

    /**
     * @author Sergi Sirvent
     * METODO PARA VOLVER AL LOGIN
     * **/
    public void lanzarLogin(View view){
        Intent i = new Intent(this,AuthActivity.class);
        startActivity(i);
    }

    //================================================================================
    // MANEJAR CICLO DE VIDA
    //================================================================================

    // guardar estado
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString("registro-nombre",etRegisterNombre.getText().toString());
        outState.putString("registro-correo",etRegisterEmail.getText().toString());
        outState.putString("registro-contra",etRegisterContra.getText().toString());
        outState.putString("registro-repetir-contra",etRegisterRepetirContra.getText().toString());


    }

    // recuperar estado
    private void recueprarEstadoSiEsPosible(Bundle savedInstanceState) {

        if(savedInstanceState!=null){
            etRegisterNombre.setText(savedInstanceState.getString("registro-nombre",""));
            etRegisterEmail.setText(savedInstanceState.getString("registro-correo",""));
            etRegisterContra.setText(savedInstanceState.getString("registro-contra",""));
            etRegisterRepetirContra.setText(savedInstanceState.getString("registro-repetir-contra",""));



        }

    }
}
