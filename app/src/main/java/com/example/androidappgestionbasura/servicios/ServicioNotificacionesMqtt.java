package com.example.androidappgestionbasura.servicios;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.androidappgestionbasura.R;

import java.util.Random;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ServicioNotificacionesMqtt extends Service {

    private static final String TAG = "NotificationMqttService";
    private static final String CHANNEL_ID = "CHANNEL-1";

    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind()" );
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service created...", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onCreate() , service started...");

    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Service started...", Toast.LENGTH_SHORT).show();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.buttonphoto)
                .setContentTitle("SERVICIO")
                .setContentText("xd")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "CHANNEL-1", importance);
            channel.setDescription("description");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);

        }

        Random random = new Random();
        notificationManager.notify(random.nextInt(Integer.MAX_VALUE), builder.build());



        return Service.START_STICKY;
    }




    public IBinder onUnBind(Intent arg0) {
        Log.i(TAG, "onUnBind()");
        return null;
    }

    public void onStop() {
        Log.i(TAG, "onStop()");
    }
    public void onPause() {
        Log.i(TAG, "onPause()");
    }
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service stopped...", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onCreate() , service stopped...");
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
    }

}
