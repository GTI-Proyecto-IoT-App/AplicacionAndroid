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


/**
 * En la splash activity ocurre el enrutamiento principal de la aplicación
 *
 * Si existe un usuario logeado y esta guardado en cache -> home activity
 * Si existe un usuario logeado y no esta guardado en cache, se pedira al servidor -> home activity
 * Si no existe un usuario logeado a auth acitivty
 *
 * Si el usuario que llega del servido o guardado en cache no esta verificado -> VerifyActivity
 *
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final CasosUsoUsuario casosUsoUsuario = new CasosUsoUsuario(this);
        // comprobamos si tenemos guarda una UID en preferences
        if(casosUsoUsuario.isUsuarioLogeado()){
            // comprobamos si tenemos el usuario guardado en cache
            if(casosUsoUsuario.getUsuario()==null){
                setContentView(R.layout.splash_background);
                // obtener usuario de la base de datos
                casosUsoUsuario.getUsuarioSiExisteSinoPedirlo(new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        casosUsoUsuario.guardarUidUsuario((Usuario) object);
                        if(((Usuario) object).isEmailVerified()){
                            casosUsoUsuario.showHome(false);
                        }else{
                            casosUsoUsuario.showVerifyActivity();
                        }
                    }
                    @Override
                    public void onError(Object object) {
                        if(object == null){
                            // no existe el usuario y volvemos al login
                            casosUsoUsuario.showAuthActivity();
                        }else{
                            // lo mas probable es que no hay conexion
                            //mostramos error inesperado y que pruebe mas tarde
                            casosUsoUsuario.showHome(true);
                        }

                    }
                });
            }
            else if(!casosUsoUsuario.getUsuario().isEmailVerified()){// en el caso de que si tenemos el usuario comprobar si esta verficiado
                //  hay usuario pero no esta verificado
                casosUsoUsuario.showVerifyActivity();
            }
            else{
                // hay usuario y esta verficado
                casosUsoUsuario.showHome(false);
            }

        }
        else{
            // si no hay nos movemos a la auth activity
            casosUsoUsuario.showAuthActivity();
        }

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }

}