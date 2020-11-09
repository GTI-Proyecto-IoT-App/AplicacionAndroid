package com.example.androidappgestionbasura.presentacion.HomeActivityPackage.misdispositivos;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoDispositivo;
import com.example.androidappgestionbasura.model.TipoDispositivo;
import com.example.androidappgestionbasura.presentacion.ScanCodeActivity;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivos;
import com.example.androidappgestionbasura.utility.AppConf;
import com.example.androidappgestionbasura.model.InterfaceDispositivos;
import com.example.androidappgestionbasura.utility.Constantes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.app.Activity.RESULT_OK;


public class MisDispositivosFragment extends Fragment {
    private InterfaceDispositivos listaDispositivos;
    private CasosUsoDispositivo usoDispositivo;
    private RecyclerView recyclerView;
    public AdaptadorDispositivos adaptador;
    private LinearLayout emptyView;
    private final int codigoRespuestaCreacionDispositivo = 1234;
    private final int codigoRespuestaEdicionDispositivo = 4321;
    private Activity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.mis_dispositivos, container, false);
        setHasOptionsMenu(true);

        activity = getActivity();

        listaDispositivos = ((AppConf) activity.getApplication()).listaDispositivos;
        usoDispositivo = new CasosUsoDispositivo(activity, listaDispositivos);
        adaptador = new AdaptadorDispositivos(listaDispositivos);

        recyclerView = root.findViewById(R.id.recyclerview_mis_dispositivos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        recyclerView.setAdapter(adaptador);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarVistaDispositivos(v);
            }
        });
        emptyView = root.findViewById(R.id.textviewrecyclervacio);

        FloatingActionButton fab = root.findViewById(R.id.buttonQR);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDipositvo(null);
            }
        });


        comprobarVaciadoDispositivos();



        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("RESULTDETALLES","ENTRO");
        if(codigoRespuestaCreacionDispositivo == requestCode && resultCode == RESULT_OK){
            int a = data.getIntExtra("Dispositivo creado",0);
            adaptador.notifyItemInserted(a);
            comprobarVaciadoDispositivos();
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

        if (listaDispositivos.tamaño() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }


    public void lanzarVistaDispositivos(View view) {
        usoDispositivo.mostrar(view,recyclerView.getChildAdapterPosition(view),codigoRespuestaEdicionDispositivo);
    }

    public void addDipositvo(View view) {
        new AlertDialog.Builder(activity)
                .setTitle(getString(R.string.vincular_disposiivo_dialog_title))
                .setMessage(getString(R.string.vincular_disposiivo_dialog_mensaje))

                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        startActivity(new Intent(getContext(), ScanCodeActivity.class));
                        //usoDispositivo.crear(TipoDispositivo.BASURA, codigoRespuestaCreacionDispositivo);

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {

        inflater.inflate(R.menu.menu_dispositivos, menu);
        super.onCreateOptionsMenu(menu, inflater);



    }


}