package com.sales.darksheet.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import com.sales.darksheet.R;
import com.sales.darksheet.StartApp;
import com.sales.darksheet.connection.ConnectionIO;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class NotificationService extends Service implements Runnable {

    private NotificationManager notificationManager;
    private Socket socket;
    private String email;

    @Override
    public void onCreate() {
        new Thread(this).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        System.out.println("Serviço finalizado.");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        startSocket();
        createSocketEvents();
    }

    private void startSocket() {
        socket = new ConnectionIO().connect();
        email = "arlinda@gmail.com";
        socket.emit("notification_socket_record", email);
    }

    private void createSocketEvents() {

        socket.on("NOTIFY_NEW_MSG", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject msgObject = new JSONObject(String.valueOf(args[0]));
                    System.out.println("SERVICE _>> " + msgObject);
                    String contact = msgObject.getString("contact");
                    String msg = msgObject.getString("msg");
                    int id = (int) (Math.random() * 1000);
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                            id, new Intent(getApplicationContext(), StartApp.class), PendingIntent.FLAG_UPDATE_CURRENT);

                    // Código para android de versões anteriores a API 26 (Código depreciado)
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                    builder.setAutoCancel(true)
                            .setTicker("Nova mensagem no DarkSheet!")
                            .setContentTitle(contact)
                            .setContentText(msg)
                            .setSmallIcon(R.drawable.icon_1_sem_fundo)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.user_64))
                            .setContentIntent(pendingIntent);
                    Notification notification = builder.build();
                    notification.vibrate = new long[]{150, 300, 150, 600};
                    notificationManager.notify(id, notification);
                    try {
                        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone player = RingtoneManager.getRingtone(getApplicationContext(), sound);
                        player.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
