package com.example.androidappgestionbasura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthActivity extends AppCompatActivity {

    // controladors registro
    private EditText etRegisterNombre,etRegisterEmail,etRegisterContra,etRegisterRepetirContra;
    private EditText etLoginNombreEmail, etLoginContra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        setUp(savedInstanceState);

    }


    // inicializa la logica de auth acitivity
    private void setUp(Bundle savedInstanceState) {
        etRegisterNombre = findViewById(R.id.editTextRegisterName);
        etRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        etRegisterContra = findViewById(R.id.editTextRegisterPass);
        etRegisterRepetirContra = findViewById(R.id.editTextRegisterRepeatPass);

        etLoginNombreEmail = findViewById(R.id.editTextLoginUsuarioEmail);
        etLoginContra = findViewById(R.id.editTextLoginPass);

        recueprarEstadoSiEsPosible(savedInstanceState);

    }

    //===============================================================================
    // CALLBACKS
    //===============================================================================

    // call back boton de registro
    // comprueba si es valido el formulario
    public void signUp(View v){
        hideErrors();// esconder por si estaba mostrado de antes
       if(isValidFormRegister()){
           // crear usuario con email y contraseña y añadir un on complete listener
           // para actuar cunado se cree el usuario en la BDD
           FirebaseAuth.getInstance()
                   .createUserWithEmailAndPassword(etRegisterEmail.getText().toString(),
                           etRegisterContra.getText().toString())
                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        showHome();
                    }else{
                        showRegiserError(task.getException().getLocalizedMessage());
                    }
               }
           });
       }
    }

    // call back boton de registro
    // comprueba si es valido el formulario
    public void logIn(View v){
        hideErrors();// esconder por si estaba mostrado de antes
        if(isValidFormLogin()){
            // crear usuario con email y contraseña y añadir un on complete listener
            // para actuar cunado se cree el usuario en la BDD
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(etLoginNombreEmail.getText().toString(),
                            etLoginContra.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                showHome();
                            }else{
                                showLoginError(task.getException().getLocalizedMessage());
                            }
                        }
                    });
        }
    }

    //===============================================================================
    // LOGICA VISUAL
    //===============================================================================
    private void showHome(){
        Intent intentHome = new Intent(this, HomeActivity.class);
        startActivity(intentHome);
        finish();
    }

    private void showRegiserError(String error){
        TextView tv = findViewById(R.id.tvRegisterError);
        tv.setVisibility(View.VISIBLE);
        tv.setText(error);
    }
    private void showLoginError(String error){
        TextView tv = findViewById(R.id.tvRegisterError);
        tv.setVisibility(View.VISIBLE);
        tv.setText(error);
    }
    private void hideErrors(){
        TextView tvR = findViewById(R.id.tvRegisterError);
        TextView tvL = findViewById(R.id.tvLoginError);
        tvR.setVisibility(View.GONE);
        tvL.setVisibility(View.GONE);
    }


    //================================================================================
    // AUTH LOGICA
    //================================================================================
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


        outState.putString("login-correo",etLoginNombreEmail.getText().toString());
        outState.putString("login-contra",etLoginContra.getText().toString());

    }

    // recuperar estado
    private void recueprarEstadoSiEsPosible(Bundle savedInstanceState) {

        if(savedInstanceState!=null){
            etRegisterNombre.setText(savedInstanceState.getString("registro-nombre",""));
            etRegisterEmail.setText(savedInstanceState.getString("registro-correo",""));
            etRegisterContra.setText(savedInstanceState.getString("registro-contra",""));
            etRegisterRepetirContra.setText(savedInstanceState.getString("registro-repetir-contra",""));

            etLoginContra.setText(savedInstanceState.getString("login-contra",""));
            etLoginNombreEmail.setText(savedInstanceState.getString("login-correo",""));

        }

    }


}