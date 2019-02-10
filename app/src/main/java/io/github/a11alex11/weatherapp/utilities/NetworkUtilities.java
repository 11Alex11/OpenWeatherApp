package io.github.a11alex11.weatherapp.utilities;

import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;



public final class NetworkUtilities {
    private static final String TAG = NetworkUtilities.class.getSimpleName();
    private static final String UNITS = "metric";
    private static final int DAYS = 14;



    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast";
    private static final String QUERY_PARAM = "q";
    private static final String UNITS_PARAM = "units";
    private static final String DAYS_PARAM = "cnt";
    private static final String APIKEY_PARAM = "APPID";

    public static URL buildUrl(String locationQuery,String apikey){

        Uri uriBuilder = Uri.parse(WEATHER_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM,locationQuery)
                .appendQueryParameter(UNITS_PARAM,UNITS)
                .appendQueryParameter(APIKEY_PARAM,apikey)
                .appendQueryParameter(DAYS_PARAM,Integer.toString(DAYS)).build();
        URL url = null;
        try{
            url = new URL(uriBuilder.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.d(TAG, "Built URI: " + url);
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in;
            int status = urlConnection.getResponseCode();
            Log.d(TAG,Integer.toString(status));
            if(status!=HttpURLConnection.HTTP_OK){
                in = urlConnection.getErrorStream();
            }
            else{
                in = urlConnection.getInputStream();
            }

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
