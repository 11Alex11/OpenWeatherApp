package io.github.a11alex11.weatherapp.utilities;

/**
 * Created by RickySimon on 1/15/2019.
 */
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Date;

import io.github.a11alex11.weatherapp.data.WeatherEntry;
import io.github.a11alex11.weatherapp.data.WeatherPreferences;



public final class OpenWeatherJsonUtils {

    /* Location information */
    private static final String OWM_CITY = "city";
    private static final String OWM_COORD = "coord";

    /* Location coordinate */
    private static final String OWM_LATITUDE = "lat";
    private static final String OWM_LONGITUDE = "lon";

    // Each forecast information is inside list
    private static final String OWM_LIST = "list";
    private static final String OWM_MAIN = "main";

    private static final String OWM_PRESSURE = "pressure";
    private static final String OWM_HUMIDITY = "humidity";
    private static final String OWM_WINDSPEED = "speed";
    private static final String OWM_WIND_DIRECTION = "deg";




    private static final String OWM_MAX = "temp_max";
    private static final String OWM_MIN = "temp_min";
    private static final String OWM_WIND = "wind";
    private static final String OWM_WEATHER = "weather";
    private static final String OWM_WEATHER_ID = "id";

    private static final String OWM_MESSAGE_CODE = "cod";

    private static final String OWM_DESCRIPTION = "main";





    // Parse the JSON, converting each forecast into a WeatherEntry and return them in an array
    public static WeatherEntry[] getWeatherEntriesFromJson(Context context, String forecastJsonStr) throws JSONException{

        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        // Check for errors in JSON
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Bad location
                    return null;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    // Invalid API key TODO
                    return null;
                default:

                    return null;
            }
        }

        JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);

        JSONObject cityCoord = cityJson.getJSONObject(OWM_COORD);
        double cityLatitude = cityCoord.getDouble(OWM_LATITUDE);
        double cityLongitude = cityCoord.getDouble(OWM_LONGITUDE);
        WeatherPreferences.setLocationDetails(context,cityLatitude,cityLongitude);



        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
        WeatherEntry[] weatherEntries = new WeatherEntry[weatherArray.length()];

        long localDate = System.currentTimeMillis();
        long utcDate = WeatherDateUtils.getUTCDateFromLocal(localDate);
        long startDay = WeatherDateUtils.normalizeDate(utcDate);

        for (int i = 0; i < weatherArray.length(); i++) {

            // Values needed to build WeatherEntry
            long dateTimeMillis;
            double pressure;
            int humidity;
            double windSpeed;
            double windDirection;

            double high;
            double low;
            String description;
            int weatherId;


            JSONObject dayForecast = weatherArray.getJSONObject(i);
            JSONObject mainData = dayForecast.getJSONObject(OWM_MAIN);
            JSONObject windData = dayForecast.getJSONObject(OWM_WIND);

            pressure = mainData.getDouble(OWM_PRESSURE);
            humidity = mainData.getInt(OWM_HUMIDITY);
            windSpeed = windData.getDouble(OWM_WINDSPEED);
            windDirection = windData.getDouble(OWM_WIND_DIRECTION);

            // Assume that the days in the JSON are in order
            // TODO - Fix JSON dates, should not assume this!
            dateTimeMillis = startDay + WeatherDateUtils.DAY_IN_MILLIS * i;


            JSONObject weatherObject =
                    dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);
            weatherId = weatherObject.getInt(OWM_WEATHER_ID);



            JSONObject mainObject = dayForecast.getJSONObject("main");

            high = mainObject.getDouble(OWM_MAX);
            low = mainObject.getDouble(OWM_MIN);


            weatherEntries[i] = new WeatherEntry(weatherId,low,high,humidity,pressure,windSpeed,windDirection,new Date(dateTimeMillis),description);
        }

        return weatherEntries;

    }
}