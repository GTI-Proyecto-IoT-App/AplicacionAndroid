package com.example.androidappgestionbasura.presentacion.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.notificaciones.Notificacion;
import com.example.androidappgestionbasura.utility.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorNotificacionesFirestoreUI
        extends FirestoreRecyclerAdapter<Notificacion, AdaptadorNotificacionesFirestoreUI.ViewHolder> {

    protected View.OnClickListener onClickListener;
    protected Context context;
    private CallBack callBack;

    public AdaptadorNotificacionesFirestoreUI(
            @NonNull FirestoreRecyclerOptions<Notificacion> options, Context context,
            CallBack callBack){
        super(options);
        this.context = context;
        this.callBack = callBack;

    }
    @Override public AdaptadorNotificacionesFirestoreUI.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_notificacion, parent, false);

        return new AdaptadorNotificacionesFirestoreUI.ViewHolder(view);
    }
    @Override protected void onBindViewHolder(@NonNull AdaptadorNotificacionesFirestoreUI.ViewHolder holder,
                                              int position, @NonNull Notificacion notificacion) {
        holder.itemView.setTag(new Integer(position));//para obtener posición
        holder.personaliza(notificacion);
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


    @Override
    public void onDataChanged() {
        callBack.onSuccess(this.getItemCount());
        super.onDataChanged();
    }


    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView nombre, fecha,descripcion;
        public ImageView foto,btnBorrar;


        public ViewHolder(View itemView) {
            super(itemView);

            btnBorrar = itemView.findViewById(R.id.btnBorrar);
            btnBorrar.setOnClickListener(onClickListener);

            nombre = itemView.findViewById(R.id.nombre);
            descripcion = itemView.findViewById(R.id.tvDescrpcion);
            fecha = itemView.findViewById(R.id.tvFecha);
            foto = itemView.findViewById(R.id.foto);

        }
        // Personalizamos un ViewHolder a partir de un notificacion
        public void personaliza(Notificacion notificacion) {

            btnBorrar.setTag(notificacion.getId());


            nombre.setText(notificacion.getNombreDispositivo());
            fecha.setText(Utility.getDate(notificacion.getFecha(),"dd-mm-yyyy HH:mm"));
            int id = notificacion.getTipo().getRecurso();
            switch(notificacion.getTipo()) {
                case DESCONECTADO:
                    descripcion.setText(R.string.dispositivo_sin_conexion);
                    break;
                case CONECTADO:
                    descripcion.setText(R.string.dispositivo_con_conexion);
            }
            foto.setImageResource(id);
            foto.setScaleType(ImageView.ScaleType.FIT_END);

        }
    }

}
