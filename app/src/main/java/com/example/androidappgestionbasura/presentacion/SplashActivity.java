package com.example.androidappgestionbasura.presentacion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.datos.firebase.constants.Constant;
import com.example.androidappgestionbasura.model.Usuario;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        //TODO al ser asyn task el metodo pasa por el onStart y muestra una ventana blanca
        final CasosUsoUsuario casosUsoUsuario = new CasosUsoUsuario(this);
        if(casosUsoUsuario.isUsuarioLogeado()){
            // si ya estaba logeado debemos obtener el usuario
            casosUsoUsuario.getUsuarioSiExisteSinoPedirlo(new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    casosUsoUsuario.usuarioAccedeCorrectamente((Usuario) object);

                }
                @Override
                public void onError(Object object) {
                    casosUsoUsuario.showHome(true);
                }
            });

        }else{
            Intent intent = new Intent(this,AuthActivity.class);
            startActivity(intent);
            finish();
        }




    }


}