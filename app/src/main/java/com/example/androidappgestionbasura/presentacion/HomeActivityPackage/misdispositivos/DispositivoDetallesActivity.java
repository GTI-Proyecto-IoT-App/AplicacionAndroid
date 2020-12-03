package com.example.androidappgestionbasura.presentacion.HomeActivityPackage.misdispositivos;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.model.mesuras_dispositivos.ListaMesuras;
import com.example.androidappgestionbasura.model.mesuras_dispositivos.Mesura;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivosFirestoreUI;
import com.example.androidappgestionbasura.utility.AppConf;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //Grafica lineal

    LineChart lineChartGeneral;


    //Colores para las graficas

    String colorGraficaOrganica ="#983909";
    String colorGraficaPlastica ="#ECCA0F";
    String colorGraficaPapel ="#1235AF";
    String colorGraficaVidrio ="#359004";
    String colorVacio = "#D3D3D3";



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

        //buscamos las vistas de las graficas circulares y lineal

        pieChartOrganico = findViewById(R.id.pieChartOrganico);
        pieChartPapel = findViewById(R.id.pieChartPapel);
        pieChartPlastico = findViewById(R.id.pieChartPlastico);
        pieChartVidrio = findViewById(R.id.pieChartVidrio);
        lineChartGeneral = findViewById(R.id.lineChart);

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


                }

                //llamamos a los metodos de crear las graficas
               crearGraficaPie(mesuraFinalOrganico.getTipoMedida(),mesuraFinalOrganico.getLlenado(),pieChartOrganico);
               crearGraficaPie(mesuraFinalPlastico.getTipoMedida(),mesuraFinalPlastico.getLlenado(),pieChartPlastico);
               crearGraficaPie(mesuraFinalPapel.getTipoMedida(),mesuraFinalPapel.getLlenado(),pieChartPapel);
               crearGraficaPie(mesuraFinalVidrio.getTipoMedida(),mesuraFinalVidrio.getLlenado(),pieChartVidrio);

               crearGraficaLineal(listaMesuras.getMesuras(),lineChartGeneral);



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
        int numeroVacio = 100-(int)ultimaMedida;
        listaCantidades.put("Vacio",numeroVacio);

        //Metemos los datos en pieEntries
        for(String type: listaCantidades.keySet()){
            pieEntries.add(new PieEntry(listaCantidades.get(type).floatValue(), ""));
        }

        //recogemos los datos que coincidan con el nombre de la label
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);

        //tamaño de texto
        pieDataSet.setValueTextSize(0);

        //indicamos el color de las diferentes entradas en funcion del tipo de basura

        ArrayList<Integer> colors = setearColoresPieChart(graficaCircular,tipoBasura);
        pieDataSet.setColors(colors);

        //agrupamos los datos de las entradas en la grafica
        PieData pieData = new PieData(pieDataSet);

        //mostramos el valor de las entradas en la grafica
        pieData.setDrawValues(true);

        //adjudicamos los datos a la grafica
        graficaCircular.setData(pieData);

        //ponemos el texto central dentro de la grafica

        graficaCircular.setCenterText(String.valueOf(ultimaMedida+"%"));

        //desactivamos la leyenda
        Legend l = graficaCircular.getLegend();
        graficaCircular.getDescription().setText("");
        l.setEnabled(false);

        //adecuamos el grosor de la linea de la grafica

        graficaCircular.setHoleRadius(80);

        //animamos la grafica
        graficaCircular.animateX(2000); // animar milisegundos horizaontales

        graficaCircular.animateY(2000); // animar milisegundos verticales

        graficaCircular.animateXY(2000, 2000); // animar milisegundos horizaontales y verticales

        //la invalidamos
        graficaCircular.invalidate();


    }

    /**
     *
     * Metodo para asignar los colores a las graficas circulares
     */
    ArrayList<Integer> setearColoresPieChart(PieChart grafica , String tipoBasura){

        ArrayList<Integer> listaFinal = new ArrayList<>();

        if(tipoBasura.equals("organico")){

            listaFinal.add(Color.parseColor(colorGraficaOrganica));
            listaFinal.add(Color.parseColor(colorVacio));


        }else if(tipoBasura.equals("papel")){

            listaFinal.add(Color.parseColor(colorVacio));
            listaFinal.add(Color.parseColor(colorGraficaPapel));

        }else if(tipoBasura.equals("plastico")){

            listaFinal.add(Color.parseColor(colorVacio));
            listaFinal.add(Color.parseColor(colorGraficaPlastica));

        }else if(tipoBasura.equals("vidrio")){

            listaFinal.add(Color.parseColor(colorGraficaVidrio));
            listaFinal.add(Color.parseColor(colorVacio));


        }

        return listaFinal;
    }


    /**
     *
     * Metodo para crear la lineal
     * Autor : Sergi Sirvent Sempere
     *Recibe una lista de mesuras y la view de una grafica lineal y devuelve void
     */
    public void crearGraficaLineal(List<Mesura> listaMesuras , LineChart graficaLineal){

        //declaramos los dataSets
        LineDataSet dataSetOrganico = new LineDataSet(llenarDataSets(separarMesuras("organico",listaMesuras)),"Organico");
        LineDataSet dataSetPlastico = new LineDataSet(llenarDataSets(separarMesuras("plastico",listaMesuras)),"Plastico");
        LineDataSet dataSetVidrio = new LineDataSet(llenarDataSets(separarMesuras("vidrio",listaMesuras)),"Vidrio");
        LineDataSet dataSetPapel = new LineDataSet(llenarDataSets(separarMesuras("papel",listaMesuras)),"Papel");

        //personalizamos los dataset

        personalizarDataset("organico",dataSetOrganico);
        personalizarDataset("plastico",dataSetPlastico);
        personalizarDataset("vidrio",dataSetVidrio);
        personalizarDataset("papel",dataSetPapel);



        //array de datasets

        ArrayList<ILineDataSet>dataSets = new ArrayList<>();
        dataSets.add(dataSetOrganico);
        dataSets.add(dataSetPlastico);
        dataSets.add(dataSetVidrio);
        dataSets.add(dataSetPapel);

        //si la grafica se queda sin datos aparecerá este texto

        graficaLineal.setNoDataText("No hay datos para representar");
        graficaLineal.setNoDataTextColor(Color.RED);

        //ocultamos la descripcion
        Description description = new Description();
        description.setText("");
        graficaLineal.setDescription(description);


        //habilitamos  el touch en la grafica

        graficaLineal.setTouchEnabled(true);
         graficaLineal.setPinchZoom(true);

        //animamos la grafica
        graficaLineal.animateX(2000); // animar milisegundos horizaontales

        graficaLineal.animateY(2000); // animar milisegundos verticales

        graficaLineal.animateXY(2000, 2000); // animar milisegundos horizaontales y verticales


        //adjudicamos los datasets a la grafica
        LineData data = new LineData(dataSets);
        graficaLineal.setData(data);

        //invalidamos la grafica
        graficaLineal.invalidate();


    }

    /**
     *
     * Metodo para separar por tipos las diferentes mesuras
     * Autor : Sergi Sirvent Sempere
     *Recibe una lista de mesuras y el tipo de basura que se quiera separar, devuelve una lista solo con las medidas de ese tipo
     */

    private List<Mesura> separarMesuras(String tipoBasura,List<Mesura> listaMesuras){

        List<Mesura> mesurasConcretas = new ArrayList<>();
        for (Mesura mesura:listaMesuras
             ) {
            if (mesura.getTipoMedida().equals(tipoBasura)){
                mesurasConcretas.add(mesura);
            }
        }
        return mesurasConcretas;
    }

    /**
     *
     * Metodo para llenar los diferentes datasets de la grafica lineal
     * Autor : Sergi Sirvent Sempere
     *Recibe una lista de mesuras y devuelve un array de entrys
     */

    private ArrayList<Entry> llenarDataSets(List<Mesura> listaMesuras){

        ArrayList<Entry> entrys = new ArrayList<>();

        int i = 0;
        for (Mesura mesura:listaMesuras
             ) {



                // este codigo devuelve el dia en que la medida fue tomada
//            Date date = new Date(mesura.getUnixTime() * 1000);
//            DateFormat dateFormat = new SimpleDateFormat("dd");
//            String strDate = dateFormat.format(date);
//
//            int dia = Integer.parseInt(strDate);
//
//            Log.d("Fecha","Milliseconds to Date: " + strDate);

            entrys.add(new Entry(i,(float)mesura.getLlenado()));
            i++;
        }

        return entrys;

    }

    /**
     *
     * Metodo para personalizar los datasets
     * Autor : Sergi Sirvent Sempere
     *Recibe el tipo de basura que tiene  ese dataset  y el dataset
     */

    private void personalizarDataset(String tipoBasura , LineDataSet dataSet){

        //caracteristicas comunes para todos los datasets
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setCircleHoleRadius(3);
        dataSet.setCircleRadius(5);
        dataSet.setLineWidth(3);

        switch (tipoBasura) {
            case "organico":

                dataSet.setColor(Color.parseColor(colorGraficaOrganica));
                dataSet.setCircleColorHole(Color.parseColor(colorGraficaOrganica));

                break;
            case "plastico":

                dataSet.setColor(Color.parseColor(colorGraficaPlastica));
                dataSet.setCircleColorHole(Color.parseColor(colorGraficaPlastica));

                break;
            case "vidrio":

                dataSet.setColor(Color.parseColor(colorGraficaVidrio));
                dataSet.setCircleColorHole(Color.parseColor(colorGraficaVidrio));

                break;
            case "papel":

                dataSet.setColor(Color.parseColor(colorGraficaPapel));
                dataSet.setCircleColorHole(Color.parseColor(colorGraficaPapel));

                break;
        }


    }


}
