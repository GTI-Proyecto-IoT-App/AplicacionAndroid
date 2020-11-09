package com.example.androidappgestionbasura.presentacion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoDispositivo;
import com.example.androidappgestionbasura.model.TipoDispositivo;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivos;
import com.example.androidappgestionbasura.utility.AppConf;
import com.example.androidappgestionbasura.model.InterfaceDispositivos;
import com.example.androidappgestionbasura.utility.Constantes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MisDispositivos extends AppCompatActivity  {
    private InterfaceDispositivos interfaceDispositivos;
    private CasosUsoDispositivo usoDispositivo;
    private RecyclerView recyclerView;
    public AdaptadorDispositivos adaptador;
    private TextView emptyView;
    private final int codigoRespuestaCreacionDispositivo = 1234;
    private final int codigoRespuestaEdicionDispositivo = 4321;
    private AdaptadorDispositivos.RecyclerViewClickListener listener;
    public static TextView textViewResult;
    FloatingActionButton buttonQR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        interfaceDispositivos = ((AppConf) getApplication()).interfaceDispositivos;
        usoDispositivo = new CasosUsoDispositivo(this, interfaceDispositivos);
        setContentView(R.layout.mis_dispositivos);
        adaptador = ((AppConf) getApplication()).adaptador;
        textViewResult = (TextView)findViewById(R.id.textViewResult);
        buttonQR = (FloatingActionButton)findViewById(R.id.buttonQR);
        recyclerView = findViewById(R.id.recyclerview_mis_dispositivos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adaptador);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarVistaDispositivos(v);
            }
        });
        emptyView = findViewById(R.id.textviewrecyclervacio);

        comprobarVaciadoDispositivos();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(codigoRespuestaCreacionDispositivo == requestCode && resultCode == RESULT_OK){

            int a = data.getIntExtra("Dispositivo creado",0);
            Log.d("CREAR DISP", ""+a);
            adaptador.notifyItemInserted(a);
        }else if (codigoRespuestaEdicionDispositivo == requestCode && resultCode == Constantes.RESULT_RECYCLER_VIEW_BORRAR){
            int a = data.getIntExtra("Dispositivo a borrar",0);
            adaptador.notifyItemRemoved(a);
            comprobarVaciadoDispositivos();
        }else if (codigoRespuestaEdicionDispositivo == requestCode && resultCode == Constantes.RESULT_RECYCLER_VIEW_EDITAR){
            int a = data.getIntExtra("Dispositivo a editar",0);
            adaptador.notifyItemChanged(a);
        }
    }

    public void comprobarVaciadoDispositivos() {

        if (interfaceDispositivos.tamaño() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }


    public void lanzarVistaDispositivos(View view) {
        usoDispositivo.mostrar(recyclerView.getChildAdapterPosition(view),codigoRespuestaEdicionDispositivo);
    }

    public void addDipositvo(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Vincular dispositivo")
                .setMessage("Escanea su código QR")

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        startActivity(new Intent(getApplicationContext(),ScanCodeActivity.class));

                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }





}