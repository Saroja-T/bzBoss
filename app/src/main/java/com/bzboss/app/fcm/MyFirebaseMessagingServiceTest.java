package com.bzboss.app.fcm;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bzboss.app.R;
import com.bzboss.app.activity.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;


public class MyFirebaseMessagingServiceTest extends FirebaseMessagingService {

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String APS = "aps"; //because of IOS notification style
    public static final String ALERT = "alert";
    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final String POST_SLUG = "post_slug";
    public static final String BADGE = "badge";
    public static final String SUBCATEGORY_ID = "subcategory_id";
    private static final String TAG = "MyFirebaseMessagingServiceTest";
    private Intent intent;
    public static final int NOTIFICATION_ID = 1;
    //  private SessionManager mySession;
    private String NOTI_TYPE;
    private NotificationManager notificationManager;

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        Log.d(TAG, "Message data payload: " + remoteMessage.getNotification());


        //Log.e(TAG, "TEST notification : " + remoteMessage.getNotification().getBody());


        Log.e(TAG, "TEST Data Payload : " + remoteMessage.getNotification().getTitle());
        Log.e(TAG, "TEST Data Payload : " + remoteMessage.getNotification().getClickAction());
        Log.e(TAG, "TEST Data Payload : " + remoteMessage.getNotification().getBody());
        Log.e(TAG, "TEST Data Payload-Image : " + remoteMessage.getNotification().getImageUrl());
       /* Log.i("PVL", "MESSAGE RECEIVED!!");
        if (remoteMessage.getNotification().getBody() != null) {
            Log.i("PVL", "RECEIVED MESSAGE: " + remoteMessage.getNotification().getBody());
        } else {
            Log.i("PVL", "RECEIVED MESSAGE: " + remoteMessage.getData().get("message"));
        }*/

        /*if (remoteMessage.getNotification().getBody() != null) {
            Log.i("PVL", "RECEIVED MESSAGE: " + remoteMessage.getNotification().getBody());
        } else {
            Log.i("PVL", "RECEIVED MESSAGE: " + remoteMessage.getData().get("body"));
        }*/
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

       /* if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }*/


//        Log.e("TAG", "remoteMessage==> " + remoteMessage.getNotification().getTitle());
//        Log.e("TAG", "remoteMessage==> " + remoteMessage.getNotification().getClickAction());
//        Log.e("TAG", "remoteMessage==> " + remoteMessage.getNotification());


        Log.e("TAG", "remoteMessage data==> " + remoteMessage.getData());
//        Log.e("TAG", "remoteMessage==> " + remoteMessage.getNotification().getClickAction());
//        Log.e("TAG", "remoteMessage==> " + remoteMessage.getNotification());




        /*Log.e("TAG","BACKGROUD DATA GET");
        JSONObject object = new JSONObject(remoteMessage.getData());
        Log.e("TAG", "VALUE OF STRING get data from json object" + object.toString());
        Map<String, String> data = remoteMessage.getData();
        // Log.e("TAG","VALUE OF STRING"+data.toString());
        try {
            Log.e(TAG, "Clcik Action: " + object.get("click_action"));
            Log.e(TAG, "getTitle Action: " + object.get("title"));
            Log.e(TAG, "getChannelId Action: " + object.get("body"));
            Log.e(TAG, "getBody: " + object.get("android_channel_id"));
        } catch (Exception e) {
            //Log.e(TAG, e.getMessage());
        }*/
        // String myCustomKey = data.get("body");


        // Log.e(TAG, "Backgroud Ma bhai msg maliyo: " + remoteMessage.getNotification());
        JSONObject object = null;
        String mAction = "";
        String msg_id="";
        String symptoms_name="";
        try {
            Map<String, String> data = remoteMessage.getData();

            // object = new JSONObject(remoteMessage.getData());
            // Log.e(TAG, "NEW TEST Payload : " + object.get("click_action"));


        /*    Log.e(TAG, "TEST Data Payload getNotification: " + remoteMessage.getNotification().getTitle());
            Log.e(TAG, "TEST Data Payload getNotification: " + remoteMessage.getNotification().getBody());
            Log.e(TAG, "TEST Data Payload getNotification: " + remoteMessage.getNotification().getClickAction());
*/


            Log.e(TAG, "TEST Data Payload : " + data.get("msg_title"));
            Log.e(TAG, "TEST Data Payload : " + data.get("msg_body"));
            Log.e(TAG, "TEST Data Payload : symptoms_id" + data.get("symptoms_id"));
            Log.e(TAG, "TEST Data Payload msg_type: " + data.get("click_action"));

          /*  object = new JSONObject(remoteMessage.getData().get("message"));
            mAction = object.getJSONObject("data").optString("click_action");
          */
            //Log.e(TAG, "TEST Data Payload : " + remoteMessage.getNotification().getClickAction());
            mAction = data.get("click_action");
            msg_id=data.get("symptoms_id");
            symptoms_name=data.get("symptoms_name");
            Log.e(TAG, "Data Payload : " + data.get("msg_click_action"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // sendNotification(myCustomKey);


        //if (remoteMessage.getData().get("data"))


      /*  Log.e(TAG, "NEW TEST Payload : " + remoteMessage.getData().get("title"));
        Log.e(TAG, "NEW TEST Payload : " + remoteMessage.getData().get("body"));
        Log.e(TAG, "NEW TEST Payload : " +remoteMessage.getData().get("body"));*/
        //sendNotification(remoteMessage.getData().get("msg_title"), remoteMessage.getData().get("msg_body"), mAction,msg_id,symptoms_name,remoteMessage.getNotification().getIcon());
        sendNotification(remoteMessage.getData().get("msg_title"), remoteMessage.getData().get("msg_body"), mAction,msg_id,symptoms_name, String.valueOf(remoteMessage.getNotification().getImageUrl()));
        /*
        if (!isAppIsInBackground(getApplicationContext())) {
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }else
        {
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }*/


    }

    @SuppressLint("LongLogTag")
    @Override
    public void onNewToken(@NotNull String newToken) {
        super.onNewToken(newToken);
        Log.e(TAG, "Service File Refreshed Token ===> " + newToken);
    }

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        /*OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance(this).beginWith(work).enqueue();*/
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    @SuppressLint("LongLogTag")
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendNotification(String tittle, String messageBody, String clickAction, String msg_id,String symptoms_name,String image_url) {
        Random random = new Random();
        int Low = 10;
        int High = 100;
        int notificationId = random.nextInt(High - Low) + Low;

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);


        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("click_action", clickAction);
        intent.putExtra("symptoms_id", msg_id);
        intent.putExtra("symptoms_name", symptoms_name);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100) /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "M_CH_ID")
                //.setSmallIcon(getNotificationIcon())
                .setSmallIcon(R.drawable.ic_app)
                .setLargeIcon(getBitmapFromURL(image_url))
                .setContentTitle(tittle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(this, R.color.background))
                .setColorized(true)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(image_url)))
//                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        //if(!silent)
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int i = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            i = notificationManager.getImportance();
        }
        boolean soundAllowed = i < 0 || i >= NotificationManager.IMPORTANCE_DEFAULT;
       /* MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(this, defaultSoundUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    mp.release();
                }
            });
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel =
                    new NotificationChannel("1", "bzBoss", importance);


            notificationChannel.enableLights(true);
            //   notificationChannel.setLightColor(Color.WHITE);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);

            notificationBuilder.setLargeIcon(getBitmapFromURL(image_url));
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            notificationBuilder.setChannelId("1");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        notificationManager.notify(NOTIFICATION_ID /* ID of notification */, notificationBuilder.build());
    }
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }

}