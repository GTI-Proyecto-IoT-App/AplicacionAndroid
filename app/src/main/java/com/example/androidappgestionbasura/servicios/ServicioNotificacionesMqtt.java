package com.example.androidappgestionbasura.servicios;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoDispositivo;
import com.example.androidappgestionbasura.casos_uso.CasosUsoNotificacion;
import com.example.androidappgestionbasura.datos.preferences.SharedPreferencesHelper;
import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.Usuario;
import com.example.androidappgestionbasura.model.notificaciones.Notificacion;
import com.example.androidappgestionbasura.model.notificaciones.TipoNotificacion;
import com.example.androidappgestionbasura.presentacion.HomeActivityPackage.HomeActivity;
import com.example.androidappgestionbasura.presentacion.RatailerStartUpScreenActivity;
import com.example.androidappgestionbasura.presentacion.adapters.AdaptadorDispositivosFirestoreUI;
import com.example.androidappgestionbasura.repository.impl.DispositivosRepositoryImpl;
import com.example.androidappgestionbasura.utility.AppConf;
import com.example.rparcas.mqtt.Mqtt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;
import static com.example.rparcas.mqtt.Mqtt.topicRoot;

public class ServicioNotificacionesMqtt extends Service implements MqttCallback {

    private static final String TAG = "NotificationMqttService";
    private static final String CHANNEL_ID1 = "CHANNEL-1";
    private MqttClient client = null;


    private CasosUsoNotificacion casosUsoNotificacion;
    private List<Dispositivo> dispositivoList;



    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind()" );
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate() , service started...");


        conectarMQTT();

        empezarServicioPrimerPlano();

