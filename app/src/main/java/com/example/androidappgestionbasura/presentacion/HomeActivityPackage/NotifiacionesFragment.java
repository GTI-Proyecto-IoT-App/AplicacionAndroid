package com.example.androidappgestionbasura.presentacion.HomeActivityPackage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoNotificacion;
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

        String uid = ((AppConf) getActivity().getApplication()).getUsuario().getUid();
        casosUsoNotificacion = new CasosUsoNotificacion(uid);

        setUpRecyclerView(root);

        return root;
    }

    private void setUpRecyclerView(View root) {

        RecyclerView recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        FirestoreRecyclerOptions<Notificacion> opciones =
                casosUsoNotificacion.getQueryNotificaciones(getActivity());

        adapter =  new AdaptadorNotificacionesFirestoreUI(opciones, getActivity());

        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NOTIFICACION","borrar: "+v.getTag());
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
}