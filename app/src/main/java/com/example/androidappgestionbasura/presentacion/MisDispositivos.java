package com.example.androidappgestionbasura.presentacion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoDispositivo;
import com.example.androidappgestionbasura.model.TipoDispositivo;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivos;
import com.example.androidappgestionbasura.utility.AppConf;
import com.example.androidappgestionbasura.model.Dispositivos;



public class MisDispositivos extends AppCompatActivity {
        private Dispositivos dispositivos;
        private CasosUsoDispositivo usoDispositivo;
        private RecyclerView recyclerView;
        public AdaptadorDispositivos adaptador;
        private TextView emptyView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            dispositivos = ((AppConf) getApplication()).dispositivos;
            usoDispositivo = new CasosUsoDispositivo(this, dispositivos);
            setContentView(R.layout.mis_dispositivos);
            adaptador = ((AppConf) getApplication()).adaptador;
            recyclerView = findViewById(R.id.recyclerview_mis_dispositivos);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adaptador);
            emptyView = findViewById(R.id.textviewrecyclervacio);

            comprobarVaciadoDispositivos();


        }
        public void comprobarVaciadoDispositivos(){

            if(dispositivos.tamaño()== 0){
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
        }


        public void lanzarVistaDispositivos(View view) {
            usoDispositivo.mostrar(0);
        }

        public void addDipositvo(View view) {
            new AlertDialog.Builder(this)
                    .setTitle("Vincular dispositivo")
                    .setMessage("Escanea su código QR")

                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                           usoDispositivo.crear(TipoDispositivo.BASURA);

                        }})
                    .setNegativeButton("Cancelar", null)
                    .show();
        }



       /* @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_scrolling, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.menu_buscar) {
                lanzarVistaDispositivos(null);
                return true;
            }

            //noinspection SimplifiableIfStatement

        }*/
    }