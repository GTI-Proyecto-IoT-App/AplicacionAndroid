package com.example.androidappgestionbasura.presentacion.HomeActivityPackage;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoMesuras;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.utility.Utility;

/**
 * 26/11/2020 Rubén Pardo
 * Ventana con el resumen de reciclaje y los logros
 */
public class HuellaC02Fragment extends Fragment {

    private CasosUsoMesuras casosUsoMesuras;
    private ProgressBar progressBarCargaCO2;

    //para la barra de progreso horizontal

    private ProgressBar progressBarArboles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        casosUsoMesuras = new CasosUsoMesuras(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_huella_c02, container, false);
        progressBarCargaCO2 = root.findViewById(R.id.pbCO2);
        // si no hay mesuras pediras al servidor
        if(casosUsoMesuras.getListaMesuras()==null){
            progressBarCargaCO2.setVisibility(View.VISIBLE);// empieza la carga
            casosUsoMesuras.getMesurasMensuales(new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    // llamar a set up succes
                    progressBarCargaCO2.setVisibility(View.GONE);
                    setUpSucces(root);
                }

                @Override
                public void onError(Object object) {
                    // llamar a set up error
                    progressBarCargaCO2.setVisibility(View.GONE);
                    setUpError(root);
                }
            });
        }

        // Inflate the layout for this fragment
        return root;
    }

    private void setUpError(View root) {
        TextView tvInfo = root.findViewById(R.id.tvKgC02);
        tvInfo.setText("ERROR");
    }




    private void setUpSucces(View root) {
        TextView tvKgRes = root.findViewById(R.id.tvKgC02);
        TextView tvGeneradoReducido = root.findViewById(R.id.tvGeneradoOReducido);
        TextView tvArboles = root.findViewById(R.id.tvArboles);

        //buscamos e indicamos las propiedades que tendra la barra de llenado
        progressBarArboles = root.findViewById(R.id.progressBar);
        ImageView arbolSinHojas = root.findViewById(R.id.imageViewDeshojado);
        ImageView arbolConHojas = root.findViewById(R.id.imageViewConHojas);
        progressBarArboles.setSecondaryProgress(30);
        progressBarArboles.setMax(30);




        root.findViewById(R.id.linearDatos).setVisibility(View.VISIBLE);


        float kgCo2Generados = (float)casosUsoMesuras.getBolsasBasura().getKgC02Generados();

        //le atribuimos los kg de co2 creados a la barra
        //progressBarArboles.setProgress((int)kgCo2Generados*-1);
        progressBarArboles.setProgress(20);
        progressBarArboles.getLayoutParams().height = 2;

        //le cambiamos el color a la progress bar
        progressBarArboles.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#b70000")));
        progressBarArboles.setSecondaryProgressTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")));

        int arboles = casosUsoMesuras.getBolsasBasura().getArbolesPlantados();

        tvKgRes.setText(String.valueOf(Math.abs(kgCo2Generados)));

        tvArboles.setText(String.valueOf(arboles));

        Utility.animateTextValue(0,arboles,tvArboles);
        if(kgCo2Generados>=0){
            tvGeneradoReducido.setText(R.string.huella_co2_generado);
        }else{
            tvGeneradoReducido.setText(R.string.huella_co2_reducido);
        }

    }


}