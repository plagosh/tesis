package cl.paulina.yotrabajoconpecs.ServicioAPI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import cl.paulina.yotrabajoconpecs.Preferences;
import cl.paulina.yotrabajoconpecs.R;
import cl.paulina.yotrabajoconpecs.MisClases.LibroEmpleador.MensajeriaFragment;
import cl.paulina.yotrabajoconpecs.MisClases.LibroPDC.LibroPDCFragment;

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
        String categoria = remoteMessage.getData().get("categoria");
        String emisor = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_USUARIO_LOGIN);

        if(!emisorPHP.equals(receptor)) {
            String uno = "PaulinaLagosHenriquez";
            if (emisor.equals(uno)) {
                MensajeEmpleador(mensaje, hora, emisorPHP);
                showNotificationEmpleador(cabecera, cuerpo);
            } else {
                MensajePDC(mensaje, hora, emisorPHP, url, categoria);
                showNotificationPDC(cabecera, cuerpo);
            }
        }
    }

    private void MensajeEmpleador(String mensaje, String hora, String emisor){
        Intent i = new Intent(MensajeriaFragment.MENSAJE);
        i.putExtra("key_mensaje",  mensaje);
        i.putExtra("key_hora",  hora);
        i.putExtra("key_emisor_PHP",  emisor);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    private void MensajePDC(String mensaje, String hora, String emisor, String url, String cat){
        Intent i = new Intent(LibroPDCFragment.MENSAJE);
        i.putExtra("key_mensaje",  mensaje);
        i.putExtra("key_hora",  hora);
        i.putExtra("key_emisor_PHP",  emisor);
        i.putExtra("key_url",  url);
        i.putExtra("key_categoria",  cat);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationEmpleador(String cabecera, String cuerpo){
        Intent i = new Intent(this, MensajeriaFragment.class);
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
        Intent i = new Intent(this, LibroPDCFragment.class);
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
