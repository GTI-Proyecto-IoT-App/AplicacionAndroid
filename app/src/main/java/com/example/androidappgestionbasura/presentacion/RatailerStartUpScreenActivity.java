package com.example.androidappgestionbasura.presentacion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.datos.preferences.SharedPreferencesHelper;
import com.example.androidappgestionbasura.presentacion.HomeActivityPackage.HomeActivity;

public class RatailerStartUpScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // antes de nada comprobamos si se ha abierto por una notificacion
        comprobarSiVieneDeNotificacion();


        setContentView(R.layout.activity_ratailer_start_up_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

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

    private void comprobarSiVieneDeNotificacion() {
        // get extras para ver si se abrio desde notificacion
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            // obtenemos la id de la notificacion
            int idNotificacion = extras.getInt(HomeActivity.INTENT_KEY_ABRIR_NOTIFICACIONES,-1);

            if(idNotificacion!=-1){

                // quitamos la notificacion
                NotificationManager notificationManager =
                        (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(idNotificacion);

                String uid = SharedPreferencesHelper.getInstance().getUID();
                // si hay sesion abrir las notificaciones, sino quedarse en la activiy principal
                boolean haySesion = true;
                if(uid==null){
                    haySesion = false;
                }else if(uid.equals("")){
                    haySesion = false;
                }


                if(haySesion){// si hay sesion abrimos la bandeja de notificaciones
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra(HomeActivity.INTENT_KEY_ABRIR_NOTIFICACIONES,true);
                    startActivity(intent);
                    finish();
                }


            }
        }

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