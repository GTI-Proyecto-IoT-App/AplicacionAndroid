package com.example.androidappgestionbasura.presentacion;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.datos.firebase.constants.Constant;

/**
 * @author Ruben Pardo Casanova
 * Actividad que consta de tres botones:
 *  - Send email de verificacion
 *  - Comprobar si estas verficado
 *  - volver al login
 */
public class VerfiyEmailActivity extends AppCompatActivity {


    private CasosUsoUsuario casosUsoUsuario;
    private LoadingDialogActivity loadingDialogActivity;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfiy_email);

        tvInfo = findViewById(R.id.tvVerificarError);
        casosUsoUsuario = new CasosUsoUsuario(this);
        loadingDialogActivity = new LoadingDialogActivity(this);


        Button btnSendEmail = findViewById(R.id.btnSendEmailVerification);
        Button btnCheckEmail = findViewById(R.id.btnCheckEmailVerified);
        Button btnVolverLogin = findViewById(R.id.btnVolverLogin);

        //subrayado del boton de volver al inicio

        btnVolverLogin.setPaintFlags(btnVolverLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // send email verificacion
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(casosUsoUsuario.resendVerificationEmail()){
                    tvInfo.setText("Correo enviado a: "+casosUsoUsuario.getUsuario().getEmail());
                }else{
                    tvInfo.setText("No se pudo enviar el correo a: "+casosUsoUsuario.getUsuario().getEmail());
                }

            }
        });

        // check email
        btnCheckEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EMAILVERIFIED", "pulsa boton");
                showLoading(true);
                casosUsoUsuario.checkIsEmailVerifiedAndVerifyIt(new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        Log.d("EMAILVERIFIED", "VERIFY ACTIVITY succes");
                        showLoading(false);
                        casosUsoUsuario.showHome(false);
                    }

                    @Override
                    public void onError(Object object) {
                        Log.d("EMAILVERIFIED", "VERIFY ACTIVITY ERROR");
                        showLoading(false);
                        if(object!=null){
                            if(object.equals(Constant.FAIL)){
                                // el usuario no exite
                                tvInfo.setText("No existe usuario con email: "+casosUsoUsuario.getUsuario().getEmail());
                            }else{
                                // error en la base de datos
                                tvInfo.setText("Error al verficar el email: "+casosUsoUsuario.getUsuario().getEmail() +" pruebe más tarde");
                            }

                        }else{
                            tvInfo.setText("El email: "+casosUsoUsuario.getUsuario().getEmail() +" no esta verificado, comprueba tu correo. Si no te llegó pulsa en Volver a Enviar");
                        }
                    }
                });
            }
        });

        btnVolverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casosUsoUsuario.cerrarSesion();
            }
        });

    }

    private void showLoading(boolean show){
        if(show){
            loadingDialogActivity.startLoadingDialog();
        }else{
            loadingDialogActivity.dismissDialog();
        }
    }

}