        super.onCreate();

    }

    private void addSnapshotListenerDispositivos(String ui) {

        Log.d(Mqtt.TAG,"query");
        Query query = new DispositivosRepositoryImpl().getDispositvosVinculados(ui);

        // obtener todos los dispositivos
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value!=null){
                    List<Dispositivo> dispositivosDelQuery = new ArrayList<>();
                    for(DocumentSnapshot d : value.getDocuments()){
                        Dispositivo disp = d.toObject(Dispositivo.class);
                        dispositivosDelQuery.add(disp);
                    }

                    if(dispositivoList == null){
                        // se llama por primera vez
                        dispositivoList = new ArrayList<Dispositivo>();
                        dispositivoList.addAll(dispositivosDelQuery);
                        // conectar todos los dispositivos
                        for(Dispositivo dispositivo : dispositivoList){
                            conectarDispositivoMQTT(dispositivo);
                        }

                    }else{

                        if(dispositivoList.size() > dispositivosDelQuery.size()){
                            // se ha borrado un dispositivo
                            Dispositivo dispositivoBorrado;

                            for(Dispositivo d : dispositivoList){
                                boolean isBorrado = true;
                                for(Dispositivo dList : dispositivosDelQuery){
                                    if (d.getId().equals(dList.getId())) {
                                        isBorrado = false;
                                        break;
                                    }
                                }
                                if(isBorrado){
                                    dispositivoBorrado = d;
                                    dispositivoList.remove(dispositivoBorrado);
                                    // desvincularlo
                                    desconectarDispositivoMQTT(dispositivoBorrado);
                                    break;
                                }
                            }


                        }
                        else if(dispositivoList.size() < dispositivosDelQuery.size()){
                            // se ha añadido un dispositivo
                            Dispositivo dispositivoNuevo;

                            for(Dispositivo d : dispositivosDelQuery){
                                boolean isNuevo = true;

                                for(Dispositivo dList : dispositivoList){
                                    if (d.getId().equals(dList.getId())) {
                                        isNuevo = false;
                                        break;
                                    }

                                }
                                if(isNuevo){
                                    dispositivoNuevo = d;
                                    dispositivoList.add(dispositivoNuevo);
                                    // vincularlo
                                    conectarDispositivoMQTT(dispositivoNuevo);
                                    break;
                                }
                            }

                        }

                    }


                }

            }
        });


    }

    private void desconectarDispositivoMQTT(Dispositivo dispositivoBorrado) {
        try {
            Log.i(Mqtt.TAG, "Desuscrito a " + topicRoot+ dispositivoBorrado.getId() + "/WillTopic");
            client.unsubscribe( topicRoot+ dispositivoBorrado.getId() + "/WillTopic");
            client.setCallback(this);
        } catch (MqttException e) {
            Log.e(Mqtt.TAG, "Error al desuscribir.", e);
        }
    }

    private void conectarDispositivoMQTT(Dispositivo dispositivoNuevo) {
        try {

            Log.i(Mqtt.TAG, "Suscrito a " + topicRoot+ dispositivoNuevo.getId() + "/WillTopic");
            client.subscribe( topicRoot+ dispositivoNuevo.getId() + "/WillTopic", Mqtt.qos);
            client.setCallback(this);

        } catch (MqttException e) {
            Log.e(Mqtt.TAG, "Error al suscribir.", e);
        }
    }

    private void empezarServicioPrimerPlano() {


        String channelid = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? createNotificationChannel("my_service", "My Background Service")
                : "";


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelid);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_smart_house_black_48)
                .setColor(Color.RED)
                .setContentTitle(getString(R.string.title_servicio_notificaciones))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
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
            Log.d(Mqtt.TAG, "CONECTAR.");
            client = new MqttClient(Mqtt.broker, Mqtt.clientId, new
                    MemoryPersistence());

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(60);
            client.connect(connOpts);

        } catch (MqttException e) {
            Log.e(Mqtt.TAG, "ERROR CONECTAR.",e);
            e.printStackTrace();
        }

    }

    public int onStartCommand(Intent intent, int flags, int startId) {



        SharedPreferencesHelper.initializeInstance(getApplicationContext());

        String ui = SharedPreferencesHelper.getInstance().getUID();
        casosUsoNotificacion = new CasosUsoNotificacion(ui,null);


        addSnapshotListenerDispositivos(ui);

        return Service.START_STICKY;
    }




    public IBinder onUnBind(Intent arg0) {
        Log.i(TAG, "onUnBind()");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy() , service stopped...");
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.d(Mqtt.TAG,"conexion perdida");
        conectarMQTT();

        // volver a subscribirse
        for(Dispositivo d : dispositivoList){
            conectarDispositivoMQTT(d);
        }

    }

    @Override
    public void messageArrived(String topic, MqttMessage message){



        String idDisp = topic.split("/")[2];//proyectoGTI2A/dispositivo/25:6F:28:A0:90:80%basura/WillTopic -> 25:6F:28:A0:90:80%basura
        boolean isConectado = message.toString().equals("conectado"); // message -> conectado | disconected
        Log.d("MQTT","De: "+idDisp+" llega: "+message);
        // obtenemos que dispostivo se ha desconectado por su id que se envia en el mensaje del will topic


        for(Dispositivo d : dispositivoList){
            if(d.getId().equals(idDisp)){
                enviarNotifiacacion(d,isConectado);
                guardarNotifiacacionFirestore(d,isConectado);
                break;
            }
        }



    }

    private void guardarNotifiacacionFirestore(Dispositivo dispositivo,boolean isConectado) {
        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(System.currentTimeMillis());
        notificacion.setIdDispositivo(dispositivo.getId());

        notificacion.setNombreDispositivo(dispositivo.getNombre());
        notificacion.setTipo(isConectado ? TipoNotificacion.CONECTADO : TipoNotificacion.DESCONECTADO);

        casosUsoNotificacion.enviarNotificacionFirestore(notificacion);
    }

    private void enviarNotifiacacion(Dispositivo dispositivo,boolean isConectado) {

        String titile = isConectado
                ? getString(R.string.titulo_notif_dispositivo_con_conexion )
                : getString(R.string.titulo_notif_dispositivo_sin_conexion );
        String content = isConectado
                ? getString(R.string.content_notif_dispositivo_con_conexion ).replace("$",dispositivo.getNombre())
                : getString(R.string.content_notif_dispositivo_sin_conexion ).replace("$",dispositivo.getNombre());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID1)
                .setSmallIcon(isConectado ? R.drawable.ic_smart_trash_conectado : R.drawable.ic_smart_trash_no_conectado)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle(titile)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID1, "Estado de conexión de los dispositivos", importance);
            channel.setDescription("Notificaciones que avisan del estado de conexión de los dispositivos vinculados");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);

        }
        Random random = new Random();
        int idNotificacion = random.nextInt(Integer.MAX_VALUE-1)+1;// entre 1 y MAX;

        // ir siempre a la primera actividad asi podemos comprobar si hay acceso
        Intent intentNotificaciones = new Intent(this, RatailerStartUpScreenActivity.class);


        // ponemos la id de la notifiacion, asi al pulsarlo podemos coger el id y hacerle dismiss
        intentNotificaciones.putExtra(HomeActivity.INTENT_KEY_ABRIR_NOTIFICACIONES,idNotificacion);

        PendingIntent intencionPendiente = PendingIntent.getActivity(
                this, 0, intentNotificaciones, 0);

        builder.setContentIntent(intencionPendiente);


        notificationManager.notify(idNotificacion, builder.build());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
