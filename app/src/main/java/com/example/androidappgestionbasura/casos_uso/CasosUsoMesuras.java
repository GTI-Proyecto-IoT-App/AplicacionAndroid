package com.example.androidappgestionbasura.casos_uso;

import android.app.Activity;

import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.datos.preferences.SharedPreferencesHelper;
import com.example.androidappgestionbasura.model.mesuras_dispositivos.ListaMesuras;
import com.example.androidappgestionbasura.presentacion.LoadingDialogActivity;
import com.example.androidappgestionbasura.repository.impl.MesurasRepositorioImpl;

import androidx.fragment.app.FragmentActivity;

/**
 * 26/11/2020 Rubén Pardo
 * Gestion de las mesuras
 */
public class CasosUsoMesuras {
    private Activity actividad;
    private MesurasRepositorioImpl mesurasRepository;
    private LoadingDialogActivity loadingDialogActivity;

    private ListaMesuras listaMesuras;


    public CasosUsoMesuras(Activity activity) {
        actividad = activity;
        loadingDialogActivity = new LoadingDialogActivity(activity);
        mesurasRepository =  new MesurasRepositorioImpl();
    }


    public void getMesurasAnuales(final CallBack callBack){
        loadingDialogActivity.startLoadingDialog();
        String uid =  SharedPreferencesHelper.getInstance().getUID();
        mesurasRepository.readMesurasAnualesByUID(uid, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                loadingDialogActivity.dismissDialog();
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                loadingDialogActivity.dismissDialog();
                callBack.onError(object);
            }
        });
    }

    public ListaMesuras getListaMesuras() {
        return listaMesuras;
    }
}
