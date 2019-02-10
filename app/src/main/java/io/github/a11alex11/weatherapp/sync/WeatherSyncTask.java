package io.github.a11alex11.weatherapp.sync;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import java.net.URL;

import io.github.a11alex11.weatherapp.WeatherActivity;
import io.github.a11alex11.weatherapp.data.WeatherDatabase;
import io.github.a11alex11.weatherapp.data.WeatherEntry;
import io.github.a11alex11.weatherapp.data.WeatherPreferences;
import io.github.a11alex11.weatherapp.utilities.NetworkUtilities;
import io.github.a11alex11.weatherapp.utilities.NotificationUtils;
import io.github.a11alex11.weatherapp.utilities.OpenWeatherJsonUtils;



public class WeatherSyncTask {
    private static String TAG = WeatherSyncTask.class.getSimpleName();
    synchronized public static void syncWeather(Context context){
        String location = WeatherPreferences.getPreferredWeatherLocation(context);
        String apikey = WeatherPreferences.getApikey(context);

        if(apikey.equals("")){
            WeatherPreferences.resetApiKey(context);
            apikey = WeatherPreferences.getApikey(context);
        }

        URL url = NetworkUtilities.buildUrl(location,apikey);
        try {
            String jsonWeatherData = NetworkUtilities.getResponseFromHttpUrl(url);
            WeatherEntry[] weatherValues = OpenWeatherJsonUtils.getWeatherEntriesFromJson(context,jsonWeatherData);

            // Get the database, clear it and then insert new data
            WeatherDatabase weatherDatabase = WeatherDatabase.getInstance(context);
            weatherDatabase.weatherDao().deleteAllWeatherEntries();
            Log.d(TAG,  "Weather in database deleted.");

            for(int i=0; i<weatherValues.length;i++){
                weatherDatabase.weatherDao().insertWeather(weatherValues[i]);
            }
            Log.d(TAG,  "Weather in database added.");

            boolean useNotifications = WeatherPreferences.isNotificationsEnabled(context);

            if(useNotifications){
                long timeSinceLastNotification = WeatherPreferences.getTimeSinceLastNotification(context);
                if(timeSinceLastNotification > DateUtils.DAY_IN_MILLIS){
                    NotificationUtils.weatherUpdated(context,weatherValues[0]);
                }
            }




        }catch(Exception e) {
            // Server may be down!
            e.printStackTrace();
        }

    }

}
