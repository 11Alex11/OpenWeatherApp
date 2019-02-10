package io.github.a11alex11.weatherapp.viewmodel;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import io.github.a11alex11.weatherapp.data.WeatherDatabase;

// Factory is required for passing data to the ViewModel
public class WeatherDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final WeatherDatabase weatherDatabase;
    private final long timeInMilli;

    public WeatherDetailViewModelFactory(WeatherDatabase weatherDatabase,long timeInMilli){
        this.weatherDatabase = weatherDatabase;
        this.timeInMilli = timeInMilli;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WeatherDetailViewModel(weatherDatabase,timeInMilli);
    }
}
