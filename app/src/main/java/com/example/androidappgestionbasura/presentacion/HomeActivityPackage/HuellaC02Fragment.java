package com.example.androidappgestionbasura.presentacion.HomeActivityPackage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoMesuras;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;

/**
 * 26/11/2020 Rubén Pardo
 * Ventana con el resumen de reciclaje y los logros
 */
public class HuellaC02Fragment extends Fragment {

    private CasosUsoMesuras casosUsoMesuras;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        casosUsoMesuras = new CasosUsoMesuras(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_huella_c02, container, false);
        // si no hay mesuras pediras al servidor
        if(casosUsoMesuras.getListaMesuras()==null){
            casosUsoMesuras.getMesurasMensuales(new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    // llamar a set up succes
                    setUpSucces(root);
                }

                @Override
                public void onError(Object object) {
                    // llamar a set up error
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
        TextView tvInfo = root.findViewById(R.id.tvKgC02);
        double kgCo2Generados = casosUsoMesuras.getBolsasBasura().getKgC02Generados();

        if(kgCo2Generados>=0){
            tvInfo.setText("Has generado " + kgCo2Generados +" kg de C02 en este mes");
        }else{
            tvInfo.setText("Has reducido " + -kgCo2Generados +" kg de C02 en este mes");
        }

    }


}