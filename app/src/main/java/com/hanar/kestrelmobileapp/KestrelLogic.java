package com.hanar.kestrelmobileapp;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.hanar.openweathermap.WeatherData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;


public class KestrelLogic {
    private AppCompatActivity activity;
    private LocationHandling locationHandling;
    private MaterialButton powerButton, rightButton, leftButton;
    private eKestrelMeasurement eKestrelMeasurementScreen;
    private MaterialTextView measurementTextView;
    private ImageView measurementIcon1, measurementIcon2, measurementIcon3;
    private boolean isPowerOn;
    private WeatherData weatherData;
    private ValueAnimator valueAnimator1, valueAnimator2;

    public KestrelLogic(AppCompatActivity aca) {
        activity = aca;
        locationHandling = new LocationHandling(activity);
        powerButton = activity.findViewById(R.id.powerButton);
        rightButton = activity.findViewById(R.id.rightButton);
        leftButton = activity.findViewById(R.id.leftButton);
        measurementTextView = activity.findViewById(R.id.measurementTextView);
        measurementIcon1 = activity.findViewById(R.id.measurementIcon1);
        measurementIcon2 = activity.findViewById(R.id.measurementIcon2);
        measurementIcon3 = activity.findViewById(R.id.measurementIcon3);

        weatherData = null;
        isPowerOn = false;
        eKestrelMeasurementScreen = eKestrelMeasurement.Temperature;
        locationHandling.uiListener = (this::updateUiWeatherData);

        powerButton.setOnClickListener((v) -> onPowerButtonClicked());
        rightButton.setOnClickListener((v ->
        {
            if(eKestrelMeasurementScreen!= eKestrelMeasurement.DiscomfortIndex)
            {
                eKestrelMeasurementScreen = eKestrelMeasurementScreen.next();
                setKestrelMeasurementViewAndIcons(eKestrelMeasurementScreen);
            }
        }));
        leftButton.setOnClickListener((v ->
        {
            if(eKestrelMeasurementScreen!= eKestrelMeasurement.Temperature)
            {
                eKestrelMeasurementScreen = eKestrelMeasurementScreen.previous();
                setKestrelMeasurementViewAndIcons(eKestrelMeasurementScreen);
            }
        }));
        disableKestrelButtonsWithoutPower();

        setMeasurementFont();
        initializeAnimationViews();

    }

