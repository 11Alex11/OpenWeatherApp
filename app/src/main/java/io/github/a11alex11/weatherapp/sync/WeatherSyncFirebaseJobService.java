package io.github.a11alex11.weatherapp.sync;


import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

// Sync the weather every so often
//

public class WeatherSyncFirebaseJobService extends JobService {
    private AsyncTask<Void, Void, Void> weatherTask;

    // Job runs on main thread, so must create asynctask!
    @Override
    public boolean onStartJob(final JobParameters job) {
        Log.d("WeatherSyncFirebase","Running job");
        weatherTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                WeatherSyncTask.syncWeather(getApplicationContext());
                jobFinished(job, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                jobFinished(job,false);

            }
        };
        weatherTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d("WeatherSyncFirebase","Stop job");

        if(weatherTask!=null){
            weatherTask.cancel(true);
        }

        return true;
    }
}
