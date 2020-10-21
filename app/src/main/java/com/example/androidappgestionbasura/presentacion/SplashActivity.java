package com.example.androidappgestionbasura.presentacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;

public class SplashActivity extends AppCompatActivity {


    private CasosUsoUsuario casosUsoUsuario;


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ONSTART", "SPLASH");
    }

    @Override
    protected void onDestroy() {
        Log.d("ONSTART", "SPLASH MUERE");
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        casosUsoUsuario = new CasosUsoUsuario(this);
        setVisible(false);
        if(casosUsoUsuario.isUsuarioLogeado()){
            // si ya estaba logeado debemos obtener el usuario
            casosUsoUsuario.getUsuarioSiExisteSinoPedirlo(new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    casosUsoUsuario.showHome(false);

                }
                @Override
                public void onError(Object object) {
                    casosUsoUsuario.showHome(true);
                }
            });

        }
        else{
            SystemClock.sleep(4000);
            // tenemos que ir a la login pero tardamos unos segundos
            casosUsoUsuario.showAuthActivity();
        }
        SystemClock.sleep(4000);
    }


}