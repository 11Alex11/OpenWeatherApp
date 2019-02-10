package io.github.a11alex11.weatherapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.List;
import java.util.Locale;

import io.github.a11alex11.weatherapp.data.WeatherEntry;
import io.github.a11alex11.weatherapp.data.WeatherPreferences;
import io.github.a11alex11.weatherapp.sync.WeatherSyncUtils;
import io.github.a11alex11.weatherapp.viewmodel.WeatherViewModel;

/**
 * Created by RickySimon on 1/20/2019.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_weather);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        int prefCount = preferenceScreen.getPreferenceCount();
        for(int i = 0; i < prefCount; i ++){
            Preference p = preferenceScreen.getPreference(i);
            if(!( p instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(p.getKey(),"");
                setPreferenceSummary(p,value);
            }
        }


    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // TODO - no need to requery if units change


        if(key.equals(getString(R.string.pref_location_key))){
            // TODO - remove coordinates
            WeatherSyncUtils.startImmediateSync(getActivity());
        }else if(key.equals(getString(R.string.pref_unit_key))){

        }
        else if(key.equals(getString(R.string.pref_language_key))){
            WeatherPreferences.setLocale(getActivity(),sharedPreferences.getString(key,getString(R.string.pref_language_default)));
            getActivity().recreate();
        }


        Preference p = findPreference(key);
        if(p != null) {
            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(key, "");
                setPreferenceSummary(p, value);
            }
        }
    }


    private void setPreferenceSummary(Preference p, String value){
        if(p instanceof ListPreference){
            ListPreference listPreference = (ListPreference)p;
            int prefIndex = listPreference.findIndexOfValue(value);
            if(prefIndex>=0){
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        else if(p instanceof EditTextPreference){
            EditTextPreference editTextPreference = (EditTextPreference)p;
            editTextPreference.setSummary(editTextPreference.getText());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

    }
}
