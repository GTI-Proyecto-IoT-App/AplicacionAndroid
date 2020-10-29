package com.example.androidappgestionbasura.presentacion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.model.InterfaceDispositivos;
import com.example.androidappgestionbasura.model.Dispositivo;

public class AdaptadorDispositivos extends
        RecyclerView.Adapter<AdaptadorDispositivos.ViewHolder> {

    protected InterfaceDispositivos listaDispositivos;


    protected View.OnClickListener onClickListener;

     // Lista de dispositivos a mostrar
    public AdaptadorDispositivos(InterfaceDispositivos listaDispositivos) {
        this.listaDispositivos = listaDispositivos;

    }
    // Creamos el ViewHolder con la vista de un elemento sin personalizar
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dispositivo_lista, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }
    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Dispositivo dispositivo = listaDispositivos.elemento(posicion);
        holder.personaliza(dispositivo);
    }
    // Indicamos el número de elementos de la lista
    @Override public int getItemCount() {

        return listaDispositivos.tamaño();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView nombre, descripcion;
        public ImageView foto;


        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            descripcion = itemView.findViewById(R.id.descripcion);
            foto = itemView.findViewById(R.id.foto);



        }
        // Personalizamos un ViewHolder a partir de un dispositivo
        public void personaliza(Dispositivo dispositivo) {
            nombre.setText(dispositivo.getNombre());
            descripcion.setText(dispositivo.getDescripcion());
            int id = R.drawable.ic_smart_trash;
            switch(dispositivo.getTipo()) {
                case BASURA:
                    id = R.drawable.ic_smart_trash; break;
                case ELECTRICO: id = R.drawable.ic_control_de_energia; break;
                case AGUA: id = R.drawable.ic_control_de_agua; break;
                 }
            foto.setImageResource(id);
            foto.setScaleType(ImageView.ScaleType.FIT_END);

        }



    }


}

