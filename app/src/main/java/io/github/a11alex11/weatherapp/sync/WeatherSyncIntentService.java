package io.github.a11alex11.weatherapp.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;



public class WeatherSyncIntentService extends IntentService {
    public WeatherSyncIntentService() {
        super(WeatherSyncIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        WeatherSyncTask.syncWeather(this);

    }
}
