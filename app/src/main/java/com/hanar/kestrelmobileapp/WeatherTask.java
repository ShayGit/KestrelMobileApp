package com.hanar.kestrelmobileapp;

import android.os.AsyncTask;


import com.hanar.openweathermap.IWeatherDataService;
import com.hanar.openweathermap.WeatherData;
import com.hanar.openweathermap.WeatherDataServiceException;

import java.lang.ref.WeakReference;

public class WeatherTask extends AsyncTask<Double, Double, AsyncTaskResult<WeatherData>> {
    private WeatherDataServiceException ex = null;
    private IWeatherDataService weatherService;
    private WeakReference<Callback> mCallback;

    interface Callback {
        void postWeatherRetrieval(AsyncTaskResult<WeatherData> atr);
    }

    public WeatherTask(IWeatherDataService weatherService, WeatherTask.Callback callback) {
        this.weatherService = weatherService;
        mCallback = new WeakReference<Callback>(callback);
    }

    @Override
    protected AsyncTaskResult<WeatherData> doInBackground(Double... params) {
        AsyncTaskResult<WeatherData> atr = null;

        try {
            atr = new AsyncTaskResult<WeatherData>(weatherService.getWeatherData(params[0], params[1]));

        } catch (WeatherDataServiceException e) {
            atr = new AsyncTaskResult<WeatherData>(e);
        }
        return atr;
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<WeatherData> atr) {
        if (mCallback.get() != null) {
            mCallback.get().postWeatherRetrieval(atr);
        }
    }


}
