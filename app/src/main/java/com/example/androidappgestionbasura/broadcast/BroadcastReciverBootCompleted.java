package com.example.androidappgestionbasura.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.androidappgestionbasura.servicios.ServicioNotificacionesMqtt;

public class BroadcastReciverBootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            context.startForegroundService(new Intent(context, ServicioNotificacionesMqtt.class));
        }else{
            Intent service = new Intent(context.getApplicationContext(),  ServicioNotificacionesMqtt.class);
            service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(service);
        }
    }
}
