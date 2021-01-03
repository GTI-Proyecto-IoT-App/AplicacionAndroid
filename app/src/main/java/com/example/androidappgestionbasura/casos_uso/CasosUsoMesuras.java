package com.example.androidappgestionbasura.casos_uso;

import android.app.Activity;
import android.util.Log;

import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.datos.preferences.SharedPreferencesHelper;
import com.example.androidappgestionbasura.model.bolsas_basura.BolsaBasura;
import com.example.androidappgestionbasura.model.bolsas_basura.ListaBolsaBasura;
import com.example.androidappgestionbasura.model.mesuras_dispositivos.ListaMesuras;
import com.example.androidappgestionbasura.model.mesuras_dispositivos.Mesura;
import com.example.androidappgestionbasura.presentacion.LoadingDialogActivity;
import com.example.androidappgestionbasura.repository.impl.MesurasRepositorioImpl;

/**
 * 26/11/2020 Rubén Pardo
 * Gestion de las mesuras
 */
public class CasosUsoMesuras {
    private Activity actividad;
    private MesurasRepositorioImpl mesurasRepository;
    private LoadingDialogActivity loadingDialogActivity;

    private ListaBolsaBasura bolsaBasuras;
    private ListaMesuras listaMesuras;

    public CasosUsoMesuras(Activity activity) {
        actividad = activity;
        loadingDialogActivity = new LoadingDialogActivity(activity);
        mesurasRepository =  new MesurasRepositorioImpl();
    }


    public void getMesurasMensuales(final CallBack callBack){

        String uid =  SharedPreferencesHelper.getInstance().getUID();
        mesurasRepository.readBolsasBasurasMensualesByUID(uid, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                bolsaBasuras = (ListaBolsaBasura) object;
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    public ListaBolsaBasura getListaMesuras(){
        return bolsaBasuras;
    }

    public ListaBolsaBasura getBolsasBasura(){
        return bolsaBasuras;
    }

    public void getMesurasPorId(String id, final CallBack callBack){
        loadingDialogActivity.startLoadingDialog();

        //String id =  "24:6F:28:A0:90:80%basura"; //aqui almaceno la id de la basura

        mesurasRepository.readMesurasByID(id, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                loadingDialogActivity.dismissDialog();
                listaMesuras = (ListaMesuras) object;
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                loadingDialogActivity.dismissDialog();
                callBack.onError(object);
            }
        });
    };
}
