package io.github.a11alex11.weatherapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Date;

import io.github.a11alex11.weatherapp.data.WeatherDatabase;
import io.github.a11alex11.weatherapp.data.WeatherEntry;


//Class that will persist throughout the application and hold our data

public class WeatherDetailViewModel extends ViewModel {
    private LiveData<WeatherEntry> weatherDetailData;

    // Constructor for the factory
    public WeatherDetailViewModel(WeatherDatabase weatherDatabase, long dateInMilli){
        weatherDetailData = weatherDatabase.weatherDao().getWeatherEntryByDate(new Date(dateInMilli));
    }

    public LiveData<WeatherEntry> getWeatherDetail(){
        return weatherDetailData;
    }

}
