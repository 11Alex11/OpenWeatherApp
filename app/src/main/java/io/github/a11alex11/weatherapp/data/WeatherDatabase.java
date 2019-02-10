package io.github.a11alex11.weatherapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;



// Singleton class for database

@Database(entities = {WeatherEntry.class},version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class WeatherDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "WeatherDatabase";

    private static WeatherDatabase instance;

    public static WeatherDatabase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        WeatherDatabase.class,WeatherDatabase.DATABASE_NAME).build();
            }
        }
        return instance;
    }

    public abstract WeatherDao weatherDao();
}
