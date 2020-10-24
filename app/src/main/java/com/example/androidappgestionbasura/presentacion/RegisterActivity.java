package com.example.androidappgestionbasura.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.datos.firebase.constants.Constant;

public class RegisterActivity extends AppCompatActivity {

    // controladors registro
    private EditText etRegisterNombre,etRegisterEmail,etRegisterContra,etRegisterRepetirContra;

    private LoadingDialogActivity loadingDialogActivity;
    private CasosUsoUsuario casosUsoUsuario;
    private Button btnVolverLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        casosUsoUsuario = new CasosUsoUsuario(this);

        //codigo para el boton de volver al login
        btnVolverLogin = findViewById(R.id.botonReturnLogin);
        btnVolverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarLogin(null);
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

        etRegisterNombre = findViewById(R.id.editTextRegisterName);
        etRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        etRegisterContra = findViewById(R.id.editTextRegisterPass);
        etRegisterRepetirContra = findViewById(R.id.editTextRegisterRepeatPass);



        loadingDialogActivity = new LoadingDialogActivity(this);

        recueprarEstadoSiEsPosible(savedInstanceState);

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
