package io.github.a11alex11.weatherapp.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;



import io.github.a11alex11.weatherapp.R;
import io.github.a11alex11.weatherapp.WeatherDetailActivity;
import io.github.a11alex11.weatherapp.data.WeatherEntry;
import io.github.a11alex11.weatherapp.data.WeatherPreferences;

public class NotificationUtils {
    private static final int WEATHER_PENDING_INTENT_ID = 44;

    private static final int WEATHER_SYNC_NOTIFICATION_ID = 53;
    private static final String WEATHER_SYNC_NOTIFICATION_CHANNEL_ID = "weather_synced_channel";


    // Notify the user that the weather has been updated
    public static void weatherUpdated(Context context, WeatherEntry today){



        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    WEATHER_SYNC_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }


        String title = WeatherPreferences.getPreferredWeatherLocation(context);
        String condition = WeatherUtils.getStringForWeatherCondition(context,today.getWeatherId());
        String high = WeatherUtils.formatTemperature(context,today.getMaxTemp());
        String low = WeatherUtils.formatTemperature(context,today.getMinTemp());

        String notificationFormat = context.getString(R.string.format_notification);
        String body = String.format(notificationFormat,condition,high,low);

        int smallImageId = WeatherUtils.getSmallArtResourceForWeatherCondition(today.getWeatherId());
        int largeImageId = WeatherUtils.getLargeArtResourceForWeatherCondition(today.getWeatherId());


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,WEATHER_SYNC_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(smallImageId)
                .setLargeIcon(largeIcon(context,largeImageId))
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context,today))
                .setAutoCancel(true);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(WEATHER_SYNC_NOTIFICATION_ID,notificationBuilder.build());
        WeatherPreferences.setLastNotificationTime(context,System.currentTimeMillis());

    }


    // Intent that will link to the current date activity
    private static PendingIntent contentIntent(Context context,WeatherEntry weather){
        Intent detailIntent = new Intent(context, WeatherDetailActivity.class);
        detailIntent.putExtra(Intent.EXTRA_TEXT,weather.getDate().getTime());


        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(detailIntent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(WEATHER_PENDING_INTENT_ID,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private static Bitmap largeIcon(Context context, int resId){
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, resId);
        return largeIcon;
    }


}
