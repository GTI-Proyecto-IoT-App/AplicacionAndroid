package com.example.androidappgestionbasura.presentacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.Usuario;

public class HomeActivity extends AppCompatActivity {

    private CasosUsoUsuario casosUsoUsuario;
    private LoadingDialogActivity loadingDialogActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        casosUsoUsuario = new CasosUsoUsuario(this);
        loadingDialogActivity = new LoadingDialogActivity(this);

        setUp();




    }

    private void setUp() {
        if(getIntent().getBooleanExtra("error",false)){
            // volver a auth activity
            // se borro el usuario lo mas probable
            casosUsoUsuario.showAuthActivity();
        }else{
            // ya tenemos el usuario
            Usuario usuario = casosUsoUsuario.getUsuario();
            TextView textView = findViewById(R.id.tvHomePrueba);

            String strWelcome = getString(R.string.bienvenido) + ", "+ usuario.getName();
            textView.setText(strWelcome);

        }
    }

    public void cerrar_sesion(View v){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.cerrar_sesion))
                .setMessage("¿Desas cerrar sesión?")
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        casosUsoUsuario.cerrarSesion();
                    }
                })
                .setNegativeButton(getString(android.R.string.cancel),null)
        .show();
    }

}