package com.example.androidappgestionbasura.presentacion;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoDispositivo;
import com.example.androidappgestionbasura.model.Dispositivos;
import com.example.androidappgestionbasura.model.Disp;
import com.example.androidappgestionbasura.utility.AppConf;

public class DispositivoDetallesActivity extends AppCompatActivity {
    private Dispositivos dispositivos;
    private CasosUsoDispositivo usoDispositivo;
    private int pos;
    private Disp disp;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispositivo_lista);
        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos", 0);
        dispositivos = ((AppConf) getApplication()).dispositivos;
        usoDispositivo = new CasosUsoDispositivo(this, dispositivos);

        actualizaVistas();
    }

    public void actualizaVistas() {
        TextView nombre = findViewById(R.id.nombre);
        nombre.setText(disp.getNombre());
        ImageView logo_tipo = findViewById(R.id.foto);
        logo_tipo.setImageResource(disp.getTipo().getRecurso());
        TextView descripcion = findViewById(R.id.descripcion);
        descripcion.setText(disp.getDescripcion());



    }
}
