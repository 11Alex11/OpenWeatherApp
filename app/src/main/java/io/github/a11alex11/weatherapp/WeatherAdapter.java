package io.github.a11alex11.weatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import io.github.a11alex11.weatherapp.data.WeatherEntry;
import io.github.a11alex11.weatherapp.utilities.WeatherDateUtils;
import io.github.a11alex11.weatherapp.utilities.WeatherUtils;



public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherAdapterViewHolder> {
    private List<WeatherEntry> weatherData;
    private String TAG = WeatherAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_DAYS = 0;
    private static final int VIEW_TYPE_TODAY = 1;


    private WeatherAdapterOnClickHandler clickHandler;
    private Context context;

    private boolean useTodayLayout;

    public WeatherAdapter(@NonNull Context context,WeatherAdapterOnClickHandler handler){
        clickHandler = handler;
        this.context = context;
        useTodayLayout = this.context.getResources().getBoolean(R.bool.use_today_layout);
    }

    @Override
    public WeatherAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int viewToInflate = R.layout.weather_list_item;
        if(viewType == VIEW_TYPE_TODAY){
            viewToInflate = R.layout.weather_list_item_today;
        }
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(viewToInflate,parent,false);
        return new WeatherAdapterViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0 && useTodayLayout){

            return VIEW_TYPE_TODAY;
        }
        return VIEW_TYPE_DAYS;

    }

    @Override
    public void onBindViewHolder(WeatherAdapter.WeatherAdapterViewHolder holder, int position) {
        WeatherEntry curEntry = weatherData.get(position);
        Date date = curEntry.getDate();
        String dateString = WeatherDateUtils.getFriendlyDateString(context,date.getTime(),false);

        int weatherId = curEntry.getWeatherId();
        String description = WeatherUtils.getStringForWeatherCondition(context,weatherId);
        String descriptionA11y = context.getString(R.string.a11y_forecast,description);


        double highInCelcius = curEntry.getMaxTemp();
        double lowInCelcius = curEntry.getMinTemp();
        Log.d(TAG,highInCelcius + " " + lowInCelcius);

        String highFormatted = WeatherUtils.formatTemperatureNoUnit(context,highInCelcius);
        String highA11y = context.getString(R.string.a11y_high_temp,highFormatted);

        String lowFormatted = WeatherUtils.formatTemperatureNoUnit(context,lowInCelcius);
        String lowA11y = context.getString(R.string.a11y_low_temp,lowFormatted);

        int image = WeatherUtils.getSmallArtResourceForWeatherCondition(weatherId);
        String imageA11y = context.getString(R.string.a11y_forecast_icon,description);

        holder.weatherHigh.setText(highFormatted);
        holder.weatherHigh.setContentDescription(highA11y);

        holder.weatherLow.setText(lowFormatted);
        holder.weatherLow.setContentDescription(lowA11y);

        holder.description.setText(description);
        holder.description.setContentDescription(descriptionA11y);

        holder.date.setText(dateString);


        holder.icon.setImageResource(image);
        holder.icon.setContentDescription(imageA11y);



    }

    @Override
    public int getItemCount() {
        if(weatherData==null){
            return 0;
        }
        else{
            return weatherData.size();
        }
    }

    public void setWeatherData(List<WeatherEntry> data){
        weatherData = data;
        notifyDataSetChanged();

    }
    public class WeatherAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView weatherHigh;
        private final TextView weatherLow;
        private final TextView date;
        private final TextView description;
        private final ImageView icon;



        public WeatherAdapterViewHolder(View v){
            super(v);

            weatherHigh = v.findViewById(R.id.tv_high);
            weatherLow = v.findViewById(R.id.tv_low);
            date = v.findViewById(R.id.tv_date);
            description = v.findViewById(R.id.tv_weather);
            icon = v.findViewById(R.id.iv_weather_icon);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Log.d(TAG,"Clicked");
            clickHandler.weatherClick(weatherData.get(pos));
        }
    }

    public interface WeatherAdapterOnClickHandler{
        void weatherClick(WeatherEntry weather);
    }
}
