package com.example.androidappgestionbasura.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoDispositivo;
import com.example.androidappgestionbasura.model.InterfaceDispositivos;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.utility.AppConf;

import static com.example.androidappgestionbasura.utility.Constantes.RESULT_RECYCLER_VIEW_EDITAR;

public class DispositivoDetallesActivity extends AppCompatActivity {
    private InterfaceDispositivos interfaceDispositivos;
    private CasosUsoDispositivo usoDispositivo;
    private int pos;
    private Dispositivo dispositivo;
    private final int codigoRespuestaEdicionDispositivo = 7777;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.info_dispositivos);
        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos", 0);
        interfaceDispositivos = ((AppConf) getApplication()).interfaceDispositivos;
        usoDispositivo = new CasosUsoDispositivo(this, interfaceDispositivos);
        dispositivo = interfaceDispositivos.elemento(pos);
        actualizaVistas();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==codigoRespuestaEdicionDispositivo){

            Intent intent = new Intent();
            intent.putExtra("Dispositivo a editar",pos);
            setResult(RESULT_RECYCLER_VIEW_EDITAR, intent);
            dispositivo = interfaceDispositivos.elemento(pos);
           //Log.i("testAA", dispositivo.getNombre());
            findViewById(R.id.constraintDetallesDisp).invalidate();
            actualizaVistas();
        }
    }

    public void actualizaVistas() {
        TextView nombre = findViewById(R.id.nombre);
        nombre.setText(dispositivo.getNombre());
        ImageView logo_tipo = findViewById(R.id.foto);
        logo_tipo.setImageResource(dispositivo.getTipo().getRecurso());
        TextView descripcion = findViewById(R.id.descripcion);
        descripcion.setText(dispositivo.getDescripcion());
        TextView numero = findViewById(R.id.textViewNumeroPersonas);
        numero.setText(String.valueOf(dispositivo.getNumeroPersonasUso()));



    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalles, menu);
        return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.accion_editar:
                usoDispositivo.editar(pos, codigoRespuestaEdicionDispositivo);
                return true;
            case R.id.accion_borrar:
                usoDispositivo.borrar(pos);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
