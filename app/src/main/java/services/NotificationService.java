package services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.bramgussekloo.projectb.Activities.Login.MainActivity;
import com.bramgussekloo.projectb.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static services.Notification.CHANNEL_1_ID;
import static services.Notification.CHANNEL_2_ID;

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "NotificationService";
    public static final int BROADCAST_NOTIFICATION_ID = 1;

    public NotificationService() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String token){
        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            reference.child("user")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "").replace("@", ""))
                    .child("messaging_token")
                    .setValue(token);
        }
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        sendBroadcastNotification(title, message);


    }

    @Override
    public void onDeletedMessages() {
    }

    private void sendBroadcastNotification(String title, String message){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_1_ID);

        Intent notifyIntent = new Intent(this, MainActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setSmallIcon(R.drawable.ic_warning)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setAutoCancel(true);

        builder.setContentIntent(notifyPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(BROADCAST_NOTIFICATION_ID, builder.build());


    }


}
