package com.example.androidappgestionbasura.presentacion;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.androidappgestionbasura.R;

public class LoadingDialogActivity {



    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialogActivity(Activity myActivity){
        activity = myActivity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_layout,null));
        builder.setCancelable(false);

        dialog = builder.create();

        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }

}
