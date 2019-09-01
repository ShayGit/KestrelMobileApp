package com.hanar.kestrelmobileapp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

@SuppressLint("ClickableViewAccessibility")

public class MainActivity extends AppCompatActivity {
    private boolean isFront;
    private MaterialButton frontBackButton;
    private TransitionDrawable td;
    private KestrelLogic kestrelLogic;
    private boolean locationPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        kestrelLogic = new KestrelLogic(this, frontBackButton);
        SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", MODE_PRIVATE);
        locationPref = settings.getBoolean("locationPref", false);
        CoordinatorLayout coordinatorlayout = findViewById(R.id.appmainlayout);
        coordinatorlayout.getBackground().setAlpha(190);

    }

    @Override
    protected void onStop() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("locationPref", kestrelLogic.getLocationSettingItemState());
        editor.apply();
        super.onStop();
    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        MaterialTextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        frontBackButton = findViewById(R.id.frontBackButton);
        ImageView kestrelImage = findViewById(R.id.kestrel);
        td = new TransitionDrawable(new Drawable[]{
                getResources().getDrawable(R.drawable.kestrelfrontnofan),
                getResources().getDrawable(R.drawable.kestrelbacknofan)
        });
        td.setCrossFadeEnabled(true);
        kestrelImage.setImageDrawable(td);
        isFront = true;
        frontBackButton.setText(R.string.front_button);
        frontBackButton.setOnClickListener((v) -> changeFrontBackImage());
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        kestrelLogic.getLocationHandling().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void changeFrontBackImage() {
        if (isFront) {
            td.startTransition(500);
            kestrelLogic.invisibleKestrelButtons();
            kestrelLogic.fadeOutKestrelMeasurementViews();
            kestrelLogic.onFrontDisplayFan(false);
            frontBackButton.setText(R.string.back_Button);

        } else {
            td.reverseTransition(500);
            kestrelLogic.fadeInKestrelMeasurementViews();
            kestrelLogic.visibleKestrelButtons();
            kestrelLogic.onFrontDisplayFan(true);
            frontBackButton.setText(R.string.front_button);

        }
        isFront = !isFront;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem locationItem = menu.findItem(R.id.location_setting);
        locationItem.setChecked(locationPref);
        kestrelLogic.setLocationSettingItem(locationItem);
        kestrelLogic.getLocationHandling().setIsRandomValues(!locationItem.isChecked());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.location_setting: {
                item.setChecked(!item.isChecked());
                kestrelLogic.getLocationHandling().setIsRandomValues(!item.isChecked());
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

}
