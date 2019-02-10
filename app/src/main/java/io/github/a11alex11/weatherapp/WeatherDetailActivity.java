package io.github.a11alex11.weatherapp;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import java.util.Date;

import io.github.a11alex11.weatherapp.data.WeatherDatabase;
import io.github.a11alex11.weatherapp.data.WeatherEntry;
import io.github.a11alex11.weatherapp.databinding.ActivityWeatherDetailBinding;
import io.github.a11alex11.weatherapp.utilities.WeatherDateUtils;
import io.github.a11alex11.weatherapp.utilities.WeatherUtils;
import io.github.a11alex11.weatherapp.viewmodel.WeatherDetailViewModel;
import io.github.a11alex11.weatherapp.viewmodel.WeatherDetailViewModelFactory;

public class WeatherDetailActivity extends AppCompatActivity{
    private static final String TAG = WeatherDetailActivity.class.getSimpleName();


    private ActivityWeatherDetailBinding detailBinding;
    private String weatherSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        detailBinding = DataBindingUtil.setContentView(this,R.layout.activity_weather_detail);

        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TEXT)){
            long milli = intent.getLongExtra(Intent.EXTRA_TEXT,0);
            setupWeatherDetailViewModel(milli);

        }


    }

    private void setupWeatherDetailViewModel(long timeInMilli){
        WeatherDatabase weatherDatabase = WeatherDatabase.getInstance(WeatherDetailActivity.this);

        WeatherDetailViewModelFactory factory = new WeatherDetailViewModelFactory(weatherDatabase,timeInMilli);
        WeatherDetailViewModel viewModel = ViewModelProviders.of(this,factory).get(WeatherDetailViewModel.class);
        viewModel.getWeatherDetail().observe(this, new Observer<WeatherEntry>() {
            @Override
            public void onChanged(@Nullable WeatherEntry curEntry) {
                if(curEntry ==null){
                    showError();
                }else {
                    showWeatherData();

                    Date date = curEntry.getDate();
                    String dateString = WeatherDateUtils.getFriendlyDateString(WeatherDetailActivity.this, date.getTime(), false);


                    int weatherId = curEntry.getWeatherId();
                    int imageId = WeatherUtils.getSmallArtResourceForWeatherCondition(weatherId);



                    String description = WeatherUtils.getStringForWeatherCondition(WeatherDetailActivity.this, weatherId);
                    String descriptionA11y = getString(R.string.a11y_forecast,description);
                    String imageA11y = getString(R.string.a11y_forecast_icon,description);

                    double highInCelcius = curEntry.getMaxTemp();
                    String highString = WeatherUtils.formatTemperatureNoUnit(WeatherDetailActivity.this,highInCelcius);
                    String highA11y = getString(R.string.a11y_high_temp,highString);

                    double lowInCelcius = curEntry.getMinTemp();
                    String lowString = WeatherUtils.formatTemperatureNoUnit(WeatherDetailActivity.this,lowInCelcius);
                    String lowA11y = getString(R.string.a11y_low_temp,lowString);

                    double pressure = curEntry.getPressure();
                    String pressureString = getString(R.string.format_pressure,pressure);
                    String pressureA11y = getString(R.string.a11y_pressure,pressureString);


                    double windSpeed = curEntry.getWindSpeed();
                    double windDir = curEntry.getWindDirection();
                    String windString = WeatherUtils.getFormattedWind(WeatherDetailActivity.this,(float)windSpeed,(float)windDir);
                    String windA11y = getString(R.string.a11y_wind,windString);

                    int humidity = curEntry.getHumidity();
                    String humidityString = getString(R.string.format_humidity,humidity);
                    String humidityA11y = getString(R.string.a11y_humidity);


                    String highAndLowTemperature = WeatherUtils.formatHighLows(WeatherDetailActivity.this, highInCelcius, lowInCelcius);


                    // Main weather
                    detailBinding.weatherMain.tvHigh.setText(highString);
                    detailBinding.weatherMain.tvHigh.setContentDescription(highA11y);

                    detailBinding.weatherMain.tvLow.setText(lowString);
                    detailBinding.weatherMain.tvLow.setContentDescription(lowA11y);


                    detailBinding.weatherMain.tvDate.setText(dateString);


                    detailBinding.weatherMain.tvWeather.setText(description);
                    detailBinding.weatherMain.tvWeather.setContentDescription(descriptionA11y);

                    detailBinding.weatherMain.ivWeatherIcon.setImageResource(imageId);
                    detailBinding.weatherMain.ivWeatherIcon.setContentDescription(description);


                    // Additional weather
                    detailBinding.weatherSecondary.tvHumidityValue.setText(humidityString);
                    detailBinding.weatherSecondary.tvHumidityValue.setContentDescription(humidityA11y);

                    detailBinding.weatherSecondary.tvPressureValue.setText(pressureString);
                    detailBinding.weatherSecondary.tvPressureValue.setContentDescription(pressureA11y);

                    detailBinding.weatherSecondary.tvWindValue.setText(windString);
                    detailBinding.weatherSecondary.tvWindValue.setContentDescription(windA11y);

                    weatherSummary = String.format("%s - %s - %s/%s",
                            dateString, description, highString, lowString);


                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather_detail_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_share){

            Intent intent = ShareCompat.IntentBuilder.from(this).setChooserTitle(R.string.action_share)
                    .setType("text/plain").setText(weatherSummary).getIntent();
            startActivity(intent);
        }
        else if(id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void showLoading(){
        detailBinding.pbWeatherDetail.setVisibility(View.VISIBLE);
        detailBinding.clWeatherDetail.setVisibility(View.INVISIBLE);
        detailBinding.tvDetailErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void showWeatherData(){
        detailBinding.pbWeatherDetail.setVisibility(View.INVISIBLE);
        detailBinding.clWeatherDetail.setVisibility(View.VISIBLE);
        detailBinding.tvDetailErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void showError(){
        detailBinding.pbWeatherDetail.setVisibility(View.INVISIBLE);
        detailBinding.clWeatherDetail.setVisibility(View.INVISIBLE);
        detailBinding.tvDetailErrorMessage.setVisibility(View.VISIBLE);
    }
}
