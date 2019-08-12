package com.hanar.kestrelmobileapp;


import android.graphics.Color;

import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AppCompatActivity;

public class KestrelLogic {
    private AppCompatActivity activity;
    private LocationHandling locationHandling;
    private MaterialButton powerButton;
    private eKestrelMeasurement eKestrelMeasurement;



    public KestrelLogic(AppCompatActivity aca) {
        activity = aca;
        locationHandling = new LocationHandling(activity);
        powerButton = activity.findViewById(R.id.powerButton);
        powerButton.setOnClickListener((v) -> locationHandling.locationRequestOnKestrelStart());
        eKestrelMeasurement = eKestrelMeasurement.Temperature;

    }

    public LocationHandling getLocationHandling() {
        return locationHandling;
    }

    public void enableKestrelButtons() {
        powerButton.setEnabled(true);
    }

    public void disableKestrelButtons() {
        powerButton.setEnabled(false);
    }
    public void visibleKestrelMeasurementViews() {
        switch(eKestrelMeasurement)
        {
            case Temperature:
                break;
            case WindSpeed:
                break;
            case Humidity:
                break;
            case WindChill:
                break;
            case DiscomfortIndex:
                break;
        }
    }

    public void invisibleKestrelMeasurementViews() {
        switch(eKestrelMeasurement)
        {
            case Temperature:
                break;
            case WindSpeed:
                break;
            case Humidity:
                break;
            case WindChill:
                break;
            case DiscomfortIndex:
                break;
        }
    }


}
