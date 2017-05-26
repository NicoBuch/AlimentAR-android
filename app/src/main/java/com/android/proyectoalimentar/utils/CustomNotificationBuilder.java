package com.android.proyectoalimentar.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.NotificationType;
import com.android.proyectoalimentar.ui.drawer.DrawerActivity;


/**
 * Builder of the notifications.
 */
public class CustomNotificationBuilder {

    public static Notification build(NotificationType notificationType, String message, Context context){
        return build(notificationType,message,context,null,null);
    }

    public static Notification build(NotificationType notificationType, String message, Context context
            , String donationId, String volunteerName){
        switch (notificationType){
            case DONATIONS_NEARBY:
                return getSimpleMessageNotification(context.getString(notificationType.getTitleResource()),
                        message,
                        context);
        }

        return null;


    }

    /**
     * Create simple notification to display a message. The notification will open the drawer
     * activity.
     * @param title
     * @param message
     * @param context
     * @return
     */
    private static Notification getSimpleMessageNotification(String title, String message, Context context){
        Intent intent = new Intent(context, DrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.img_default)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent).build();
    }

}
