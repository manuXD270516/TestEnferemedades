package com.example.testenfermedades.testenferemedades;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

/**
 * Created by USUARIO on 09/11/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    //RemoteViews remoteView;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        try {
            mostrarNotificacion(remoteMessage.getData().get("titulo"),
                    remoteMessage.getData().get("message"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //mostrarNotificacion(remoteMessage.getData().get("message"));
    }

    /*private void mostrarNotificacion(String titulo, String message, String urlimagen, String resto) throws IOException {
//        remoteView = new RemoteViews(getPackageName(), R.layout.notificacion);
//        remoteView.setImageViewResource(R.id.ivImagenNotif, R.drawable.nada);
//        remoteView.setTextViewText(R.id.tvNotifTitel,titulo );
//        remoteView.setTextViewText(R.id.tvDescNotif,message );*

        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent viewPublicationIntent = PendingIntent.getActivity(this, 2, new Intent(getApplicationContext(), Publicaciones.class), PendingIntent.FLAG_UPDATE_CURRENT);
      //  PendingIntent listenIntent = PendingIntent.getActivity(this, 3, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentTitle(titulo)
                .setTicker(getString(R.string.app_name))
                .setDefaults(-1)
                .setContentText(message)
                .setSmallIcon(R.drawable.fondo_principal) //common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent)
                .setSound(defaultSound)
                .addAction(R.drawable.eye, "Ver Publicación", viewPublicationIntent)
                //.addAction(android.R.drawable.btn_star_big_on,"Escuchar", listenIntent)
                //.addAction(android.R.drawable.ic_menu_share, "Share", shareIntent)
                .setLights(Color.GREEN, 2000, 5000)
                .setPriority(NotificationCompat.PRIORITY_HIGH); // definir la prioridad de la app (visibildad inmediata)

        if (urlimagen.length()>0){
            NotificationCompat.BigPictureStyle notifBig = new NotificationCompat.BigPictureStyle(builder);
            int posSladsh = urlimagen.lastIndexOf('/');
            urlimagen = urlimagen.substring(0, posSladsh) + "%2F" + urlimagen.substring(posSladsh + 1);
            String urlCompleta = urlimagen + "&" + resto;
            //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.nada);
            //Uri x=Uri.parse(urlimagen);
            //String nada="https://firebasestorage.googleapis.com/v0/b/notificacion-prueba-c6f9c.appspot.com/o/prueba%2F1510763830836.png?alt=media&token=94e8901f-b3f8-4b9f-8009-89b6cf8b6955";
            //Bitmap imagen = getBitwmapFromUri(x);
            Bitmap image = null;//getBitmapFromURL(urlimagen);
            try {
                URL url = new URL(urlCompleta);
                //image=BitmapFactory.decodeFile(urlimagen);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                System.out.println(e);
            }

            notifBig.bigPicture(image)
                    .bigLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ico))
                    .setBigContentTitle(titulo)
                    .setSummaryText(message)
                    .build();

        }
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(new Random().nextInt(), builder.build());

    }*/
    // NOTIFICACIONES PARA VERSIONES ANDROID 8.0 EN ADELANTE
    NotificationCompat.Builder mBuilder;
    String channelId="miCanalNotificable";

    private void mostrarNotificacion(String titulo, String message) throws IOException {
        NotificationManager notificationManager=(NotificationManager)getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        mBuilder=new NotificationCompat.Builder(this,null);
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            int importancia= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel=new NotificationChannel(channelId,titulo,importancia);
            mChannel.setDescription(message);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            notificationManager.createNotificationChannel(mChannel);
            mBuilder=new NotificationCompat.Builder(this,channelId);
        }

        mBuilder.setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle(titulo)
                .setContentIntent(pendingIntent)
                .setContentText(message);

        NotificationCompat.BigPictureStyle notifBig = new NotificationCompat.BigPictureStyle(mBuilder);
        /*if (urlimagen.length()>0){
            int posSladsh = urlimagen.lastIndexOf('/');
            urlimagen = urlimagen.substring(0, posSladsh) + "%2F" + urlimagen.substring(posSladsh + 1);
            String urlCompleta = urlimagen + "&" + resto;
            //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.nada);
            //Uri x=Uri.parse(urlimagen);
            //String nada="https://firebasestorage.googleapis.com/v0/b/notificacion-prueba-c6f9c.appspot.com/o/prueba%2F1510763830836.png?alt=media&token=94e8901f-b3f8-4b9f-8009-89b6cf8b6955";
            //Bitmap imagen = getBitwmapFromUri(x);
            Bitmap image = null;//getBitmapFromURL(urlimagen);
            try {
                URL url = new URL(urlCompleta);
                //image=BitmapFactory.decodeFile(urlimagen);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                System.out.println(e);
            }
        }*/

        Bitmap imagenNotif=BitmapFactory.decodeResource(getResources(), R.drawable.imgfondo);
        notifBig.bigPicture(imagenNotif)
                .bigLargeIcon(imagenNotif)
                .setBigContentTitle(titulo)
                .setSummaryText(message)
                .build();

        notificationManager.notify(new Random().nextInt(1000),mBuilder.build());

        /*remoteView = new RemoteViews(getPackageName(), R.layout.notificacion);
        remoteView.setImageViewResource(R.id.ivImagenNotif, R.drawable.nada);
        remoteView.setTextViewText(R.id.tvNotifTitel,titulo );
        remoteView.setTextViewText(R.id.tvDescNotif,message );*/

        /*Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent viewPublicationIntent = PendingIntent.getActivity(this, 2, new Intent(getApplicationContext(), Publicaciones.class), PendingIntent.FLAG_UPDATE_CURRENT);
        //  PendingIntent listenIntent = PendingIntent.getActivity(this, 3, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentTitle(titulo)
                .setTicker(getString(R.string.app_name))
                .setDefaults(-1)
                .setContentText(message)
                .setSmallIcon(R.drawable.fondo_principal) //common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent)
                .setSound(defaultSound)
                .addAction(R.drawable.eye, "Ver Publicación", viewPublicationIntent)
                //.addAction(android.R.drawable.btn_star_big_on,"Escuchar", listenIntent)
                //.addAction(android.R.drawable.ic_menu_share, "Share", shareIntent)
                .setLights(Color.GREEN, 2000, 5000)
                .setPriority(NotificationCompat.PRIORITY_HIGH); // definir la prioridad de la app (visibildad inmediata)

        if (urlimagen.length()>0){
            NotificationCompat.BigPictureStyle notifBig = new NotificationCompat.BigPictureStyle(builder);
            int posSladsh = urlimagen.lastIndexOf('/');
            urlimagen = urlimagen.substring(0, posSladsh) + "%2F" + urlimagen.substring(posSladsh + 1);
            String urlCompleta = urlimagen + "&" + resto;
            //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.nada);
            //Uri x=Uri.parse(urlimagen);
            //String nada="https://firebasestorage.googleapis.com/v0/b/notificacion-prueba-c6f9c.appspot.com/o/prueba%2F1510763830836.png?alt=media&token=94e8901f-b3f8-4b9f-8009-89b6cf8b6955";
            //Bitmap imagen = getBitwmapFromUri(x);
            Bitmap image = null;//getBitmapFromURL(urlimagen);
            try {
                URL url = new URL(urlCompleta);
                //image=BitmapFactory.decodeFile(urlimagen);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                System.out.println(e);
            }

            notifBig.bigPicture(image)
                    .bigLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ico))
                    .setBigContentTitle(titulo)
                    .setSummaryText(message)
                    .build();

        }
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(new Random().nextInt(), builder.build());*/

    }



    // FUNCION PARA OBTENER EL BITMAP


//Fuente: https://developer.android.com/guide/topics/providers/document-provider.html#open

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /*public  Bitmap getBitmapFromURL(String src) {


        /*try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            String erro=e.getMessage().toString();
            e.printStackTrace();
            return null;
        }
    }*/
}


