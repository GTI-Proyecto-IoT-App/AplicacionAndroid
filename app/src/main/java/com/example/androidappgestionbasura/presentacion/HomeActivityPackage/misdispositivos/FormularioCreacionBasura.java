package com.example.androidappgestionbasura.presentacion.HomeActivityPackage.misdispositivos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoDispositivo;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.InterfaceDispositivos;
import com.example.androidappgestionbasura.model.TipoDispositivo;
import com.example.androidappgestionbasura.utility.AppConf;

public class FormularioCreacionBasura extends AppCompatActivity {
    private EditText nombre;
    private EditText descripcion;
    private EditText numero;
    private InterfaceDispositivos interfaceDispositivos;
    private CasosUsoDispositivo usoDispositivo;
    private int pos;
    private Dispositivo dispositivo;
    private  boolean edicion;//si viene de edicion sera true

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_creacion_basura);
        Bundle extras = getIntent().getExtras();
        interfaceDispositivos = ((AppConf) getApplication()).listaDispositivos;
        usoDispositivo = new CasosUsoDispositivo(this, interfaceDispositivos);

        if(extras != null){

            pos = extras.getInt("pos", 0);
            edicion = true;
            dispositivo = interfaceDispositivos.elemento(pos);
            actualizaVistas();

        }

    }

    public void actualizaVistas() {
        nombre = findViewById(R.id.editTextNombreBasura);
        nombre.setText(dispositivo.getNombre());

        descripcion = findViewById(R.id.editTextDescripcionBasura);
        descripcion.setText(dispositivo.getDescripcion());

        numero = findViewById(R.id.editTextNumeroPersonasBasura);
        numero.setText(String.valueOf(dispositivo.getNumeroPersonasUso()));


    }

    public void lanzarAceptar(View view){
        if(validarForm()){

            Dispositivo dip = new Dispositivo(
                    ",",
                    nombre.getText().toString().trim(),
                    descripcion.getText().toString().trim(),
                    TipoDispositivo.BASURA,
                    Integer.parseInt(numero.getText().toString())
            );

            if (edicion){
                usoDispositivo.guardar(pos, dip);
                setResult(RESULT_OK);
                finish(); // va a disp detalles
            }else{

                Intent intent = new Intent();
                intent.putExtra("Dispositivo creado",usoDispositivo.add(dip));

                setResult(RESULT_OK,intent);
                finish();//va a mis dispositivos
            }


        }

    }



    private boolean validarForm() {
        boolean isValid = true;
        nombre = findViewById(R.id.editTextNombreBasura);
        descripcion = findViewById(R.id.editTextDescripcionBasura);
        numero = findViewById(R.id.editTextNumeroPersonasBasura);

        String nombreBasura = nombre.getText().toString().trim();
        String descripcionBasura = descripcion.getText().toString().trim();
        String numeroBasura = numero.getText().toString();

        if(nombreBasura.length()==0){
            isValid = false;
            nombre.setError("Este campo no puede estar vacío");
        }
         if(numeroBasura.length()==0){
            isValid = false;
            numero.setError(getString(R.string.error_campo_vacio));

        }

        return isValid;

    }

    public void lanzarCancelar(View view){
        finish();
    }
}
