package io.github.a11alex11.weatherapp.data;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;



public class DateConverter {
    @TypeConverter
    public static Date toDate(Long dateInMill){
        return dateInMill == null? null : new Date(dateInMill);
    }

    @TypeConverter
    public static Long toMill(Date date){
        return date == null? null : date.getTime();
    }

}
