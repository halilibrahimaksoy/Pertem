package com.haksoy.pertem.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.haksoy.pertem.R;
import com.haksoy.pertem.activity.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getFrom().equals("/topics/ADD_TOPIC"));
        Log.d(TAG, "onMessageReceived : " + remoteMessage.getNotification().getBody());
    }

    private void sendNotification(String message, boolean isAdd) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "com.haksoy.pertem.NOTIFY";
        CharSequence name = "Pertem Channel";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }
        Intent intent;
        PendingIntent pendingIntent;
        if (isAdd) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.askerol_url)));
        } else {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.msb_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.msb_logo))
                .setContentTitle(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);

        notificationManager.notify(message.hashCode(), builder.build());
    }

    @Override
    public void onNewToken(String s) {
    }
}