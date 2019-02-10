package io.github.a11alex11.weatherapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;



@Dao
public interface WeatherDao {
    @Query("SELECT * FROM weather ORDER BY date")
    LiveData<List<WeatherEntry>> getWeather();

    @Insert
    void insertWeather(WeatherEntry weatherEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWeather(WeatherEntry weatherEntry);

    @Delete
    void deleteWeatherEntry(WeatherEntry weatherEntry);

    // Delete all
    @Query("DELETE FROM weather")
    void deleteAllWeatherEntries();

    // Get entry from date
    @Query("SELECT * FROM weather WHERE date = :date")
    LiveData<WeatherEntry> getWeatherEntryByDate(Date date);

    @Query("SELECT COUNT(*) FROM weather")
    int getWeatherCount();

}
