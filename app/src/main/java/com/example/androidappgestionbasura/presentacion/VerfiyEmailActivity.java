package com.example.androidappgestionbasura.presentacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

        //full window para la acividad
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //la orientacion siempre será vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
                    showAviso(getString(R.string.message_correct_correo_verificacion_enviado)+casosUsoUsuario.getUsuario().getEmail());
                }else{
                    showAviso(getString(R.string.error_correo_verificacion_enviado)+casosUsoUsuario.getUsuario().getEmail());
                }

            }
        });

        // check email
        btnCheckEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(true);
                casosUsoUsuario.checkIsEmailVerifiedAndVerifyIt(new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        showLoading(false);
                        casosUsoUsuario.showHome(false);
                    }

                    @Override
                    public void onError(Object object) {
                        showLoading(false);
                        if(object!=null){
                            if(object.equals(Constant.FAIL)){
                                // el usuario no exite
                               showAviso(getString(R.string.error_verificar_usuario_no_existe)+casosUsoUsuario.getUsuario().getEmail());

                            }else if(object.equals(Constant.CONNECTION_ERROR)){

                                showAviso(getString(R.string.error_de_conexion));
                            }else{
                                showAviso(getString(R.string.error_inesperado));
                            }



                        }else{
                            String strError = getString(R.string.error_email_no_verficado);
                            strError = strError.replace("$",casosUsoUsuario.getUsuario().getEmail());
                            tvInfo.setText(strError);
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

    private void showAviso(String error){
        tvInfo.setText(error);
    }
    private void showLoading(boolean show){
        if(show){
            loadingDialogActivity.startLoadingDialog();
        }else{
            loadingDialogActivity.dismissDialog();
        }
    }

}