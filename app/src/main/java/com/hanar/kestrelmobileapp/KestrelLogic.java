package com.hanar.kestrelmobileapp;


import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AppCompatActivity;

public class KestrelLogic {
    private AppCompatActivity activity;
    private LocationHandling locationHandling;
    private MaterialButton powerButton;


    public KestrelLogic(AppCompatActivity aca)
    {
        activity =aca;
        locationHandling = new LocationHandling(activity);
        powerButton = activity.findViewById(R.id.powerButton);
        powerButton.setOnClickListener((v) -> locationHandling.locationRequestOnKestrelStart());

    }

    public LocationHandling getLocationHandling() {
        return locationHandling;
    }
}
