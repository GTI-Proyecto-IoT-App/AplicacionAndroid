package com.example.androidappgestionbasura.presentacion.HomeActivityPackage.misdispositivos;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.androidappgestionbasura.utility.Utility;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    Spinner spinnerLineal;
    private int VALORSPINNER = 0;

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
        usoMesuras.getMesurasPorId(dispositivo.getId(),new CallBack() {
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

                //CAMBIAR , LE PASO DIRECTAMENTE EL TIPO Y LA MESURA , Y SI LA MESURA ES NULL, QUE SEA CREE LA GRAFICA SIN DATOS
                
               crearGraficaPie("organico",mesuraFinalOrganico,pieChartOrganico);
               crearGraficaPie("plastico",mesuraFinalPlastico,pieChartPlastico);
               crearGraficaPie("papel",mesuraFinalPapel,pieChartPapel);
               crearGraficaPie("vidrio",mesuraFinalVidrio,pieChartVidrio);

//                Log.d("Hloa orga",mesuraFinalOrganico.getTipoMedida()+"");
//                Log.d("Hloa plas",mesuraFinalPlastico.getTipoMedida()+ "");
//                Log.d("Hloa papl",mesuraFinalPapel.getLlenado() + "");
//                Log.d("Hloa vidrio",mesuraFinalVidrio.getLlenado()+ "");

               crearGraficaLineal(listaMesuras.getMesuras(),lineChartGeneral);



                //configuramos el spinner de ordenar medidas
                spinnerLineal = findViewById(R.id.spinner);
                spinnerLineal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                        VALORSPINNER = position;
                        Log.d("selecc",VALORSPINNER+"");
                        crearGraficaLineal(listaMesuras.getMesuras(),lineChartGeneral);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });

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
    private void crearGraficaPie(String tipoBasura , Mesura mesura, PieChart graficaCircular){



        //definimos la lista de entradas a la grafica
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        //inicializar datos
        Map<String, Integer> listaCantidades = new HashMap<>();


        if (mesura == null){

            listaCantidades.put(tipoBasura,0);
            //inicializar sector vacio
            int numeroVacio = 100;
            listaCantidades.put("Vacio",numeroVacio);

            //ponemos el texto central dentro de la grafica
            graficaCircular.setCenterText(String.valueOf(101+"%"));





        }else{

            listaCantidades.put(tipoBasura,(int)mesura.getLlenado());

            //inicializar sector vacio
            int numeroVacio = 100-(int)mesura.getLlenado();
            listaCantidades.put("Vacio",numeroVacio);

            //ponemos el texto central dentro de la grafica
            graficaCircular.setCenterText(String.valueOf(mesura.getLlenado()+"%"));

        }

        //Metemos los datos en pieEntries
        for(String type: listaCantidades.keySet()){
            pieEntries.add(new PieEntry(listaCantidades.get(type).floatValue(), ""));
        }

        //recogemos los datos que coincidan con el nombre de la label
        PieDataSet pieDataSet = new PieDataSet(pieEntries,tipoBasura);

        //tamaño de texto
        pieDataSet.setValueTextSize(0);

        //indicamos el color de las diferentes entradas en funcion del tipo de basura
        //Log.d("Tipo basura",tipoBasura+"");

        ArrayList<Integer> colors = setearColoresPieChart(tipoBasura);
        pieDataSet.setColors(colors);

        //agrupamos los datos de las entradas en la grafica
        PieData pieData = new PieData(pieDataSet);

        //mostramos el valor de las entradas en la grafica
        pieData.setDrawValues(true);

        //adjudicamos los datos a la grafica
        graficaCircular.setData(pieData);



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
    ArrayList<Integer> setearColoresPieChart( String tipoBasura){

        ArrayList<Integer> listaFinal = new ArrayList<>();
        if(tipoBasura == null){

            listaFinal.add(Color.parseColor(colorVacio));

        }else if(tipoBasura.equals("organico")){

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

        //array de datasets

        ArrayList<ILineDataSet>dataSets = new ArrayList<>();
        List<Mesura> arrayTemp = new ArrayList<>();

        //List<Mesura> mesurasOrdenadasPorTiempo = new ArrayList<>();

        Collections.sort(listaMesuras, new Comparator<Mesura>() {
            @Override
            public int compare(Mesura o1, Mesura o2) {

                return Long.compare(o1.getUnixTime(),o2.getUnixTime());
            }
        });

        
        long tiempoHaceUnaSemana = Utility.getUnixTimeHaceUnaSemana();
        long timepoHaceUnMes = Utility.getUnixTimeHaceUnMes();

        //filtramos las mesuras de la ultima semana


//si el valor de la variable es 1 se mostraran las del ultimo mes
        if (VALORSPINNER == 1){

            //modificamos listamesuras


            for (Mesura mesura :listaMesuras
            ) {

                if(mesura.getUnixTime()<=System.currentTimeMillis() && mesura.getUnixTime()>= timepoHaceUnMes){
                    arrayTemp.add(mesura);
                }
            }



            //calibramos los axis
            graficaLineal.getXAxis().setAxisMaximum(31);
            graficaLineal.getXAxis().setAxisMinimum(1);



        }else if(VALORSPINNER == 0){//si el valor de la variable es 0 se mostraran las de la ultima semana

            //modificamos arrayTemp
            for (Mesura mesura :listaMesuras) {
                Log.d("tagmseu",mesura.getLlenado()+""+mesura.getTipoMedida());
                if(mesura.getUnixTime()<=System.currentTimeMillis() && mesura.getUnixTime()>= tiempoHaceUnaSemana){
                    arrayTemp.add(mesura);
                }
            }
            //calibramos los axis
            Date date = new Date(System.currentTimeMillis());
            DateFormat dateFormat = new SimpleDateFormat("dd");
            String strDate = dateFormat.format(date);
            int dia = Integer.parseInt(strDate);
            int diaSemanaAnterior = Integer.parseInt(dateFormat.format(new Date(Utility.getUnixTimeHaceUnaSemana()*1000)));

            graficaLineal.getXAxis().setAxisMinimum(1);
            graficaLineal.getXAxis().setAxisMaximum(7);
            graficaLineal.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    String res = String.valueOf(Math.round(value));
                    String[] diasSemana = {"Lun.", "Mart.", "Miérc.", "Juev.", "Vier.", "Sáb.", "Dom."};
                    if (VALORSPINNER==0){//semana
                        res =diasSemana[Math.round(value)-1];
                    }
                    return res;
                }
            });


        }

        //declaramos los dataSets
        List<Mesura> listaMesurasOrganico = separarMesuras("organico",arrayTemp);
        List<Mesura> listaMesurasPlastico =separarMesuras("plastico",arrayTemp);
        List<Mesura> listaMesurasPapel =separarMesuras("papel",arrayTemp);
        List<Mesura> listaMesurasVidrio =separarMesuras("vidrio",arrayTemp);

        if (!listaMesurasOrganico.isEmpty()){
            //inicializamos el dataset
            LineDataSet dataSetOrganico = new LineDataSet(llenarDataSets(listaMesurasOrganico),"Orgánico");
            //llamamos a personalizar dataset
            personalizarDataset("organico",dataSetOrganico);
            //lo añadimos a la array de datasets
            dataSets.add(dataSetOrganico);
        }
        if (!listaMesurasPlastico.isEmpty()){

            LineDataSet dataSetPlastico = new LineDataSet(llenarDataSets(listaMesurasPlastico),"Plástico");
            personalizarDataset("plastico",dataSetPlastico);
            dataSets.add(dataSetPlastico);
        }
        if (!listaMesurasPapel.isEmpty()){

            LineDataSet dataSetPapel = new LineDataSet(llenarDataSets(listaMesurasPapel),"Papel");
            personalizarDataset("papel",dataSetPapel);
            dataSets.add(dataSetPapel);
        }
        if (!listaMesurasVidrio.isEmpty()){

            LineDataSet dataSetVidrio = new LineDataSet(llenarDataSets(listaMesurasVidrio),"Vidrio");

            personalizarDataset("vidrio",dataSetVidrio);
            dataSets.add(dataSetVidrio);
        }

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

        //establecemos el valor maximo y minimo para el eje y
        graficaLineal.getAxisLeft().setAxisMaximum(100);
        graficaLineal.getAxisLeft().setAxisMinimum(0);

        graficaLineal.getAxisRight().setEnabled(false);
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

        for (Mesura mesura:listaMesuras) {
            if (mesura.getTipoMedida().equals(tipoBasura)){
                mesurasConcretas.add(mesura);
//                Log.d("conc",""+mesura.getTipoMedida());
            }
        }
        return mesurasConcretas;

    }

    /**
     *
     * Metodo para llenar los diferentes datasets de la grafica lineal de semana
     * Autor : Sergi Sirvent Sempere
     *Recibe una lista de mesuras y devuelve un array de entrys
     */

    private ArrayList<Entry> llenarDataSets(List<Mesura> listaMesuras){
        ArrayList<Entry> entrys = new ArrayList<>();
        if(VALORSPINNER==0){//semana
            for (Mesura mesura:listaMesuras) {
                entrys.add(new Entry(Utility.getIndexOfDayNumber(mesura.getUnixTime()),(float)mesura.getLlenado()));
            }
        }else{//mes
            for (Mesura mesura:listaMesuras) {
                Date date = new Date(mesura.getUnixTime());
                DateFormat dateFormat = new SimpleDateFormat("dd");
                String strDate = dateFormat.format(date);
                int dia = Integer.parseInt(strDate);

                entrys.add(new Entry(dia,(float)mesura.getLlenado()));
            }
        }
        return entrys;
    }

    /**
     *
     * Metodo para llenar los diferentes datasets de la grafica lineal de mes
     * Autor : Sergi Sirvent Sempere
     *Recibe una lista de mesuras y devuelve un array de entrys
     */
    private ArrayList<Entry> llenarDataSetsMes(List<Mesura> listaMesuras){
        ArrayList<Entry> entrys = new ArrayList<>();

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
