package com.example.androidappgestionbasura.presentacion.HomeActivityPackage;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoMesuras;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.example.androidappgestionbasura.datos.firebase.FirebaseReferences;
import com.example.androidappgestionbasura.datos.firebase.FirebaseRepository;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.model.bolsas_basura.ListaBolsaBasura;
import com.example.androidappgestionbasura.utility.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import static com.example.androidappgestionbasura.datos.firebase.constants.Constant.SUCCESS;
import static com.example.androidappgestionbasura.datos.firebase.constants.FirebaseConstants.TABLA_USUARIOS;

/**
 * 26/11/2020 Rubén Pardo
 * Ventana con el resumen de reciclaje y los logros
 */
public class HuellaC02Fragment extends Fragment {

    private CasosUsoMesuras casosUsoMesuras;

    private ProgressBar progressBarCargaCO2;

    //para la barra de progreso horizontal

    //private ProgressBar progressBarArboles;

    // usuario
    private CasosUsoUsuario casosUsoUsuario;
    private EditText editTextKw;
    private EditText editTextL;
    private Button btnGuardar;
    private FirebaseRepository firebaseRepository;

    private CollectionReference usuariosCollectionReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        casosUsoMesuras = new CasosUsoMesuras(getActivity());
        casosUsoUsuario = new CasosUsoUsuario(getActivity());


        usuariosCollectionReference = FirebaseReferences.getInstancia().getDATABASE().collection(TABLA_USUARIOS);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_huella_c02, container, false);
        progressBarCargaCO2 = root.findViewById(R.id.progressBar);


        //consultamos los datos del usuario de la base de datos


        Log.d("hola","1");

        Log.d("hola","2");

       // casosUsoUsuario.getUsuario().setConsumoCO2(casosUsoMesuras.getListaMesuras().getKgC02Generados());

        TextView tvKgRes = root.findViewById(R.id.tvKgC02);
        tvKgRes.setText(String.valueOf(casosUsoUsuario.getUsuario().getConsumoCO2()));
        casosUsoUsuario.updateUsuario(casosUsoUsuario.getUsuario(), new CallBack() {

            @Override
            public void onSuccess(Object object) {
                Log.d("hola","Ha cargado el usuario al entrar");
                setUpSucces(root);

            }

            @Override
            public void onError(Object object) {
                Log.d("hola","Error");
            }
        });
        //le atribuimos los kg de co2 creados a la barra

        progressBarCargaCO2.setProgress((int) casosUsoUsuario.getUsuario().getConsumoCO2());
        progressBarCargaCO2.getLayoutParams().height = 10;

        //le cambiamos el color a la progress bar
        progressBarCargaCO2.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#b70000")));
        progressBarCargaCO2.setSecondaryProgressTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")));

        //le atribuimos los datos del layout a los edit text
        editTextKw = root.findViewById(R.id.editTextKw);
        editTextL = root.findViewById(R.id.editTextLitros);

        //le atribuimos a los edit text el valor que tiene de consumo este usuario en la base de datos

        editTextKw.setText(String.valueOf(casosUsoUsuario.getUsuario().getConsumoElectrico()));
        editTextL.setText(String.valueOf(casosUsoUsuario.getUsuario().getConsumoDeAgua()));





        //boton guardar
        btnGuardar = root.findViewById(R.id.botonGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hola","Click");
               updateUsuario();
                Toast toast1 =
                        Toast.makeText(root.getContext(),
                                getResources().getString(R.string.texto_boton_guardar), Toast.LENGTH_SHORT);

                toast1.show();
            }
        });


        // si no hay mesuras pediras al servidor
        if (casosUsoMesuras.getListaMesuras() == null){
            Log.d("hola","entra al if del null");
            casosUsoMesuras.getMesurasMensuales(new CallBack() {

                @Override
                public void onSuccess(Object object) {
                    Log.d("hola","entra al if del null y al callback");
                }

                @Override
                public void onError(Object object) {
                    Log.d("hola","entra al if del null y al callback");
                }

            });
        }







        //añadimos los listeners por si el texto de los edit text cambian
        editTextKw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



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

        root.findViewById(R.id.linearDatos).setVisibility(View.VISIBLE);

        float kgCo2Generados = (float)casosUsoMesuras.getBolsasBasura().getKgC02Generados();

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

    /*
    *Metodo para setear los valores de electricidad y agua del usuario en la base de datos
     */

    public void updateUsuario(){
        //v.setEnabled(false);

        double nuevoConsumoElectrico = Double.parseDouble(editTextKw.getText().toString());
        double nuevoConsumoAgua = Double.parseDouble(editTextL.getText().toString());
        casosUsoUsuario.getUsuario().setConsumoElectrico(nuevoConsumoElectrico);
        casosUsoUsuario.getUsuario().setConsumoDeAgua(nuevoConsumoAgua);
        casosUsoUsuario.getUsuario().setConsumoCO2(casosUsoUsuario.getUsuario().getConsumoCO2());

        casosUsoUsuario.updateUsuario(casosUsoUsuario.getUsuario(), new CallBack() {

            @Override
            public void onSuccess(Object object) {
                Log.d("hola","Se han modificado los datos con el boton guardar");
            }

            @Override
            public void onError(Object object) {

            }
        });

    };


}