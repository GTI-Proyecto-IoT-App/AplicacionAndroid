package com.example.androidappgestionbasura.servicios;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoNotificacion;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.notificaciones.Notificacion;
import com.example.androidappgestionbasura.model.notificaciones.TipoNotificacion;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivosFirestoreUI;
import com.example.androidappgestionbasura.utility.AppConf;
import com.example.rparcas.mqtt.Mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;
import static com.example.rparcas.mqtt.Mqtt.topicRoot;

public class ServicioNotificacionesMqtt extends Service implements MqttCallback {

    private static final String TAG = "NotificationMqttService";
    private static final String CHANNEL_ID1 = "CHANNEL-1";
    private static final String CHANNEL_ID2 = "CHANNEL-2";
    private static final int NOTIFICATION_ID = 1233;
    private NotificationManager notificationManager;
    private MqttClient client = null;


    private CasosUsoNotificacion casosUsoNotificacion;


    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind()" );
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate() , service started...");

        String uid = ((AppConf) getApplication()).getUsuario().getUid();
        casosUsoNotificacion = new CasosUsoNotificacion(uid);

        conectarMQTT();

        empezarServicioPrimerPlano();

        super.onCreate();

    }

    private void empezarServicioPrimerPlano() {


        String channelid = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? createNotificationChannel("my_service", "My Background Service")
                : "";


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelid);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();


        startForeground(101,notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId , String channelName){
        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }

    private void conectarMQTT(){
        // enviar cuando se desconecta
        try {
            client = new MqttClient(Mqtt.broker, Mqtt.clientId, new
                    MemoryPersistence());

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(60);
            //connOpts.setWill(topicRoot+"WillTopic", "App desconectada".getBytes(),Mqtt.qos, false);
            client.connect(connOpts);

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public int onStartCommand(Intent intent, int flags, int startId) {

//        listaIdDispositivos = intent.getCharSequenceArrayListExtra("idDispositivos");

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

    @Override
    public void onDestroy() {
        Log.i(TAG, "onCreate() , service stopped...");
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.d(Mqtt.TAG,"conexion perdida");
        conectarMQTT();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message){

        Log.d("MQTT","llega: "+message);

        // obtenemos que dispostivo se ha desconectado por su id que se envia en el mensaje del will topic
        // TODO Cambiar el will topic a que envie su id de tal forma que se sepa que se ha desconectado, mqtt si ve que no esta conectado envia "disconnected"
        // TODO enviar por ejemplo: "dispositivo$$desconectado$$<id>"
        AdaptadorDispositivosFirestoreUI adaptador = ((AppConf) getApplication()).adaptador;
        Dispositivo dispositivo = adaptador.getItem(adaptador.getPos("24:6F:28:A0:90:80%basura"));


        enviarNotifiacacion(dispositivo);
        guardarNotifiacacionFirestore(dispositivo);

    }

    private void guardarNotifiacacionFirestore(Dispositivo dispositivo) {
        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(System.currentTimeMillis());
        notificacion.setIdDispositivo(dispositivo.getId());

        notificacion.setNombreDispositivo(dispositivo.getNombre());
        notificacion.setTipo(TipoNotificacion.DESCONECTADO);

        casosUsoNotificacion.enviarNotificacionFirestore(notificacion);
    }

    private void enviarNotifiacacion(Dispositivo dispositivo) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID1)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Dispositivo desconectado")
                .setContentText("El dispositivo: "+dispositivo.getNombre()+" no tiene conexión")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID1, "CHANNEL-1", importance);
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
