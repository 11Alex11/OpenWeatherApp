package io.github.a11alex11.weatherapp.sync;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;


import java.util.concurrent.TimeUnit;

import io.github.a11alex11.weatherapp.data.WeatherDatabase;
import io.github.a11alex11.weatherapp.data.WeatherPreferences;



public class WeatherSyncUtils {
    private final static String TAG = WeatherSyncUtils.class.getSimpleName();
    private static boolean init = false;
    private final static String WEATHER_SYNC_TAG = "weather-sync";

    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 2;


    // Called once in main activity to start the dispatcher
    synchronized public static void initialize(@NonNull final Context context){
        if(init){
            return;
        }

        init = true;


        // Sync if the weather data is empty
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                WeatherDatabase weatherDatabase = WeatherDatabase.getInstance(context);
                int weatherCount = weatherDatabase.weatherDao().getWeatherCount();
                if(weatherCount==0){
                    Log.d(TAG, "No weather entries in database in init. Querying...");
                    startImmediateSync(context);
                }
                return null;
            }
        }.execute();

        scheduleFirebaseJobDispatcherSync(context);
    }

    public static void scheduleFirebaseJobDispatcherSync(@NonNull Context context){
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        boolean runOnWifi = WeatherPreferences.isWifiOnly(context);

        Job.Builder syncWeather = dispatcher.newJobBuilder()
        .setService(WeatherSyncFirebaseJobService.class)
                .setTag(WEATHER_SYNC_TAG)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS, SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true);
        if(runOnWifi){
            syncWeather.setConstraints(Constraint.ON_UNMETERED_NETWORK);
        }
        else{
            syncWeather.setConstraints(Constraint.ON_ANY_NETWORK);
        }
        Log.d(TAG, "Dsipatching job service");

        dispatcher.schedule(syncWeather.build());




    }

    public static void startImmediateSync(@NonNull final Context context){
        Intent intentSync = new Intent(context,WeatherSyncIntentService.class);
        context.startService(intentSync);
    }
}
