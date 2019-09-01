package com.hanar.openweathermap;

import android.os.AsyncTask;


import androidx.appcompat.app.AppCompatActivity;

public class JSONWeatherTask extends AsyncTask<Double, Double, AsyncTaskResult<WeatherData>> {
    private AppCompatActivity activity;
    private WeatherDataServiceException ex = null;

    public JSONWeatherTask() {
    }

    @Override
    protected AsyncTaskResult<WeatherData> doInBackground(Double... params) {
        WeatherData weatherData = null;
        String data;
        AsyncTaskResult<WeatherData> atr = null;

        try {
            data = WeatherHttpClient.getWeatherData(params[0], params[1]);
            if (data != null) {
                atr = new AsyncTaskResult<WeatherData>(JSONWeatherParser.jsonWeatherParse(data));
            }



        } catch (WeatherDataServiceException e) {
            atr = new AsyncTaskResult<WeatherData>(e);
        }
        return atr;
    }

}
