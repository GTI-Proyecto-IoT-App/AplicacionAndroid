package com.example.androidappgestionbasura.presentacion.HomeActivityPackage.misdispositivos;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoDispositivo;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.datos.preferences.SharedPreferencesHelper;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.TipoDispositivo;
import com.example.androidappgestionbasura.presentacion.ScanCodeActivity;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivosFirestoreUI;
import com.example.androidappgestionbasura.repository.impl.DispositivosRepositoryImpl;
import com.example.androidappgestionbasura.utility.AppConf;
import com.example.androidappgestionbasura.utility.Constantes;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.Query;

import static android.app.Activity.RESULT_OK;


public class MisDispositivosFragment extends Fragment {
    private CasosUsoDispositivo usoDispositivo;
    private RecyclerView recyclerView;
    private AdaptadorDispositivosFirestoreUI adaptador;

    private CasosUsoUsuario casosUsoUsuario;
    private DispositivosRepositoryImpl dispositivos;
    private LinearLayout emptyView;
    private final int codigoRespuestaCreacionDispositivo = 1234;
    private final int codigoRespuestaEdicionDispositivo = 4321;
    private final int codigoRespuestaCodigoQR = 1789;
    private Activity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.mis_dispositivos, container, false);


        activity = getActivity();
        usoDispositivo = new CasosUsoDispositivo(activity);
        casosUsoUsuario = new CasosUsoUsuario(getActivity());
        emptyView = root.findViewById(R.id.textviewrecyclervacio);
        setUpRecycler(root);

        FloatingActionButton fab = root.findViewById(R.id.buttonQR);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDipositvo(null);
            }
        });


        return root;
    }

    private void setUpRecycler(View root) {
        recyclerView = root.findViewById(R.id.recyclerview_mis_dispositivos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        ((AppConf) getActivity().getApplication()).initAdaptador();

        adaptador = ((AppConf) getActivity().getApplication()).adaptador;


        adaptador.setCallbackDataChange(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                int count = (int) object;
                if (count == 0) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Object object) {

            }
        });
        
        recyclerView.setAdapter(adaptador);
        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarVistaDispositivos(v);
            }
        });


        adaptador.startListening();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(codigoRespuestaCreacionDispositivo == requestCode && resultCode == RESULT_OK){
            //int a = data.getIntExtra("Dispositivo creado",0);
            //adaptador.notifyItemInserted(a);
//            comprobarVaciadoDispositivos();
        }else if (codigoRespuestaEdicionDispositivo == requestCode && resultCode == Constantes.RESULT_RECYCLER_VIEW_BORRAR){
            int a = data.getIntExtra("Dispositivo a borrar",0);
            //adaptador.notifyItemRemoved(a);
//            comprobarVaciadoDispositivos();
        }else if (codigoRespuestaEdicionDispositivo == requestCode && resultCode == Constantes.RESULT_RECYCLER_VIEW_EDITAR){
            int a = data.getIntExtra("Dispositivo a editar",0);
            //adaptador.notifyItemChanged(a);
        }else if (codigoRespuestaCodigoQR == requestCode && resultCode == RESULT_OK){
            String idDispositivo = data.getStringExtra("codigoQR");
            gestionarDispositivo(idDispositivo);
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
                        startActivityForResult(new Intent(getContext(), ScanCodeActivity.class), codigoRespuestaCodigoQR);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
//    {
//        inflater.inflate(R.menu.menu_dispositivos, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    private void gestionarDispositivo(final String idDispositivo){
        usoDispositivo.dipositivoYaVinculado(idDispositivo, casosUsoUsuario.getUsuario().getUid(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object == null) {

                    Snackbar.make(getActivity().findViewById(android.R.id.content), Html.fromHtml("<font color=\"#808080\">El dispositivo ya esta vinculado</font>"), Snackbar.LENGTH_LONG).show();
                }else{
                    Dispositivo dispositivo = (Dispositivo) object;
                    if (dispositivo.getUsuariosVinculados().isEmpty()){
                        usoDispositivo.vincular(TipoDispositivo.BASURA, idDispositivo,codigoRespuestaCreacionDispositivo);
                    }else{
                        dispositivo.getUsuariosVinculados().add(casosUsoUsuario.getUsuario().getUid());
                        usoDispositivo.add(dispositivo);
                    }
                }
            }
            @Override
            public void onError(Object object) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), Html.fromHtml("<font color=\"#808080\">No se ha podido encontrar el dispositivo</font>"), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adaptador.startListening();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adaptador.stopListening();
    }
}