package com.sales.darksheet.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Toast.makeText(context, "BOOT_COMPLETED", Toast.LENGTH_LONG).show();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                MyJobIntentService.startJobService(context, new Intent(), 0);
            else
                context.startService(new Intent(context, NotificationService.class));
        }
    }
}
