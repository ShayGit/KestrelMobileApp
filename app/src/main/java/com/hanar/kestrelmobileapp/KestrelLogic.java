package com.hanar.kestrelmobileapp;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.hanar.openweathermap.WeatherData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.widget.TextViewCompat;


public class KestrelLogic {
    private AppCompatActivity activity;
    private LocationHandling locationHandling;
    private MaterialButton powerButton, rightButton, leftButton;
    private eKestrelMeasurement eKestrelMeasurementScreen;
    private MaterialTextView measurementTextView, measurementIconText, holdText, measurementExplain;
    private AppCompatImageView measurementIcon1, measurementIcon2, measurementIcon3, measurementIcon4, kestrelFan;
    private boolean isPowerOn;
    private WeatherData weatherData;
    private ValueAnimator valueAnimator1, valueAnimator2;
    private TransitionDrawable td;
    private MaterialButton frontBackButton;
    private ConstraintLayout kestrelLayout;
    private RotateAnimation rotate;
    private MenuItem locationSettingItem;
    private Handler handler;
    private View kestrelLight;
    private long downTimePower, upTimePower, downTimeLeft, upTimeLeft;
    private boolean isHoldChanged,upNotDown;
    private int downTwiceNoUp;


    @SuppressLint("ClickableViewAccessibility")
    KestrelLogic(AppCompatActivity aca, MaterialButton fbb) {
        activity = aca;
        locationHandling = new LocationHandling(activity);
        powerButton = activity.findViewById(R.id.powerButton);
        rightButton = activity.findViewById(R.id.rightButton);
        leftButton = activity.findViewById(R.id.leftButton);
        measurementTextView = activity.findViewById(R.id.measurementTextView);
        measurementExplain = activity.findViewById(R.id.measurementExplain);
        measurementIcon1 = activity.findViewById(R.id.measurementIcon1);
        measurementIcon2 = activity.findViewById(R.id.measurementIcon2);
        measurementIcon3 = activity.findViewById(R.id.measurementIcon3);
        measurementIcon4 = activity.findViewById(R.id.measurementIcon4);
        measurementIconText = activity.findViewById(R.id.measurementIconText);
        measurementIcon1.setImageResource(R.drawable.ic_windicon);
        measurementIcon2.setImageResource(R.drawable.ic_dropicon);
        measurementIcon3.setImageResource(R.drawable.ic_percentageicon);
        measurementIcon4.setImageResource(R.drawable.ic_temperatureicon);
        kestrelFan = activity.findViewById(R.id.kestrelfan);
        kestrelLayout = activity.findViewById(R.id.kestrelayout);
        frontBackButton = fbb;
        handler = new Handler(Looper.getMainLooper());
        kestrelLight = activity.findViewById(R.id.kestrelLight);
        holdText = activity.findViewById(R.id.holdText);
        invisibleKestrelMeasurementViews();
        isHoldChanged = false;
        downTwiceNoUp = 0;


        weatherData = null;
        isPowerOn = false;
        locationHandling.setIsPowerOn(false);
        setDefaultMeasurement();
        locationHandling.uiListener = (this::updateUiWeatherData);
        rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(900);
        kestrelFan.startAnimation(rotate);


        td = new TransitionDrawable(new Drawable[]{
                activity.getResources().getDrawable(R.drawable.kestrelfan),
                activity.getResources().getDrawable(R.drawable.kestrelfan)
        });
        td.setCrossFadeEnabled(true);
        kestrelFan.setImageDrawable(td);

        powerButton.setOnTouchListener(this::onTouch);

        rightButton.setOnClickListener((v ->
        {
            onRightButtonClick();
        }));


        leftButton.setOnTouchListener(this::onTouch);
        disableKestrelButtonsWithoutPower();

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(holdText, 1, 12, 1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        holdText.setTextColor(Color.BLACK);
        holdText.setText(activity.getResources().getString(R.string.hold_text));

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(measurementIconText, 1, 12, 1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        measurementIconText.setTextColor(Color.BLACK);

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(measurementExplain, 1, 20, 1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        measurementExplain.setTextColor(Color.BLACK);
        measurementExplain.setTypeface(Typeface.DEFAULT_BOLD);

        setMeasurementFont();
        initializeAnimationViews();

    }

    private boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.powerButton) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downTimePower = event.getDownTime();
                    powerButton.setPressed(true);

                    if(!leftButton.isPressed()) {
                        isHoldChanged = false;
                    }
                   /* else if ( !isHoldChanged) {
                        holdTextVisibilityChange();
                        isHoldChanged = true;
                    }*/
                    break;
                }
                case MotionEvent.ACTION_MOVE: {

                    if (event.getEventTime() - downTimePower > 2000 && !isHoldChanged) {
                        onPowerButtonPressed3Sec();
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {

                    upTimePower = event.getEventTime();
                    if (upTimePower - downTimePower < 500 && !isHoldChanged) {
                        onPowerButtonClicked();
                    }
                    v.setPressed(false);
                }
            }
            return true;
        } else if (v.getId() == R.id.leftButton) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downTimePower = event.getDownTime();
                    v.setPressed(true);
                    if(!powerButton.isPressed()) {
                        isHoldChanged = false;
                    }
                    else if (!isHoldChanged) {
                        holdTextVisibilityChange();
                        isHoldChanged = true;
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {

                    upTimeLeft = event.getEventTime();
                    if ( !isHoldChanged) {
                        eKestrelMeasurementScreen = eKestrelMeasurementScreen.previous();
                        setKestrelMeasurementViewAndIcons(eKestrelMeasurementScreen);
                    }
                    v.setPressed(false);

                }
            }
            return true;
        }
        return false;
    }

    private void setMeasurementFont() {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(measurementTextView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        Typeface myTypeface = Typeface.createFromAsset(activity.getAssets(),
                "digital-7.ttf");
        measurementTextView.setTypeface(myTypeface);
        measurementTextView.setTextColor(Color.BLACK);
    }

    void onFrontDisplayFan(boolean isFront) {
        if (isFront) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(kestrelLayout);
            constraintSet.setVerticalBias(R.id.kestrelfan, 0.09f);
            constraintSet.setHorizontalBias(R.id.kestrelfan, 0.58f);
            constraintSet.applyTo(kestrelLayout);
            td.reverseTransition(1000);
        } else {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(kestrelLayout);
            constraintSet.setVerticalBias(R.id.kestrelfan, 0.083f);
            constraintSet.setHorizontalBias(R.id.kestrelfan, 0.43f);
            constraintSet.applyTo(kestrelLayout);
            td.startTransition(1000);
        }
    }

    private void initializeAnimationViews() {
        valueAnimator1 = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator1.setDuration(1000);
        valueAnimator1.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            measurementTextView.setAlpha(alpha);
            measurementIcon1.setAlpha(alpha);
            measurementIcon2.setAlpha(alpha);
            measurementIcon3.setAlpha(alpha);
            measurementIcon4.setAlpha(alpha);
            measurementIconText.setAlpha(alpha);
            measurementExplain.setAlpha(alpha);
            kestrelLight.setAlpha(alpha);
            holdText.setAlpha(alpha);

        });

        valueAnimator2 = ValueAnimator.ofFloat(1f, 0f);
        valueAnimator2.setDuration(250);
        valueAnimator2.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            measurementTextView.setAlpha(alpha);
            measurementIcon1.setAlpha(alpha);
            measurementIcon2.setAlpha(alpha);
            measurementIcon3.setAlpha(alpha);
            measurementIcon4.setAlpha(alpha);
            measurementIconText.setAlpha(alpha);
            measurementExplain.setAlpha(alpha);
            kestrelLight.setAlpha(alpha);
            holdText.setAlpha(alpha);

        });
    }

     void onPowerButtonClicked() {
        if (!isPowerOn) {
            isPowerOn = !isPowerOn;
            frontBackButton.setEnabled(false);
            locationHandling.setIsPowerOn(!isPowerOn);
            locationHandling.locationRequestOnKestrelStart();
            locationSettingItem.setChecked(!locationHandling.getIsRandomValues());
        } else if(locationHandling.getProgressBar().getVisibility() ==View.INVISIBLE){
            int isExecuted = 0;
            if (handler.hasMessages(isExecuted)) {
                handler.removeCallbacksAndMessages(null);
                //set background light off
                kestrelLight.setVisibility(View.INVISIBLE);
            } else {
                //set background light on
                kestrelLight.setVisibility(View.VISIBLE);
                handler.sendEmptyMessage(isExecuted);
                handler.postDelayed(() -> {
                    //set background light off
                    kestrelLight.setVisibility(View.INVISIBLE);
                    handler.removeMessages(isExecuted);
                }, 10000);
            }
        }
    }

     void onPowerButtonPressed3Sec() {
        if (isPowerOn) {
            isPowerOn = !isPowerOn;
            locationHandling.setIsPowerOn(!isPowerOn);
            handler.removeCallbacksAndMessages(null);

            disableKestrelButtonsWithoutPower();
            invisibleKestrelMeasurementViews();
            locationHandling.setProgressBar(false);
            frontBackButton.setEnabled(true);


        }
    }

    LocationHandling getLocationHandling() {
        return locationHandling;
    }

    void visibleKestrelButtons() {
        powerButton.setVisibility(View.VISIBLE);
        rightButton.setVisibility(View.VISIBLE);
        leftButton.setVisibility(View.VISIBLE);
    }

    private void enableKestrelButtonsWithoutPower() {
        rightButton.setEnabled(true);
        leftButton.setEnabled(true);
    }

    private void disableKestrelButtonsWithoutPower() {
        rightButton.setEnabled(false);
        leftButton.setEnabled(false);
    }

    void invisibleKestrelButtons() {
        powerButton.setVisibility(View.INVISIBLE);
        rightButton.setVisibility(View.INVISIBLE);
        leftButton.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("SetTextI18n")
    public void setKestrelMeasurementViewAndIcons(eKestrelMeasurement eKestrelMeasurementInstance) {
        switch (eKestrelMeasurementInstance) {
            case Temperature:
                measurementTextView.setText(String.format("%.1f",weatherData.getTemperature()));
                measurementExplain.setMaxLines(1);
                measurementExplain.setText(activity.getResources().getString(R.string.temperature));
                measurementIcon4.setVisibility(View.VISIBLE);
                measurementIcon1.setVisibility(View.INVISIBLE);
                measurementIcon2.setVisibility(View.INVISIBLE);
                measurementIcon3.setVisibility(View.INVISIBLE);
                measurementIconText.setVisibility(View.VISIBLE);
                measurementIconText.setText("°C");
                break;
            case WindSpeed:
                measurementTextView.setText(String.format("%.1f",weatherData.getWindSpeed()));
                measurementExplain.setMaxLines(3);
                measurementExplain.setText(activity.getResources().getString(R.string.windSpeed));
                measurementIcon1.setVisibility(View.VISIBLE);
                measurementIcon2.setVisibility(View.INVISIBLE);
                measurementIcon3.setVisibility(View.INVISIBLE);
                measurementIcon4.setVisibility(View.INVISIBLE);
                measurementIconText.setVisibility(View.VISIBLE);
                measurementIconText.setText("m/s");
                break;
            case Humidity:
                measurementTextView.setText(String.format("%d",weatherData.getHumidity()));
                measurementExplain.setMaxLines(3);
                measurementExplain.setText(activity.getResources().getString(R.string.humidity));
                measurementIcon1.setVisibility(View.INVISIBLE);
                measurementIcon2.setVisibility(View.VISIBLE);
                measurementIcon3.setVisibility(View.VISIBLE);
                measurementIcon4.setVisibility(View.INVISIBLE);
                measurementIconText.setVisibility(View.VISIBLE);
                measurementIconText.setVisibility(View.INVISIBLE);
                break;
            case WindChill:
                measurementTextView.setText(String.format("%.1f",weatherData.getWindChill()));
                measurementExplain.setMaxLines(3);
                measurementExplain.setText(activity.getResources().getString(R.string.windChill));
                measurementIcon1.setVisibility(View.VISIBLE);
                measurementIcon2.setVisibility(View.INVISIBLE);
                measurementIcon3.setVisibility(View.INVISIBLE);
                measurementIcon4.setVisibility(View.VISIBLE);
                measurementIconText.setVisibility(View.VISIBLE);
                measurementIconText.setText("°C");
                break;
            case DiscomfortIndex:
                measurementTextView.setText(String.format("%.1f",weatherData.getDiscomfortIndex()));
                measurementExplain.setMaxLines(3);
                measurementExplain.setText(activity.getResources().getString(R.string.discomfortIndex));
                measurementIcon1.setVisibility(View.INVISIBLE);
                measurementIcon2.setVisibility(View.VISIBLE);
                measurementIcon3.setVisibility(View.VISIBLE);
                measurementIcon4.setVisibility(View.VISIBLE);
                measurementIconText.setVisibility(View.VISIBLE);
                measurementIconText.setText("°C");
                break;
        }
        measurementTextView.setVisibility(View.VISIBLE);
        measurementExplain.setVisibility(View.VISIBLE);
    }

    private void invisibleKestrelMeasurementViews() {
        measurementTextView.setVisibility(View.INVISIBLE);
        measurementExplain.setVisibility(View.INVISIBLE);
        measurementIcon1.setVisibility(View.INVISIBLE);
        measurementIcon2.setVisibility(View.INVISIBLE);
        measurementIcon3.setVisibility(View.INVISIBLE);
        measurementIcon4.setVisibility(View.INVISIBLE);
        measurementIconText.setVisibility(View.INVISIBLE);
        //set background light off
        kestrelLight.setVisibility(View.INVISIBLE);
        holdText.setVisibility(View.INVISIBLE);
    }

    private void visibleKestrelMeasurementViews() {
        measurementTextView.setVisibility(View.VISIBLE);
        measurementIcon1.setVisibility(View.VISIBLE);
        measurementIcon2.setVisibility(View.VISIBLE);
        measurementIcon3.setVisibility(View.VISIBLE);
    }

    void fadeOutKestrelMeasurementViews() {
        if (valueAnimator1.isRunning()) {
            valueAnimator1.cancel();
        }
        valueAnimator2.start();
    }

    void fadeInKestrelMeasurementViews() {
        if (valueAnimator2.isRunning()) {
            valueAnimator2.cancel();
        }
        valueAnimator1.start();
    }

    private void updateUiWeatherData(WeatherData inWeatherData) {
        if (inWeatherData != null) {
            if (isPowerOn) {
                weatherData = inWeatherData;

                setKestrelMeasurementViewAndIcons(eKestrelMeasurementScreen);
                enableKestrelButtonsWithoutPower();
                if (weatherData.getWindSpeed() == 0) {
                    kestrelFan.clearAnimation();
                } else {
                    kestrelFan.startAnimation(rotate);
                }
            }
           /* final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
            builder.setMessage(weatherData.toString())
                    .setCancelable(false)
                    .setPositiveButton("אוקיי", (dialog, id) -> {
                        dialog.dismiss();
                    }).create().show();*/
        } else if (locationHandling.getIsLocationOrNetworkDisabled()) {
            setIsPowerOn(false);
            locationHandling.setIsPowerOn(false);
        }
        frontBackButton.setEnabled(true);

    }

    public LocationHandling GetLocationHandling() {
        return this.locationHandling;
    }

     boolean getIsPowerOn() {
        return this.isPowerOn;
    }

    private void setIsPowerOn(boolean ispo) {
        this.isPowerOn = ispo;
    }

    void setLocationSettingItem(MenuItem locationSettingItem) {
        this.locationSettingItem = locationSettingItem;
        locationHandling.setLocationSettingItem(locationSettingItem);
    }
    MenuItem getLocationSettingItem() {
        return this.locationSettingItem;
    }

    boolean getLocationSettingItemState() {
        return locationSettingItem.isChecked();
    }

    private void holdTextVisibilityChange() {
        if (holdText.getVisibility() == View.VISIBLE) {
            holdText.setVisibility(View.INVISIBLE);
        } else {
            holdText.setVisibility(View.VISIBLE);

        }
    }

     void setDefaultMeasurement()
    {
        eKestrelMeasurementScreen = eKestrelMeasurement.WindSpeed;
    }

    void onRightButtonClick()
    {
        eKestrelMeasurementScreen = eKestrelMeasurementScreen.next();
        setKestrelMeasurementViewAndIcons(eKestrelMeasurementScreen);
    }


}