    private void setMeasurementFont()
    {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(measurementTextView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        Typeface myTypeface = Typeface.createFromAsset(activity.getAssets(),
                "digital-7.ttf");
        measurementTextView.setTypeface(myTypeface);
        measurementTextView.setTextColor(Color.BLACK);
    }
    private void initializeAnimationViews()
    {
        valueAnimator1 = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator1.setDuration(1000);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                measurementTextView.setAlpha(alpha);
                measurementIcon1.setAlpha(alpha);
                measurementIcon2.setAlpha(alpha);
                measurementIcon3.setAlpha(alpha);

            }
        });

        valueAnimator2 = ValueAnimator.ofFloat(1f, 0f);
        valueAnimator2.setDuration(250);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                measurementTextView.setAlpha(alpha);
                measurementIcon1.setAlpha(alpha);
                measurementIcon2.setAlpha(alpha);
                measurementIcon3.setAlpha(alpha);

            }
        });
    }

    private void onPowerButtonClicked() {
        if (isPowerOn) {
            isPowerOn = !isPowerOn;
            disableKestrelButtonsWithoutPower();
            invisibleKestrelMeasurementViews();
        } else {
            isPowerOn = !isPowerOn;
            locationHandling.locationRequestOnKestrelStart();
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
    private void setKestrelMeasurementViewAndIcons(eKestrelMeasurement eKestrelMeasurementInstance) {
        switch (eKestrelMeasurementInstance) {
            case Temperature:
                measurementTextView.setText(Float.toString(weatherData.getTemperature()));
                measurementIcon1.setImageResource(R.drawable.ic_temperatureicon);
                measurementIcon1.setVisibility(View.VISIBLE);
                measurementIcon2.setVisibility(View.INVISIBLE);
                measurementIcon3.setVisibility(View.INVISIBLE);
                break;
            case WindSpeed:
                measurementTextView.setText(Float.toString(weatherData.getWindSpeed()));
                measurementIcon1.setImageResource(R.drawable.ic_windicon);
                measurementIcon1.setVisibility(View.VISIBLE);
                measurementIcon2.setVisibility(View.INVISIBLE);
                measurementIcon3.setVisibility(View.INVISIBLE);
                break;
            case Humidity:
                measurementTextView.setText(Integer.toString(weatherData.getHumidity()));
                measurementIcon1.setImageResource(R.drawable.ic_dropicon);
                measurementIcon2.setImageResource(R.drawable.ic_percentageicon);
                measurementIcon1.setVisibility(View.VISIBLE);
                measurementIcon2.setVisibility(View.VISIBLE);
                measurementIcon3.setVisibility(View.INVISIBLE);
                break;
            case WindChill:
                measurementTextView.setText(Double.toString(weatherData.getWindChill()));
                measurementIcon1.setImageResource(R.drawable.ic_windicon);
                measurementIcon2.setImageResource(R.drawable.ic_temperatureicon);
                measurementIcon1.setVisibility(View.VISIBLE);
                measurementIcon2.setVisibility(View.VISIBLE);
                measurementIcon3.setVisibility(View.INVISIBLE);
                break;
            case DiscomfortIndex:
                measurementTextView.setText(Double.toString(weatherData.getDiscomfortIndex()));
                measurementIcon1.setImageResource(R.drawable.ic_dropicon);
                measurementIcon2.setImageResource(R.drawable.ic_percentageicon);
                measurementIcon3.setImageResource(R.drawable.ic_temperatureicon);
                measurementIcon1.setVisibility(View.VISIBLE);
                measurementIcon2.setVisibility(View.VISIBLE);
                measurementIcon3.setVisibility(View.VISIBLE);
                break;
        }
        measurementTextView.setVisibility(View.VISIBLE);
    }

    private void invisibleKestrelMeasurementViews() {
        measurementTextView.setVisibility(View.INVISIBLE);
        measurementIcon1.setVisibility(View.INVISIBLE);
        measurementIcon2.setVisibility(View.INVISIBLE);
        measurementIcon3.setVisibility(View.INVISIBLE);


    }

    private void visibleKestrelMeasurementViews() {
        measurementTextView.setVisibility(View.VISIBLE);
        measurementIcon1.setVisibility(View.VISIBLE);
        measurementIcon2.setVisibility(View.VISIBLE);
        measurementIcon3.setVisibility(View.VISIBLE);

    }

    void fadeOutKestrelMeasurementViews() {
        if(valueAnimator1.isRunning()) {
            valueAnimator1.cancel();
            valueAnimator2.start();
        }

    }

    void fadeInKestrelMeasurementViews() {
        if(valueAnimator2.isRunning()) {
            valueAnimator2.cancel();
            valueAnimator1.start();
        }
    }

    private void updateUiWeatherData(WeatherData inWeatherData) {
        if (inWeatherData != null) {
            weatherData = inWeatherData;

            setKestrelMeasurementViewAndIcons(eKestrelMeasurement.Temperature);
            enableKestrelButtonsWithoutPower();
           /* final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
            builder.setMessage(weatherData.toString())
                    .setCancelable(false)
                    .setPositiveButton("אוקיי", (dialog, id) -> {
                        dialog.dismiss();
                    }).create().show();*/
        }
        else if (locationHandling.getIsLocationOrNetworkDisabled())
        {
            setIsPowerOn(false);
        }
    }

    public LocationHandling GetLocationHandling() {
        return this.locationHandling;
    }

    private void setIsPowerOn(boolean ispo)
    {
        this.isPowerOn = ispo;
    }
}
