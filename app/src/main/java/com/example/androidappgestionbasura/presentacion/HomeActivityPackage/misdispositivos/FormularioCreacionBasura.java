package com.example.androidappgestionbasura.presentacion.HomeActivityPackage.misdispositivos;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoDispositivo;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.InterfaceDispositivos;
import com.example.androidappgestionbasura.model.TipoDispositivo;
import com.example.androidappgestionbasura.utility.AppConf;
import com.example.androidappgestionbasura.utility.Utility;

public class FormularioCreacionBasura extends AppCompatActivity {
    private EditText nombre;
    private EditText descripcion;
    private EditText numero;

    private CasosUsoDispositivo usoDispositivo;
    private CasosUsoUsuario usoUsuario;
    private int pos;
    private Dispositivo dispositivo;
    private  boolean edicion;//si viene de edicion sera true

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_creacion_basura);
        nombre = findViewById(R.id.editTextNombreBasura);
        descripcion = findViewById(R.id.editTextDescripcionBasura);
        numero = findViewById(R.id.editTextNumeroPersonasBasura);

        Bundle extras = getIntent().getExtras();
        InterfaceDispositivos listaDispositivos = ((AppConf) getApplication()).listaDispositivos;
        usoDispositivo = new CasosUsoDispositivo(this, listaDispositivos);
        usoUsuario = new CasosUsoUsuario(this);

        if(extras != null){
            pos = extras.getInt("pos", -1);
            if (pos==-1){
                Log.i("INFO-JC", "Creando dispositvo");
                setTitle(getString(R.string.tituloFormularioAddBasura));
                dispositivo = new Dispositivo();
                dispositivo.setId(extras.getString("idDispositivo"));
            }else{
                edicion = true;
                setTitle(getString(R.string.editar));
                dispositivo = listaDispositivos.elemento(pos);
                actualizaVistas();
            }
        }else{
            Log.i("INFO-JC", "NO se han encotrado extras");
        }
        recueprarEstadoSiEsPosible(savedInstanceState);
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
            dispositivo.setNombre(nombre.getText().toString().trim());
            dispositivo.setDescripcion(descripcion.getText().toString().trim());
            dispositivo.setTipo(TipoDispositivo.BASURA);
            dispositivo.setNumeroPersonasUso(Integer.parseInt(numero.getText().toString()));

            if (edicion){
                usoDispositivo.guardar(pos, dispositivo);
                setResult(RESULT_OK);
                finish(); // va a disp detalles
            }else{

                Intent intent = new Intent();
                dispositivo.getUsuariosVinculados().add(usoUsuario.getUsuario().getUid());
                intent.putExtra("Dispositivo creado",usoDispositivo.add(dispositivo));

                setResult(RESULT_OK,intent);
                finish();//va a mis dispositivos
            }


        }

    }



    private boolean validarForm() {
        boolean isValid = true;

        String nombreBasura = nombre.getText().toString().trim();
        String numeroBasura = numero.getText().toString();

        if(nombreBasura.length()==0){
            isValid = false;
            Utility.setError(this,getString(R.string.error_campo_vacio),nombre);
        }
         if(numeroBasura.length()==0){
            isValid = false;
             Utility.setError(this,getString(R.string.error_campo_vacio),numero);

        }else if(Integer.parseInt(numeroBasura)==0){
             isValid = false;
             Utility.setError(this,getString(R.string.error_creacion_basura_numero_no_cero),numero);
         }

        return isValid;

    }

    public void lanzarCancelar(View view){
        finish();
    }



    // guardar estado
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString("dispositivo-nombre",nombre.getText().toString());
        outState.putString("dispositivo-desc",descripcion.getText().toString());
        outState.putString("dispositivo-num",numero.getText().toString());

    }

    // recuperar estado
    private void recueprarEstadoSiEsPosible(Bundle savedInstanceState) {

        if(savedInstanceState!=null){
            nombre.setText(savedInstanceState.getString("dispositivo-nombre",""));
            descripcion.setText(savedInstanceState.getString("dispositivo-desc",""));
            numero.setText(savedInstanceState.getString("dispositivo-num",""));

        }

    }
}
