package io.github.a11alex11.weatherapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import io.github.a11alex11.weatherapp.data.WeatherDatabase;
import io.github.a11alex11.weatherapp.data.WeatherEntry;

//Class that will persist throughout the application and hold our data

public class WeatherViewModel extends AndroidViewModel {
    private static final String TAG = WeatherViewModel.class.getSimpleName();

    private LiveData<List<WeatherEntry>> weatherData;


    public WeatherViewModel(@NonNull Application application) {
        super(application);
        WeatherDatabase weatherDatabase = WeatherDatabase.getInstance(this.getApplication());
        weatherData = weatherDatabase.weatherDao().getWeather();
        Log.d(TAG, "Weather retrieved from database");

    }

    public LiveData<List<WeatherEntry>> getWeather(){
        return weatherData;

    }
}
