package com.sales.darksheet.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.sales.darksheet.R;
import com.sales.darksheet.StartApp;

public class MyJobIntentService extends JobIntentService {

    private NotificationManager notificationManager;
    private String channelId;

    public static void startJobService(Context context, Intent intent, int jobId) {
        enqueueWork(context, MyJobIntentService.class, jobId, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        while (true) {
            try {
                Thread.sleep(3000);
                cont++;
                int id = (int) (Math.random() * 1000);
                channelId = "my_channel_id_" + id;
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                        id, new Intent(getApplicationContext(), StartApp.class), PendingIntent.FLAG_UPDATE_CURRENT);

                boolean isCreated = createChannel();

                if (isCreated) {
                    // Código para versões a partir do android 8 (API 26)
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
                    builder.setAutoCancel(true)
                            .setTicker("Nova mensagem no DarkSheet!")
                            .setContentTitle("TITLE " + cont)
                            .setContentText("TEXT")
                            .setSmallIcon(R.drawable.icon_1_sem_fundo)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.user_64))
                            .setContentIntent(pendingIntent);
                    Notification notification = builder.build();
                    notificationManager.notify(id, notification);
                } else {
                    // Código para android de versões anteriores a API 26 (Código depreciado)
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                    builder.setAutoCancel(true)
                            .setTicker("Nova mensagem no DarkSheet!")
                            .setContentTitle("TITLE " + cont)
                            .setContentText("TEXT")
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
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    int cont = 0;

    private boolean createChannel() {
        CharSequence name = "channel";
        String description = "description";
        int importance = NotificationManager.IMPORTANCE_MAX;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(channelId, name, importance);
            notificationChannel.setDescription(description);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);
            return true;
        } else {
            return false;
        }
    }
}
