package com.example.androidappgestionbasura.casos_uso;

import android.app.Activity;
import android.util.Log;

import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.datos.preferences.SharedPreferencesHelper;
import com.example.androidappgestionbasura.model.bolsas_basura.BolsaBasura;
import com.example.androidappgestionbasura.model.bolsas_basura.ListaBolsaBasura;
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

    public CasosUsoMesuras(Activity activity) {
        actividad = activity;
        loadingDialogActivity = new LoadingDialogActivity(activity);
        mesurasRepository =  new MesurasRepositorioImpl();
    }


    public void getMesurasMensuales(final CallBack callBack){
        loadingDialogActivity.startLoadingDialog();
        String uid =  SharedPreferencesHelper.getInstance().getUID();
        mesurasRepository.readBolsasBasurasMensualesByUID(uid, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                loadingDialogActivity.dismissDialog();
                bolsaBasuras = (ListaBolsaBasura) object;

                for(BolsaBasura bolsaBasura : bolsaBasuras.getBolsasBasuraList()){
                    Log.d("Dato----",bolsaBasura.getTipo());
                    Log.d("Dato",bolsaBasura.getLlenado()+"");
                    Log.d("Dato",bolsaBasura.getFecha()+"");
                }

                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                loadingDialogActivity.dismissDialog();
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
}
