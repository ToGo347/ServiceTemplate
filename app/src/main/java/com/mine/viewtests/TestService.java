package com.mine.viewtests;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;

public class TestService extends Service {
    public static boolean ENABLED = false;
    private BroadcastReceiver end = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopSelf();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ENABLED = true;
        showNotification();
        registerReceiver(end, new IntentFilter("END"));
        createView();
    }

    private void createView() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

    }

    private void showNotification() {
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Test channel";
            String description = "Just a channel for the heater service notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("com.mine.test", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        // Set the info for the views that show in the notification panel.
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "com.mine.heater")
                .setSmallIcon(R.drawable.ic_launcher_foreground)  // the status icon
                .setTicker("Just a test")  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("Testing")  // the label of the entry
                .setContentText("Views")  // the contents of the entry
                .setContentIntent(contentIntent);  // The intent to send when the entry is clicked

        final Intent addReceiver = new Intent();
        addReceiver.setAction("UP");
        PendingIntent pendingIntentAdd = PendingIntent.getBroadcast(this, 0, addReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.addAction(R.drawable.ic_launcher_background, "\t\t+", pendingIntentAdd);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId("com.mine.test");
        }

        startForeground(123, notification.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(end);
        ENABLED = false;
    }
}
