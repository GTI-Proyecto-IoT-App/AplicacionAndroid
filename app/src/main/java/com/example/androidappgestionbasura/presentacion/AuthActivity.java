package com.example.androidappgestionbasura.presentacion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


    // controladores login
    private EditText etLoginNombreEmail, etLoginContra;
    private ImageView btnLanzarRatailer;
    private Button btnLanzarRegistro;

    private LoadingDialogActivity loadingDialogActivity;
    private CasosUsoUsuario casosUsoUsuario;

    private static final int CODE_ACTIVITY_RESULT_GOOGLE = 1234;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //full window para la acividad
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_auth);

        //la orientacion siempre será vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setUp(savedInstanceState);

    }

    /**
     * @author Ruben Pardo Casanova
     * inicializa la logica de auth acitivity
     * @param savedInstanceState estado anterior
     */
    private void setUp(Bundle savedInstanceState) {
        casosUsoUsuario = new CasosUsoUsuario(this);

        //codigo para volver al ratailer
        btnLanzarRatailer = findViewById(R.id.botonRetrocederLogin);
        btnLanzarRatailer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarActividadRatailer(null);
            }
        });

        //codigo para volver al registro
        btnLanzarRegistro = findViewById(R.id.botonLanzarRegistro);
        btnLanzarRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarActividadRegistro(null);
            }
        });

        //subrayado del boton de volver al registro
        btnLanzarRegistro = (Button) this.findViewById(R.id.botonLanzarRegistro);
        btnLanzarRegistro.setPaintFlags(btnLanzarRegistro.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);



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
     * Muestra el error en caso de fallar el inicio de sesion
     */
    public void showLoginError(String error){
        TextView tv = findViewById(R.id.tvLoginError);
        tv.setVisibility(View.VISIBLE);
        tv.setText(error);
    }

    /**
     * @author Ruben Pardo Casanova
     * Esconde los errores de la vista
     */
    private void hideErrors(){
        TextView tvL = findViewById(R.id.tvLoginError);
        tvL.setVisibility(View.GONE);
    }

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
     * @author Sergi Sirvent
     * METODO PARA LANZAR LA ACTIVIDAD RATAILER
     **/
    public void lanzarActividadRatailer(View view) {
        Intent i = new Intent(this, RatailerStartUpScreenActivity.class);
        startActivity(i);
    }

    /**
     * @author Sergi Sirvent
     * METODO PARA LANZAR LA ACTIVIDAD REGISTRO
     **/
    public void lanzarActividadRegistro(View view) {
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

        outState.putString("login-correo",etLoginNombreEmail.getText().toString());
        outState.putString("login-contra",etLoginContra.getText().toString());

    }

    // recuperar estado
    private void recueprarEstadoSiEsPosible(Bundle savedInstanceState) {

        if(savedInstanceState!=null){

            etLoginContra.setText(savedInstanceState.getString("login-contra",""));
            etLoginNombreEmail.setText(savedInstanceState.getString("login-correo",""));

        }

    }


}