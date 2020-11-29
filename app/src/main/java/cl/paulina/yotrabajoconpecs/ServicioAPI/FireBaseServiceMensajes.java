package cl.paulina.yotrabajoconpecs.ServicioAPI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat.Builder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import cl.paulina.yotrabajoconpecs.Amigos.ActivityAmigos;
import cl.paulina.yotrabajoconpecs.Preferences;
import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_empleador.Mensajeria;
import cl.paulina.yotrabajoconpecs.ui.libro.libroPDC;

public class FireBaseServiceMensajes extends FirebaseMessagingService {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String mensaje = remoteMessage.getData().get("mensaje");
        String hora = remoteMessage.getData().get("hora");
        String cabecera = remoteMessage.getData().get("cabecera");
        String cuerpo = remoteMessage.getData().get("cuerpo");
        String receptor = remoteMessage.getData().get("receptor");
        String emisorPHP = remoteMessage.getData().get("emisor");
        String url = remoteMessage.getData().get("url");
        String emisor = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_USUARIO_LOGIN);

        if(!emisorPHP.equals(receptor)) {
            String uno = "PaulinaLagosHenriquez";
            if (emisor.equals(uno)) {
                MensajeEmpleador(mensaje, hora, emisorPHP);
                showNotificationEmpleador(cabecera, cuerpo);
            } else {
                MensajePDC(mensaje, hora, emisorPHP, url);
                showNotificationPDC(cabecera, cuerpo);
            }
        }
    }

    private void MensajeEmpleador(String mensaje, String hora, String emisor){
        Intent i = new Intent(Mensajeria.MENSAJE);
        i.putExtra("key_mensaje",  mensaje);
        i.putExtra("key_hora",  hora);
        i.putExtra("key_emisor_PHP",  emisor);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    private void MensajePDC(String mensaje, String hora, String emisor, String url){
        Intent i = new Intent(libroPDC.MENSAJE);
        i.putExtra("key_mensaje",  mensaje);
        i.putExtra("key_hora",  hora);
        i.putExtra("key_emisor_PHP",  emisor);
        i.putExtra("key_url",  url);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationEmpleador(String cabecera, String cuerpo){
        Intent i = new Intent(this, Mensajeria.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

        Uri soundNotificacion = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String id ="channel_1";//id of channel
        String description = "123";//Description information of channel
        int importance = NotificationManager.IMPORTANCE_LOW;//The Importance of channel
        NotificationChannel channel = new NotificationChannel(id, "123", importance);//Generating channel
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        Notification notification = new Notification.Builder(FireBaseServiceMensajes.this,id)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle(cabecera)
                .setContentText(cuerpo)
                .setSound(soundNotificacion)
                .setSmallIcon(R.drawable.logo)
                .setTicker(cuerpo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(1,notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationPDC(String cabecera, String cuerpo){
        Intent i = new Intent(this, libroPDC.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

        Uri soundNotificacion = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String id ="channel_1";//id of channel
        String description = "123";//Description information of channel
        int importance = NotificationManager.IMPORTANCE_LOW;//The Importance of channel
        NotificationChannel channel = new NotificationChannel(id, "123", importance);//Generating channel
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        Notification notification = new Notification.Builder(FireBaseServiceMensajes.this,id)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle(cabecera)
                .setContentText(cuerpo)
                .setSound(soundNotificacion)
                .setSmallIcon(R.drawable.logo)
                .setTicker(cuerpo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(1,notification);
    }

}
