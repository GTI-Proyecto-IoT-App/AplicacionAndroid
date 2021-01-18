package com.example.androidappgestionbasura.presentacion.HomeActivityPackage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoNotificacion;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.notificaciones.Notificacion;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivosFirestoreUI;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorNotificacionesFirestoreUI;
import com.example.androidappgestionbasura.utility.AppConf;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NotifiacionesFragment extends Fragment {

    private CasosUsoNotificacion casosUsoNotificacion;
    private AdaptadorNotificacionesFirestoreUI adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mis_notificaciones, container, false);

        setHasOptionsMenu(true);

        String uid = ((AppConf) getActivity().getApplication()).getUsuario().getUid();
        casosUsoNotificacion = new CasosUsoNotificacion(uid,getActivity());

        setUpRecyclerView(root);

        return root;
    }

    private void setUpRecyclerView(View root) {

        TextView tvVacio = root.findViewById(R.id.tvNotificacionesVacio);

        RecyclerView recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        FirestoreRecyclerOptions<Notificacion> opciones =
                casosUsoNotificacion.getQueryNotificaciones();

        adapter =  new AdaptadorNotificacionesFirestoreUI(opciones, getActivity(), new CallBack() {
            // cuando hay un cambio se envia cuantos items hay, asi sabemos cuando
            // esta vacio y mostrar una imagen
            @Override
            public void onSuccess(Object object) {
                int count = (int) object;
                if(count == 0){
//                    Log.d("NOTIFICACION","Se muestra imagen vacia");
                    recyclerView.setVisibility(View.GONE);
                    tvVacio.setVisibility(View.VISIBLE);
                }else{
//                    Log.d("NOTIFICACION","Se muestra el recycler");
                    recyclerView.setVisibility(View.VISIBLE);
                    tvVacio.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(Object object) {

            }
        });

        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casosUsoNotificacion.borrarNotificacion(v.getTag());
            }
        });


        recyclerView.setAdapter(adapter);

        adapter.startListening();

    }


    @Override
    public void onResume() {
        adapter.startListening();
        super.onResume();
    }

    @Override
    public void onStop() {
        adapter.stopListening();
        super.onStop();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notificaciones, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.accion_borrar_todas_notificaciones) {
            casosUsoNotificacion.borrarTodasNotificaciones();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

}