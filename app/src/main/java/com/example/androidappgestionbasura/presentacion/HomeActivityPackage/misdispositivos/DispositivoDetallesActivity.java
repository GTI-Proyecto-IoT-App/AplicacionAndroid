package com.example.androidappgestionbasura.presentacion.HomeActivityPackage.misdispositivos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoDispositivo;
import com.example.androidappgestionbasura.casos_uso.CasosUsoMesuras;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.InterfaceDispositivos;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.model.mesuras_dispositivos.ListaMesuras;
import com.example.androidappgestionbasura.model.mesuras_dispositivos.Mesura;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivosFirestoreUI;
import com.example.androidappgestionbasura.utility.AppConf;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.androidappgestionbasura.utility.Constantes.RESULT_RECYCLER_VIEW_EDITAR;

public class DispositivoDetallesActivity extends AppCompatActivity {

    private AdaptadorDispositivosFirestoreUI adaptador;
    private CasosUsoDispositivo usoDispositivo;
    private CasosUsoMesuras usoMesuras;
    private ListaMesuras listaMesuras;
    private Usuario usuario;
    private int pos;
    private Dispositivo dispositivo;
    private final int codigoRespuestaEdicionDispositivo = 7777;

    //Graficas circulares

    PieChart pieChartOrganico;
    PieChart pieChartPlastico;
    PieChart pieChartVidrio;
    PieChart pieChartPapel;



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

        //buscamos las vistas de las graficas circulares

        pieChartOrganico = findViewById(R.id.pieChartOrganico);
        pieChartPapel = findViewById(R.id.pieChartPapel);
        pieChartPlastico = findViewById(R.id.pieChartPlastico);
        pieChartVidrio = findViewById(R.id.pieChartVidrio);

        usoMesuras = new CasosUsoMesuras(this);
        usoMesuras.getMesurasPorId(new CallBack() {
            @Override
            public void onSuccess(Object object) {

                listaMesuras = (ListaMesuras) object;
                Mesura mesuraFinalOrganico = new Mesura();
                Mesura mesuraFinalPapel = new Mesura();
                Mesura mesuraFinalPlastico = new Mesura();
                Mesura mesuraFinalVidrio = new Mesura();

                long tiempoUltimo = 0;//si las medidas superan a este tiempo seran consideradas mas recientes

                for(Mesura mesura : listaMesuras.getMesuras()){

                    if (mesura.getTipoMedida().equals("organico")){

                        if (mesura.getUnixTime() > tiempoUltimo){
                             mesuraFinalOrganico = mesura;

                        }

                    }
                    if (mesura.getTipoMedida().equals("plastico")){

                        if (mesura.getUnixTime() > tiempoUltimo){
                            mesuraFinalPlastico = mesura;
                        }

                    }
                    if (mesura.getTipoMedida().equals("vidrio")){

                        if (mesura.getUnixTime() > tiempoUltimo){
                            mesuraFinalVidrio = mesura;
                        }

                    }
                    if (mesura.getTipoMedida().equals("papel")){

                        if (mesura.getUnixTime() > tiempoUltimo){
                            mesuraFinalPapel = mesura;
                        }

                    }


//                    Log.d("DatoMesura----LLenado","" + mesura.getLlenado());
//                    Log.d("DatoMesura----Tipo","" + mesura.getTipoMedida());
//                    Log.d("DatoMesura----Peso","" + mesura.getPeso());
//                    Log.d("DatoMesura----UnixTime","" + mesura.getUnixTime());

                }

//                Log.d("TAG",mesuraFinalOrganico.getLlenado()+ "");
//                Log.d("TAG",mesuraFinalOrganico.getTipoMedida()+ "");
//                Log.d("TAG",mesuraFinalOrganico.getUnixTime()+ "");
//                Log.d("TAG",mesuraFinalOrganico.getPeso()+ "");



               crearGraficaPie(mesuraFinalOrganico.getTipoMedida(),mesuraFinalOrganico.getLlenado(),pieChartOrganico);
               crearGraficaPie(mesuraFinalPlastico.getTipoMedida(),mesuraFinalPlastico.getLlenado(),pieChartPlastico);
               crearGraficaPie(mesuraFinalPapel.getTipoMedida(),mesuraFinalPapel.getLlenado(),pieChartPapel);
               crearGraficaPie(mesuraFinalVidrio.getTipoMedida(),mesuraFinalVidrio.getLlenado(),pieChartVidrio);

            }

            @Override
            public void onError(Object object) {

                Toast toast = Toast.makeText(getApplicationContext(), "No se ha podido cargar correctamente la lista de medidas", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

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

    /**
     * Metodo que recibe los datos procedentes de Firebase y crea graficas tipo PIE en el layout info_dispositivos
     * Autor : Sergi Sirvent Sempere
     **/
    private void crearGraficaPie(String tipoBasura , double ultimaMedida, PieChart graficaCircular){

        //definimos la lista de entradas a la grafica
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        //definimos el label de la grafica
        String label = tipoBasura;

        //inicializar datos
        Map<String, Integer> listaCantidades = new HashMap<>();
        listaCantidades.put(tipoBasura,(int)ultimaMedida);

        //inicializar sector vacio
        listaCantidades.put("acio",100-(int)ultimaMedida);



        //Inicializamos los colores para las entradas
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));

        //Metemos los datos en pieEntries
        for(String type: listaCantidades.keySet()){
            pieEntries.add(new PieEntry(listaCantidades.get(type).floatValue(), "%"));
        }

        //recogemos los datos que coincidan con el nombre de la label
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);

        //tamaño de texto
        pieDataSet.setValueTextSize(12f);

        //indicamos el color de las diferentes entradas
        pieDataSet.setColors(colors);

        //agrupamos los datos de las entradas en la grafica
        PieData pieData = new PieData(pieDataSet);

        //mostramos el valor de las entradas en la grafica
        pieData.setDrawValues(true);

        //adjudicamos los datos a la grafica
        graficaCircular.setData(pieData);

        //desactivamos la leyenda
        Legend l = graficaCircular.getLegend();
        l.setEnabled(false);

        //la invalidamos
        graficaCircular.invalidate();
    }
}
