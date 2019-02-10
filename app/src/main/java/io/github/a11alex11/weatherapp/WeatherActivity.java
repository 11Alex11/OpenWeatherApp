package io.github.a11alex11.weatherapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import io.github.a11alex11.weatherapp.data.WeatherEntry;
import io.github.a11alex11.weatherapp.data.WeatherPreferences;
import io.github.a11alex11.weatherapp.databinding.ActivityWeatherBinding;
import io.github.a11alex11.weatherapp.sync.WeatherSyncUtils;
import io.github.a11alex11.weatherapp.viewmodel.WeatherViewModel;


public class WeatherActivity extends AppCompatActivity implements WeatherAdapter.WeatherAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = WeatherActivity.class.getSimpleName();

    private WeatherAdapter weatherAdapter;
    private ActivityWeatherBinding weatherBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // Set up locale settings

        WeatherPreferences.setLocale(this,PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_language_key),getString(R.string.pref_language_default)));

        weatherBinding = DataBindingUtil.setContentView(this,R.layout.activity_weather);


        weatherAdapter = new WeatherAdapter(this,this);


        // Set up recycler view
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        weatherBinding.rvForecast.setAdapter(weatherAdapter);
        weatherBinding.rvForecast.setHasFixedSize(true);
        weatherBinding.rvForecast.setLayoutManager(layout);

        // Initialize weather data and setup ViewModel data observer
        initData();
        setupWeatherViewModel();


        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);


    }


    // Observe changes using LiveData in ViewModel, update recyclerview when
    private void setupWeatherViewModel(){
        WeatherViewModel model = ViewModelProviders.of(this).get(WeatherViewModel.class);

        model.getWeather().observe(this,new Observer<List<WeatherEntry>>() {
            @Override
            public void onChanged(@Nullable List<WeatherEntry> weatherEntries) {
                weatherAdapter.setWeatherData(weatherEntries);
                if(weatherEntries!=null&&weatherEntries.size()!=0){
                    showWeatherData();
                }
                else{
                    showError();
                }
            }
        });
    }


    // Initial load on app start
    private void initData(){
        showLoading();
        WeatherSyncUtils.initialize(this);
    }

    // Start an immediate sync
    private void refreshData(){
        showLoading();
        WeatherSyncUtils.startImmediateSync(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Refresh button to sync data immediately
        if(id == R.id.action_refresh) {
            weatherAdapter.setWeatherData(null);
            refreshData();
        }
        // Settings button
        else if(id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }
        // Map button
        else if(id == R.id.action_map) {
            openLocation();

        }
        return super.onOptionsItemSelected(item);
    }

    private void openLocation(){
        // Get our set location and create a map intent with it
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String address = prefs.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));

        Uri addressUri = Uri.parse("geo:0,0?q=" + address);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(addressUri);

        // If we have the required permissions, start the intent
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }

    private void showLoading(){
        weatherBinding.pbWeather.setVisibility(View.VISIBLE);
        weatherBinding.rvForecast.setVisibility(View.INVISIBLE);
        weatherBinding.tvErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void showWeatherData(){
        weatherBinding.pbWeather.setVisibility(View.INVISIBLE);
        weatherBinding.rvForecast.setVisibility(View.VISIBLE);
        weatherBinding.tvErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void showError(){
        weatherBinding.pbWeather.setVisibility(View.INVISIBLE);
        weatherBinding.rvForecast.setVisibility(View.INVISIBLE);
        weatherBinding.tvErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void weatherClick(WeatherEntry weather) {
        Intent intent = new Intent(this,WeatherDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,weather.getDate().getTime());

        startActivity(intent);

    }


    @Override
    protected void onStart() {
        super.onStart();

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_unit_key))){

            WeatherViewModel model = ViewModelProviders.of(this).get(WeatherViewModel.class);
            LiveData<List<WeatherEntry>> data = model.getWeather();
            weatherAdapter.setWeatherData(data.getValue());

        }
        else if(key.equals(getString(R.string.pref_update_wifi_key))){
            WeatherSyncUtils.scheduleFirebaseJobDispatcherSync(this);
        }
        else if(key.equals(getString(R.string.pref_language_key))){
            recreate();
        }
    }
}
