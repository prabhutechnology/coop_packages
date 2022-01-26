package com.prabhutech.prabhupackages.wallet.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.wallet.activities.starteractivity.StarterActivity;
import com.prabhutech.prabhupackages.wallet.core.UserCore;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;
import com.prabhutech.prabhupackages.wallet.core.repo.UserRepo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import io.reactivex.observers.DisposableCompletableObserver;

public class MainFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessaging";
    private static final List<String> channels = Arrays.asList
            ("all", "transaction", "events", "offers");

    public static final String INTENT_NOTIFICATION_ACTION = "INTENT_NOTIF_ACTION";

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "onNewToken: ");
        // Update new device token to servers
        UserCore.deviceToken = token;
        UserCore.deviceTokenIsNew = true;
        if (UserCore.customerId != null && !UserCore.customerId.isEmpty()) {
            UserRepo.saveNotificationToken(getApplicationContext(), token)
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: registered notification token");
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            e.printStackTrace();
                            UserPref.setFailedNotificationTokenUpdate(MainFirebaseMessagingService.this, token);
                            Log.e(TAG, "onError: register of token failed", e);
                        }
                    });
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: ");
        showNotification(Objects.requireNonNull(remoteMessage.getNotification()), remoteMessage.getData());
    }

    @Override
    public void onDeletedMessages() {
        Log.d(TAG, "onDeletedMessages: ");
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        Log.d(TAG, "onMessageSent: on message sent");
        // TODO: 9/24/19
    }

    @Override
    public void onSendError(@NonNull String s, @NonNull Exception e) {
        Log.d(TAG, "onSendError: ");
        // TODO: 9/24/19
    }

    private void showNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        String channelId = getString(R.string.default_notification_channel_id);
        if (notification.getChannelId() != null) {
            if (channels.contains(notification.getChannelId())) {
                channelId = notification.getChannelId();
            }
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_prabhu_notification_logo)
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setContentTitle(notification.getTitle())
                        .setContentText(notification.getBody())
                        .setContentIntent(createPendingIntent(notification, data, notification.getTitle(), notification.getBody(), notification.getImageUrl()))
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSound(defaultSoundUri);

        // extra body data
        String body = data.get("body");
        String notificationCount = data.get("notificationCount");
        if (body != null) {
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("body")));
        }

        if (notificationCount != null) {
            notificationBuilder.setNumber(Integer.parseInt(notificationCount));
        }

        // set expandable image
        // large icon is also set as this image if large icon is not given just in case.
        if (notification.getImageUrl() != null) {
            FutureTarget target = Glide.with(this).asBitmap().load(notification.getImageUrl()).submit();
            try {
                Bitmap bitmap = (Bitmap) target.get();
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmap)
                                .bigLargeIcon(null)
                );
                notificationBuilder.setLargeIcon(bitmap);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        // setting large icon again if given
        try {
            if (data.get("icon") != null && !data.get("icon").isEmpty()) {
                // FutureTarget target = Glide.with(this).asBitmap().load(data.get("icon")).submit();
                Bitmap target = BitmapFactory.decodeResource(getResources(), R.drawable.ic_prabhu_notification_logo);
                notificationBuilder.setLargeIcon(target);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            buildChannels(notificationManager);
        }

        if (notificationManager != null) {
            notificationManager.notify(0, notificationBuilder.build());
        } else {
            Log.e(TAG, "showNotification: notification manager is null");
        }
    }

    private PendingIntent createPendingIntent(RemoteMessage.Notification notification, Map<String, String> data, String title, String body, Uri image) {
        // TODO: 10/20/19 use notification data
        String urlImage = "";
        try{
            if(data.size() > 0){
                JSONObject obj = new JSONObject(data);
                urlImage = obj.getString("INTENT_NOTIF_ACTIONimage");
            }
        } catch (Exception e){

        }

        String actionId = data.get("actionId");
        if(actionId == null){
            actionId = "111";
        }
        Intent intent = new Intent(this, StarterActivity.class);
        intent.putExtra(INTENT_NOTIFICATION_ACTION, actionId);
        intent.putExtra(INTENT_NOTIFICATION_ACTION + "title", title);
        intent.putExtra(INTENT_NOTIFICATION_ACTION + "body", body);
        intent.putExtra(INTENT_NOTIFICATION_ACTION + "image", urlImage);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buildChannels(NotificationManager notificationManager) {
        List<NotificationChannel> channels = new ArrayList<>();

        // default channel
        NotificationChannel allChannel = new NotificationChannel("all", "Transactions", NotificationManager.IMPORTANCE_HIGH);
        allChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        allChannel.enableLights(true);
        allChannel.enableVibration(true);
        allChannel.setLightColor(R.color.white);
        channels.add(allChannel);

        // transactions channel
        NotificationChannel transactionChannel = new NotificationChannel("transactions", "Transactions", NotificationManager.IMPORTANCE_HIGH);
        transactionChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        transactionChannel.enableLights(true);
        transactionChannel.enableVibration(true);
        transactionChannel.setLightColor(R.color.white);
        channels.add(transactionChannel);

        // events
        NotificationChannel eventsChannel = new NotificationChannel("events", "Events", NotificationManager.IMPORTANCE_HIGH);
        eventsChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        eventsChannel.enableLights(true);
        eventsChannel.enableVibration(true);
        eventsChannel.setLightColor(R.color.white);
        channels.add(eventsChannel);

        // offers
        NotificationChannel offersChannel = new NotificationChannel("offers", "Offers", NotificationManager.IMPORTANCE_HIGH);
        offersChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        offersChannel.enableLights(true);
        offersChannel.enableVibration(true);
        offersChannel.setLightColor(R.color.white);
        channels.add(offersChannel);

        if (notificationManager != null) {
            notificationManager.createNotificationChannels(channels);
        }
    }
}
