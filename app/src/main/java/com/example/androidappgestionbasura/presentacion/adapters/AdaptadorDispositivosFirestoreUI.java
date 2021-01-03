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
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorDispositivosFirestoreUI extends FirestoreRecyclerAdapter<Dispositivo, AdaptadorDispositivosFirestoreUI.ViewHolder> {
    protected View.OnClickListener onClickListener;
    protected Context context;
    private boolean empty;
    private CallBack callBack;

    private List<Dispositivo> dispositivoList;

    public AdaptadorDispositivosFirestoreUI(
            @NonNull FirestoreRecyclerOptions<Dispositivo> options, Context context){
        super(options);
        this.context = context;
        empty = true;

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

    @Override
    public void onDataChanged() {

        // hay una lista guardada que contiene los dispositivos antiguos, cuando se llame a este
        // metodo se comprobará si se ha añadido o eliminado un dispostivo para asi notificar al
        // servicio de notificaciones si tiene que registrarlo o elimanrlo
        // independientemente guardarlo en shared preferences en forma de json para que en caso de que se reincie la app
        // y se arranque el servicio sin la app se tenga el json para convertirlo en objetos y que no de error
        // this.getSnapshots se actualizará y este meteodo se llamará cada vez que el server se actualiza


        if(dispositivoList == null){
            // se llama por primera vez
            dispositivoList = new ArrayList<Dispositivo>();
            dispositivoList.addAll(this.getSnapshots());

        }else{

            if(dispositivoList.size() > this.getSnapshots().size()){
                // se ha borrado un dispositivo
                Dispositivo dispositivoBorrado;

                for(Dispositivo d : dispositivoList){
                    boolean isBorrado = true;
                    for(Dispositivo dList : this.getSnapshots()){
                        if (d.getId().equals(dList.getId())) {
                            isBorrado = false;
                            break;
                        }
                    }
                    if(isBorrado){
                        dispositivoBorrado = d;
                        dispositivoList.remove(dispositivoBorrado);
                        break;
                    }
                }


            }else if(dispositivoList.size() < this.getSnapshots().size()){
                // se ha añadido un dispositivo
                Log.d("SNAPSHOTS","SE HA AÑADIDO UNO");
                Dispositivo dispositivoNuevo;

                for(Dispositivo d : this.getSnapshots()){
                    boolean isNuevo = true;
                    Log.d("SNAPSHOTS","VUELTA-----D: "+d.getId());
                    for(Dispositivo dList : dispositivoList){
                        Log.d("SNAPSHOTS","D: "+d.getId());
                        Log.d("SNAPSHOTS","dList: "+dList.getId());
                        if (d.getId().equals(dList.getId())) {
                            isNuevo = false;
                            Log.d("SNAPSHOTS","BREAK");
                            break;
                        }

                    }
                    Log.d("SNAPSHOTS","FIN VUELTA-----");
                    Log.d("SNAPSHOTS","COMPROBAR: "+isNuevo);
                    if(isNuevo){
                        dispositivoNuevo = d;
                        dispositivoList.add(dispositivoNuevo);
                        Log.d("SNAPSHOTS-NUEVO",dispositivoNuevo.getNombre());
                        break;
                    }
                }

            }

        }
        // guardar en shared preferences la lista en formato json independientemente para asi guardar
        // los posibles nuevos cambios en nombres


        callBack.onSuccess(getItemCount());

        super.onDataChanged();
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void setCallbackDataChange(CallBack callBack) {
        this.callBack = callBack;
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
