package com.sales.darksheet.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.sales.darksheet.R;
import com.sales.darksheet.StartApp;
import com.sales.darksheet.connection.ConnectionIO;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MyJobIntentService extends JobIntentService {

    private NotificationManager notificationManager;
    private String channelId;
    private Socket socket;
    private String email;

    public static void startJobService(Context context, Intent intent, int jobId) {
        enqueueWork(context, MyJobIntentService.class, jobId, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        startSocket();
        createSocketEvents();
        reloadConnection();
    }

    private void reloadConnection() {
        try {
            Thread.sleep(500);
            if (!socket.connected()) {
                startSocket();
                createSocketEvents();
            }
            reloadConnection();
        } catch (Exception e) {}
    }

    private void startSocket() {
        socket = new ConnectionIO().connect();
        email = "lucasdevsoftware@gmail.com";
        socket.emit("notification_socket_record", email);
    }

    int cont = 0;

    private void createSocketEvents() {
        /*
        while (true) {
            try {
                cont++;
                Thread.sleep(2000);
                try {
                    //JSONObject msgObject = new JSONObject(String.valueOf(args[0]));
                    //System.out.println("SERVICE _>> " + msgObject);
                    //String contact = msgObject.getString("contact");
                    //String msg = msgObject.getString("msg");
                    int id = (int) (Math.random() * 1000);
                    channelId = "my_channel_id_" + id;
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                            id, new Intent(getApplicationContext(), StartApp.class), PendingIntent.FLAG_UPDATE_CURRENT);

                    createChannel();

                    // Código para versões a partir do android 8 (API 26)
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
                    builder.setAutoCancel(true)
                            .setTicker("Nova mensagem no DarkSheet!")
                            .setContentTitle("TITLE"+cont)
                            .setContentText("MSG"+cont)
                            .setSmallIcon(R.drawable.icon_1_sem_fundo)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.user_64))
                            .setContentIntent(pendingIntent);
                    Notification notification = builder.build();
                    notificationManager.notify(id, notification);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

        socket.on("NOTIFY_NEW_MSG", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject msgObject = new JSONObject(String.valueOf(args[0]));
                    System.out.println("SERVICE _>> " + msgObject);
                    Log.d("SERVICE _>> ", msgObject.toString());
                    String contact = msgObject.getString("contact");
                    String msg = msgObject.getString("msg");
                    int id = (int) (Math.random() * 1000);
                    channelId = "my_channel_id_" + id;
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                            id, new Intent(getApplicationContext(), StartApp.class), PendingIntent.FLAG_UPDATE_CURRENT);

                    createChannel();

                    // Código para versões a partir do android 8 (API 26)
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
                    builder.setAutoCancel(true)
                            .setTicker("Nova mensagem no DarkSheet!")
                            .setContentTitle(contact)
                            .setContentText(msg)
                            .setSmallIcon(R.drawable.icon_1_sem_fundo)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.user_64))
                            .setContentIntent(pendingIntent);
                    Notification notification = builder.build();
                    notificationManager.notify(id, notification);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createChannel() {
        CharSequence name = "channel";
        String description = "description";
        int importance = NotificationManager.IMPORTANCE_MAX;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(channelId, name, importance);
            notificationChannel.setDescription(description);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
