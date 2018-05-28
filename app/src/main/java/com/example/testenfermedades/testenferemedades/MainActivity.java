package com.example.testenfermedades.testenferemedades;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private ImageSwitcher imageSwitcher;
    private Timer timer = null;
    private int[] galleryDefault = {R.drawable.imgfondo,R.drawable.img1,R.drawable.img2,R.drawable.img3};
    private int position;
    private static final Integer DURATION = 4000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            iniciarComponentes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FirebaseMessaging.getInstance().subscribeToTopic("test");
            FirebaseInstanceId.getInstance().getToken();
        } catch (Exception e) {
            String error = e.getMessage().toString();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void iniciarComponentes() throws IOException {
        imageSwitcher=(ImageSwitcher)findViewById(R.id.imgSwTest);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imageView;
            }
        });
        Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
        imageSwitcher.setInAnimation(fadeIn);

        imageSwitcher.setOutAnimation(fadeOut);
        position=0;
        startSlider();
        // mostrar la notificacion momentanea
        mostrarNotificacion("TEST MEDICO","Debes realizar tu test periodicamente porfavor, revisa las fechas","","");
    }
    public void startSlider() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                // avoid exception:
                // "Only the original thread that created a view hierarchy can touch its views"
                runOnUiThread(new Runnable() {
                    public void run() {
                        //Glide.with(getApplicationContext()).load(galleryFirebase[position]).into(imageViewX);
                        imageSwitcher.setImageResource(galleryDefault[position]);
                        position++;
                        if (position==galleryDefault.length){
                            position=0;
                        }
                        /*if (galleryFirebase==null ||galleryFirebase.length==0){
                            imageSwitcher.setImageResource(galleryDefault[position]);
                            position++;
                            if (position == galleryDefault.length) {
                                position = 0;
                            }
                        } else {
                            System.out.println(position+" ...................");
                            String rutaImgDB=galleryFirebase[position2];
                            //String ff="https://firebasestorage.googleapis.com/v0/b/contaduria-6cc7f.appspot.com/o/presentacion_img%2Fimg+0_1516802925961.jpg?alt=media&token=ef396e8e-d575-4f7d-814c-d78e769461ac";
                            Glide.with(getApplicationContext()).load(rutaImgDB).into(imageViewX);
                            imageViewX.setVisibility(View.VISIBLE);
                            imageSwitcher.setImageDrawable(imageViewX.getDrawable());
                            imageViewX.setVisibility(View.GONE);
                            position2++;
                            if (position2==galleryFirebase.length){
                                position2=0;
                            }
                        }*/
                    }
                });
            }

        }, 0, DURATION);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            startSlider();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // NOTIFICACIONES PARA VERSIONES ANDROID 8.0 EN ADELANTE
    NotificationCompat.Builder mBuilder;
    String channelId="miCanalNotificable";

    private void mostrarNotificacion(String titulo, String message, String urlimagen, String resto) throws IOException {
        NotificationManager notificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this, null);
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importancia = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, titulo, importancia);
            mChannel.setDescription(message);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(mChannel);
            mBuilder = new NotificationCompat.Builder(this, channelId);
        }

        mBuilder.setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle(titulo)
                .setContentIntent(pendingIntent)
                .setContentText(message);

        NotificationCompat.BigPictureStyle notifBig = new NotificationCompat.BigPictureStyle(mBuilder);
        // No se esta trabajando con una imagen de grandes dimensiones
        /*int posSladsh = urlimagen.lastIndexOf('/');
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
        }*/
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.imgfondo);
        notifBig.bigPicture(icon)
                .bigLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.imgfondo))
                .setBigContentTitle(titulo)
                .setSummaryText(message)
                .build();
        notificationManager.notify(new Random().nextInt(1000), mBuilder.build());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
