package com.hanar.kestrelmobileapp;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.hanar.openweathermap.WeatherData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;


public class KestrelLogic {
    private AppCompatActivity activity;
    private LocationHandling locationHandling;
    private MaterialButton powerButton,rightButton,leftButton;
   // private eKestrelMeasurement eKestrelMeasurementInstance;
    private MaterialTextView measurementTextView;
    private boolean isPowerOn;
    private WeatherData weatherData;


    public KestrelLogic(AppCompatActivity aca) {
        activity = aca;
        locationHandling = new LocationHandling(activity);
        powerButton = activity.findViewById(R.id.powerButton);
        rightButton = activity.findViewById(R.id.rightButton);
        leftButton = activity.findViewById(R.id.leftButton);
        powerButton.setOnClickListener((v) -> onPowerButtonClicked());
        //eKestrelMeasurementInstance = eKestrelMeasurement.Temperature;
        isPowerOn = false;
        locationHandling.uiListener=(this::updateUiWeatherData);
        weatherData = null;
        measurementTextView = activity.findViewById(R.id.measurementTextView);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(measurementTextView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        Typeface myTypeface = Typeface.createFromAsset(activity.getAssets(),
                "digital-7.ttf");
        measurementTextView.setTypeface(myTypeface);
        measurementTextView.setTextColor(Color.BLACK);
    }

    private void onPowerButtonClicked()
    {
        if(isPowerOn){
            isPowerOn = !isPowerOn;
            disableKestrelButtonsWithoutPower();
            invisibleKestrelMeasurementViews();
        }
        else
        {
            isPowerOn = !isPowerOn;
            locationHandling.locationRequestOnKestrelStart();
            visibleKestrelMeasurementViews();
            enableKestrelButtonsWithoutPower();
        }
    }
    LocationHandling getLocationHandling() {
        return locationHandling;
    }

    void enableKestrelButtons() {
        powerButton.setEnabled(true);
        rightButton.setEnabled(true);
        leftButton.setEnabled(true);
    }

    void enableKestrelButtonsWithoutPower() {
        rightButton.setEnabled(true);
        leftButton.setEnabled(true);
    }

    void disableKestrelButtonsWithoutPower() {
        rightButton.setEnabled(false);
        leftButton.setEnabled(false);
    }
    void disableKestrelButtons() {
        powerButton.setEnabled(false);
        rightButton.setEnabled(false);
        leftButton.setEnabled(false);
    }

    @SuppressLint("SetTextI18n")
    private void setKestrelMeasurementViewAndIcon(eKestrelMeasurement eKestrelMeasurementInstance) {
        switch(eKestrelMeasurementInstance)
        {
            case Temperature:
                measurementTextView.setText(Float.toString(weatherData.getTemperature()));
                break;
            case WindSpeed:
                measurementTextView.setText(Float.toString(weatherData.getWindSpeed()));
                break;
            case Humidity:
                measurementTextView.setText(Integer.toString(weatherData.getHumidity()));
                break;
            case WindChill:
                measurementTextView.setText(Double.toString(weatherData.getWindChill()));
                break;
            case DiscomfortIndex:
                measurementTextView.setText(Double.toString(weatherData.getDiscomfortIndex()));
                break;
        }
    }

    void invisibleKestrelMeasurementViews() {
        measurementTextView.setVisibility(View.INVISIBLE);

    }
    void visibleKestrelMeasurementViews() {
        measurementTextView.setVisibility(View.VISIBLE);
    }

    private void updateUiWeatherData(WeatherData inWeatherData)
    {
        if (inWeatherData != null) {
            weatherData = inWeatherData;

            setKestrelMeasurementViewAndIcon(eKestrelMeasurement.Temperature);
            final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
            builder.setMessage(weatherData.toString())
                    .setCancelable(false)
                    .setPositiveButton("אוקיי", (dialog, id) -> {
                        dialog.dismiss();
                    }).create().show();
        }
    }
    public LocationHandling GetLocationHandling()
    {
        return this.locationHandling;
    }
}
