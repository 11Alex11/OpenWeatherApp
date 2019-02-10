package io.github.a11alex11.weatherapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;


// This class defined what data will be stored in the database using Room

@Entity(tableName = "weather")
public class WeatherEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private int weatherId;
    @NonNull
    private double minTemp;
    @NonNull
    private double maxTemp;
    @NonNull
    private int humidity;
    @NonNull
    private double pressure;
    @NonNull
    private double windSpeed;
    @NonNull
    private double windDirection;
    @NonNull
    private Date date;
    @NonNull
    private String description;

    public WeatherEntry(int id, int weatherId, double minTemp, double maxTemp, int humidity,double pressure,double windSpeed, double windDirection, Date date, String description){
        this.id = id;
        this.weatherId = weatherId;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.date = date;
        this.description = description;
    }

    @Ignore
    public WeatherEntry(int weatherId, double minTemp, double maxTemp, int humidity,double pressure,double windSpeed, double windDirection, Date date, String description){
        this.weatherId = weatherId;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.date = date;
        this.description = description;

    }

    public int getId(){return id;}
    public int getWeatherId(){return weatherId;}
    public double getMinTemp(){return minTemp;}
    public double getMaxTemp(){return maxTemp;}
    public int getHumidity(){return humidity;}
    public double getPressure(){return pressure;}
    public double getWindSpeed(){return windSpeed;}
    public double getWindDirection(){return windDirection;}
    public Date getDate(){return date;}
    public String getDescription(){return description;}


    public void setId(int id){this.id = id;}
    public void setWeatherId(int weatherId){this.weatherId = weatherId;}
    public void setMinTemp(double minTemp){this.minTemp = minTemp;}
    public void setMaxTemp(double maxTemp){this.maxTemp = maxTemp;}
    public void setHumidity(int humidity){this.humidity = humidity;}
    public void setPressure(double pressure){this.pressure = pressure;}
    public void setWind(double windSpeed){this.windSpeed = windSpeed;}
    public void setDegrees(double windDirection){this.windDirection = windDirection;}
    public void setDate(Date date){this.date = date;}
    public void setDescription(String description){this.description = description;}



}
