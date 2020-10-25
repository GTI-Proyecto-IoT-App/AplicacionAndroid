package com.example.androidappgestionbasura.presentacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.androidappgestionbasura.R;

public class RatailerStartUpScreenActivity extends AppCompatActivity {

    //declaramos los botones para lanzar las actividades
    private Button btnLanzarLogin;
    private Button btnLanzarRegistro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratailer_start_up_screen);

        //les añadimos listeners a los botones

        btnLanzarLogin = findViewById(R.id.iniciarSesionButton);
        btnLanzarLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarActividadLogin(null);
            }
        });

        btnLanzarRegistro = findViewById(R.id.registrarseButton);
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


}