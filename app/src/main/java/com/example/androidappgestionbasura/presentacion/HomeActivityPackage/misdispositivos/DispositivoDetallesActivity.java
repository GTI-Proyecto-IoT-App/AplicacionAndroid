package com.example.androidappgestionbasura.presentacion.HomeActivityPackage.misdispositivos;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoDispositivo;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.example.androidappgestionbasura.model.InterfaceDispositivos;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivosFirestoreUI;
import com.example.androidappgestionbasura.utility.AppConf;

import static com.example.androidappgestionbasura.utility.Constantes.RESULT_RECYCLER_VIEW_EDITAR;

public class DispositivoDetallesActivity extends AppCompatActivity {
    private AdaptadorDispositivosFirestoreUI adaptador;
    private CasosUsoDispositivo usoDispositivo;
    private Usuario usuario;
    private int pos;
    private Dispositivo dispositivo;
    private final int codigoRespuestaEdicionDispositivo = 7777;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.info_dispositivos);
        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos", 0);
        adaptador = ((AppConf) getApplication()).adaptador;
        usoDispositivo = new CasosUsoDispositivo(this);
        dispositivo =adaptador.getItem(pos);
        usuario =((AppConf) getApplication()).getUsuario();
        ActionBar actionBar = getSupportActionBar();// poner el boton de volver atrás
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);

        actualizaVistas();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && requestCode==codigoRespuestaEdicionDispositivo){

            Intent intent = new Intent();
            intent.putExtra("Dispositivo a editar",pos);
            setResult(RESULT_RECYCLER_VIEW_EDITAR, intent);
            dispositivo = adaptador.getItem(pos);
            findViewById(R.id.constraintDetallesDisp).invalidate();
            actualizaVistas();
        }
    }

    public void actualizaVistas() {

        setTitle(dispositivo.getNombre());
        ImageView logo_tipo = findViewById(R.id.foto);
        logo_tipo.setImageResource(dispositivo.getTipo().getRecurso());
        TextView descripcion = findViewById(R.id.descripcion);
        descripcion.setText(dispositivo.getDescripcion());



    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalles_dispositivos, menu);
        return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.accion_editar:
                usoDispositivo.editar(pos, codigoRespuestaEdicionDispositivo);
                return true;
            case R.id.accion_borrar:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.confirmar_borrado_titulo)
                        .setMessage(R.string.confirmar_borrado_mensaje)

                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                usoDispositivo.borrar(pos, usuario.getUid());

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();

                return true;

            case android.R.id.home://boton de volver a atras
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
