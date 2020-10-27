package com.example.androidappgestionbasura.presentacion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.androidappgestionbasura.R;

public class RatailerStartUpScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratailer_start_up_screen);

        //la orientacion siempre será vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //les añadimos listeners a los botones

        //declaramos los botones para lanzar las actividades
        Button btnLanzarLogin = findViewById(R.id.iniciarSesionButton);
        btnLanzarLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarActividadLogin(null);
            }
        });

        Button btnLanzarRegistro = findViewById(R.id.registrarseButton);
        btnLanzarRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarActividadRegistro(null);
            }
        });


    }

    /**
     * @author Sergi Sirvent
     * METODO PARA LANZAR LA ACTIVIDAD LOGIN
     **/
    public void lanzarActividadLogin(View view) {
        Intent i = new Intent(this, AuthActivity.class);

        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View,String>(findViewById(R.id.iniciarSesionButton),"transicion_login");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RatailerStartUpScreenActivity.this,pairs);

        startActivity(i,options.toBundle());
    }

    /**
     * @author Sergi Sirvent
     * METODO PARA LANZAR LA ACTIVIDAD REGISTRO
     **/
    public void lanzarActividadRegistro(View view) {
        Intent i = new Intent(this, RegisterActivity.class);

        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View,String>(findViewById(R.id.registrarseButton),"transicion_register");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RatailerStartUpScreenActivity.this,pairs);

        startActivity(i,options.toBundle());
    }


}