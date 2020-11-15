package com.example.androidappgestionbasura.presentacion.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdaptadorDispositivosFirestoreUI extends FirestoreRecyclerAdapter<Dispositivo, AdaptadorDispositivosFirestoreUI.ViewHolder> {
    protected View.OnClickListener onClickListener;
    protected Context context;
    private boolean empty;

    public AdaptadorDispositivosFirestoreUI(
            @NonNull FirestoreRecyclerOptions<Dispositivo> options, Context context){
        super(options);
        this.context = context;
    }
    @Override public ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dispositivo_lista, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }
    @Override protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Dispositivo dispositivo) {
        holder.itemView.setTag(new Integer(position));//para obtener posición
        holder.personaliza(dispositivo);
    }
    public void setOnItemClickListener(View.OnClickListener onClick) {
        onClickListener = onClick;
    }
    public String getKey(int pos) {
        return super.getSnapshots().getSnapshot(pos).getId();
    }
    public int getPos(String id) {
        int pos = 0;
        while (pos < getItemCount()){
            if (getKey(pos).equals(id)) return pos;
            pos++;
        }
        return -1;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
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
