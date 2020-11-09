package com.example.androidappgestionbasura.casos_uso;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.View;

import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.InterfaceDispositivos;
import com.example.androidappgestionbasura.model.TipoDispositivo;
import com.example.androidappgestionbasura.presentacion.HomeActivityPackage.misdispositivos.DispositivoDetallesActivity;
import com.example.androidappgestionbasura.presentacion.HomeActivityPackage.misdispositivos.FormularioCreacionBasura;
import com.example.androidappgestionbasura.repository.impl.DispositivosRepositoryImpl;
import com.example.androidappgestionbasura.utility.Utility;

import static com.example.androidappgestionbasura.utility.Constantes.RESULT_RECYCLER_VIEW_BORRAR;

public class CasosUsoDispositivo {
    private Activity actividad;
    private InterfaceDispositivos dispositivos;
    private final DispositivosRepositoryImpl dispositivosRepository;// leer editar dispositivos

    public CasosUsoDispositivo(Activity actividad, InterfaceDispositivos dispositivos) {
        this.actividad = actividad;
        this.dispositivos = dispositivos;
        dispositivosRepository = new DispositivosRepositoryImpl();

    }
    // OPERACIONES BÁSICAS
    public void mostrar(View cardViewAMostrar, int pos, int codigoRespuestaEdicionDispositivo) {

        Intent i = new Intent(actividad, DispositivoDetallesActivity.class);
        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View,String>(cardViewAMostrar,"transicion_card_item_dispositivos");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(actividad,pairs);


        i.putExtra("pos", pos);
        actividad.startActivityForResult(i, codigoRespuestaEdicionDispositivo,options.toBundle());
    }
    public void vincular(TipoDispositivo tipo, String idDispositivo , int codigoRespuestaCreacionDispositivo){
        switch(tipo) {
            case BASURA:
                Intent formulario = new Intent(actividad, FormularioCreacionBasura.class);
                formulario.putExtra("idDispositivo", idDispositivo);
                actividad.startActivityForResult(formulario, codigoRespuestaCreacionDispositivo);
                break;

            case ELECTRICO:  break;
            case AGUA:  break;
        }
    }

    /**
     * se llama cuando se da a aceptar en el dialog de confirmar
     * vuelve a mis dispositivos
     * @param id
     */
    public void borrar(final int id, String idUsuario) {
        Dispositivo disp = dispositivos.elemento(id);
        disp.getUsuariosVinculados().remove(idUsuario);
        dispositivosRepository.updateDispositivo(disp.getId(), Utility.objectToHashMap(disp), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                dispositivos.borrar(id);
                Intent intent = new Intent();
                intent.putExtra("Dispositivo a borrar",id);
                actividad.setResult(RESULT_RECYCLER_VIEW_BORRAR,intent);
                actividad.finish();
            }

            @Override
            public void onError(Object object) {

            }
        });

    }

    /**
     * llama a formulario editar m¡dispositvos
     * @param pos
     */
    public void editar(int pos,int codigo) {
        Intent i = new Intent(actividad, FormularioCreacionBasura.class);
        i.putExtra("pos", pos);
        actividad.startActivityForResult(i, codigo );
    }

    //TODO revisar funcionalida de actualizar el recycler view
   public int add (final Dispositivo dispositivo){
       dispositivosRepository.updateDispositivo(dispositivo.getId(), Utility.objectToHashMap(dispositivo), new CallBack() {
           @Override
           public void onSuccess(Object object) {
               dispositivos.añade(dispositivo);
           }

           @Override
           public void onError(Object object) {

           }
       });

       return dispositivos.tamaño()-1;

   }

    /**
     * actualiza el objeto
     * @param id
     * @param dispositivo
     */
    //TODO revisar mostrar mensajes de error
    public void guardar(final int id, final Dispositivo dispositivo) {
        dispositivosRepository.updateDispositivo(dispositivo.getId(), Utility.objectToHashMap(dispositivo), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                dispositivos.actualiza(id, dispositivo);
            }

            @Override
            public void onError(Object object) {

            }
        });


    }



    public void dipositivoYaVinculado(String idDispositivo, final String idUsuario, final CallBack callBack) {
        dispositivosRepository.readDispostivoByID(idDispositivo, new CallBack() {
            @Override
            public void onSuccess(Object object) {

                if (object == null){
                    callBack.onError(null);
                }else {
                    Dispositivo dispositivo = (Dispositivo) object;
                    if (dispositivo.getUsuariosVinculados().contains(idUsuario)){
                        //el dipositivo ya esta vinculado
                        callBack.onSuccess(null);

                    }else{
                        //vincular dispositivo
                        callBack.onSuccess(dispositivo);
                    }
                }


            }

            @Override
            public void onError(Object object) {
               callBack.onError(null);
            }
        });
    }
}
