package io.github.a11alex11.weatherapp.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

import io.github.a11alex11.weatherapp.R;


public class WeatherPreferences {



    static public void setLocationDetails(Context context, double lat, double lon) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putLong(context.getString(R.string.pref_coord_lat_key), Double.doubleToRawLongBits(lat));
        editor.putLong(context.getString(R.string.pref_coord_long_key), Double.doubleToRawLongBits(lon));
        editor.apply();
    }



    public static String getPreferredWeatherLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String locationKey = context.getString(R.string.pref_location_key);
        String locationDefault = context.getString(R.string.pref_location_default);
        String prefLocation = prefs.getString(locationKey,locationDefault);

        return prefLocation;
    }

    public static String getApikey(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String apikeyKey = context.getString(R.string.pref_apikey_key);
        String apikeyDefault = context.getString(R.string.pref_apikey_default);

        String apikey = prefs.getString(apikeyKey,apikeyDefault);
        return apikey;

    }

    public static void resetApiKey(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        String apikeyKey = context.getString(R.string.pref_apikey_key);
        String apikeyDefault = context.getString(R.string.pref_apikey_default);
        editor.putString(apikeyKey,apikeyDefault);

        editor.apply();

    }

    public static boolean isWifiOnly(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String isWifiKey = context.getString(R.string.pref_update_wifi_key);
        boolean isWifiKeyDefault = context.getResources().getBoolean(R.bool.pref_update_wifi_default);

        return prefs.getBoolean(isWifiKey,isWifiKeyDefault);
    }

    public static boolean isNotificationsEnabled(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String isNotificationKey = context.getString(R.string.pref_notification_key);
        boolean isNotificationDefault = context.getResources().getBoolean(R.bool.pref_notification_default);



        return prefs.getBoolean(isNotificationKey,isNotificationDefault);
    }

    public static long getTimeSinceLastNotification(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        long lastNotificationTime = getLastNotificationTime(context);

        return System.currentTimeMillis() - lastNotificationTime;
    }

    public static long getLastNotificationTime(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String lastNotificationKey = context.getString(R.string.pref_last_notification_key);
        long lastNotificationDefault = context.getResources().getInteger(R.integer.pref_last_notification_default);



        return prefs.getLong(lastNotificationKey,lastNotificationDefault);
    }

    public static void setLastNotificationTime(Context context, long curTime){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor =  prefs.edit();


        String lastNotificationKey = context.getString(R.string.pref_last_notification_key);
        editor.putLong(lastNotificationKey,curTime);
        editor.apply();
    }

    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String unitsKey = context.getString(R.string.pref_unit_key);
        String unitsDefault = context.getString(R.string.pref_unit_default);
        String metricKey = context.getString(R.string.pref_unit_celcius_key);

        String prefsUnits = prefs.getString(unitsKey,unitsDefault);

        if(prefsUnits.equals(metricKey)){
            return true;
        }
        else{
            return false;
        }

    }


    public static double[] getLocationCoordinates(Context context) {
        double[] coords = new double[2];

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String lat_key = context.getString(R.string.pref_coord_lat_key);
        long lat_default = Long.parseLong(context.getString(R.string.pref_coord_lat_default));

        String long_key = context.getString(R.string.pref_coord_long_key);
        long long_default = Long.parseLong(context.getString(R.string.pref_coord_long_default));

        coords[0] = prefs.getLong(lat_key,lat_default);
        coords[1] = prefs.getLong(long_key,long_default);
        return coords;
    }


    public static void setLocale(Context context,String lang){
        Log.d("SettingsFrag",lang);
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }


}