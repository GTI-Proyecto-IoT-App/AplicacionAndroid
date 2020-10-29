package com.example.androidappgestionbasura.presentacion.HomeActivityPackage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;


public class MiPerfilFragment extends Fragment {

    private CasosUsoUsuario casosUsoUsuario;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mi_perfil, container, false);
        casosUsoUsuario = new CasosUsoUsuario(getActivity());


        setUpVista(root);
        setUpAcciones(root);


        return root;
    }

    private void setUpVista(View root) {

    }

    private void setUpAcciones(View root) {
        Button btnCerrarSesion = root.findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrar_sesion(v);
            }
        });

    }


    public void cerrar_sesion(View v){
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.cerrar_sesion))
                .setMessage(R.string.cerrar_sesion_mensaje)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        casosUsoUsuario.cerrarSesion();
                    }
                })
                .setNegativeButton(getString(android.R.string.cancel),null)
                .show();
    }
}