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
import com.example.androidappgestionbasura.model.ListaDispositivos;
import com.example.rparcas.mqtt.Mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.rparcas.mqtt.Mqtt.topicRoot;

public class ServicioNotificacionesMqtt extends Service implements MqttCallback {

    private static final String TAG = "NotificationMqttService";
    private static final String CHANNEL_ID = "CHANNEL-1";

    private MqttClient client = null;

    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind()" );
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate() , service started...");
        Toast.makeText(this, "Service created...", Toast.LENGTH_SHORT).show();
        conectarMQTT();
        super.onCreate();

    }

    private void conectarMQTT(){
        // enviar cuando se desconecta
        try {
            client = new MqttClient(Mqtt.broker, Mqtt.clientId, new
                    MemoryPersistence());

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(60);
            connOpts.setWill(topicRoot+"WillTopic", "App desconectada".getBytes(),Mqtt.qos, false);
            client.connect(connOpts);

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public int onStartCommand(Intent intent, int flags, int startId) {

//        listaIdDispositivos = intent.getCharSequenceArrayListExtra("idDispositivos");

        Toast.makeText(this, "Service started...", Toast.LENGTH_SHORT).show();



        // subscribirse a topic
        try {
            Log.i(Mqtt.TAG, "Suscrito a " + topicRoot+"24:6F:28:A0:90:80%basura/WillTopic");
            client.subscribe(topicRoot+"24:6F:28:A0:90:80%basura/WillTopic", Mqtt.qos);
            client.setCallback(this);
        } catch (MqttException e) {
            Log.e(Mqtt.TAG, "Error al suscribir.", e);
        }

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

    @Override
    public void connectionLost(Throwable cause) {
        Log.d(Mqtt.TAG,"conexion perdida");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        Log.d("MQTT","llega");


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Dispositivo desconectado")
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


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
