package com.hanar.kestrelmobileapp;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Looper;
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
    private MaterialTextView measurementTextView, measurementIconText, holdText;
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
    private long downTime, upTime;


    KestrelLogic(AppCompatActivity aca, MaterialButton fbb) {
        activity = aca;
        locationHandling = new LocationHandling(activity);
        powerButton = activity.findViewById(R.id.powerButton);
        rightButton = activity.findViewById(R.id.rightButton);
        leftButton = activity.findViewById(R.id.leftButton);
        measurementTextView = activity.findViewById(R.id.measurementTextView);
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


        weatherData = null;
        isPowerOn = false;
        eKestrelMeasurementScreen = eKestrelMeasurement.WindSpeed;
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

        powerButton.setOnClickListener((v) ->
        {
                  /*  if(leftButton.isPressed()&&isPowerOn)
                    {
                        holdTextVisibilityChange();
                    }
                    else {*/
            onPowerButtonClicked();
            //   }
        });
        powerButton.setOnLongClickListener((v) ->
        {
           /*  if(leftButton.isPressed()&&isPowerOn)
             {
                 holdTextVisibilityChange();
             }
             else
             {*/
            onPowerButtonPressed3Sec();
            // }
            return true;
        });

        powerButton.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downTime = motionEvent.getDownTime();
                    if (leftButton.isPressed()) {
                        holdTextVisibilityChange();
                        leftButton.setPressed(false);
                    }
                    return true;
                }
                case MotionEvent.ACTION_MOVE: {
                     if (motionEvent.getEventTime() - downTime > 3000) {
                         view.performLongClick();
                         return true;
                     }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    if (motionEvent.getEventTime() - downTime < 500) {
                        view.performClick();
                        return true;
                    }
                }
            }
            return false;
        });
        /*powerButton.setOnTouchListener((v, event) -> {
            boolean isTurnedOffNow = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP: {
                    if (!(isTurnedOffNow || getIsPowerOn())) {
                        onPowerButtonClicked();
                    }
                    return  true;
                }
                case MotionEvent.ACTION_DOWN: {
                    down = event.getEventTime();
                    return false;
                }
                case MotionEvent.ACTION_MOVE: {
                    up = event.getEventTime();
                    if (up - down > 3000 && getIsPowerOn()) {
                        onPowerButtonPressed3Sec();
                        isTurnedOffNow = true;
                        return true;
                    }
                    break;
                }
            }
            return false;
        });*/
        rightButton.setOnClickListener((v ->
        {
            eKestrelMeasurementScreen = eKestrelMeasurementScreen.next();
            setKestrelMeasurementViewAndIcons(eKestrelMeasurementScreen);
        }));
        leftButton.setOnClickListener((v ->
        {
           /* if(powerButton.isPressed() && isPowerOn)
            {
                holdTextVisibilityChange();
            }
            else {*/
            eKestrelMeasurementScreen = eKestrelMeasurementScreen.previous();
            setKestrelMeasurementViewAndIcons(eKestrelMeasurementScreen);
            // }
        }));
        leftButton.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    if (powerButton.isPressed()) {
                        holdTextVisibilityChange();
                        powerButton.setPressed(false);
                    }
                    return  true;
                }
                case MotionEvent.ACTION_UP: {
                    {
                            view.performClick();
                            return true;
                    }
                }
            }
            return false;
        });
        disableKestrelButtonsWithoutPower();

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(holdText, 1, 12, 1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        holdText.setTextColor(Color.BLACK);
        holdText.setText(activity.getResources().getString(R.string.hold_text));

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(measurementIconText, 1, 12, 1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        measurementIconText.setTextColor(Color.BLACK);
        setMeasurementFont();
        initializeAnimationViews();

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
            kestrelLight.setAlpha(alpha);
            holdText.setAlpha(alpha);

        });
    }

    private void onPowerButtonClicked() {
        if (!isPowerOn) {
            isPowerOn = !isPowerOn;
            frontBackButton.setEnabled(false);
            locationHandling.locationRequestOnKestrelStart();
            locationSettingItem.setChecked(!locationHandling.getIsRandomValues());
        } else {
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

    private void onPowerButtonPressed3Sec() {
        if (isPowerOn) {
            isPowerOn = !isPowerOn;
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
    private void setKestrelMeasurementViewAndIcons(eKestrelMeasurement eKestrelMeasurementInstance) {
        switch (eKestrelMeasurementInstance) {
            case Temperature:
                measurementTextView.setText(Float.toString(weatherData.getTemperature()));
                measurementIcon4.setVisibility(View.VISIBLE);
                measurementIcon1.setVisibility(View.INVISIBLE);
                measurementIcon2.setVisibility(View.INVISIBLE);
                measurementIcon3.setVisibility(View.INVISIBLE);
                measurementIconText.setVisibility(View.VISIBLE);
                measurementIconText.setText("°C");
                break;
            case WindSpeed:
                measurementTextView.setText(Float.toString(weatherData.getWindSpeed()));
                measurementIcon1.setVisibility(View.VISIBLE);
                measurementIcon2.setVisibility(View.INVISIBLE);
                measurementIcon3.setVisibility(View.INVISIBLE);
                measurementIcon4.setVisibility(View.INVISIBLE);
                measurementIconText.setVisibility(View.VISIBLE);
                measurementIconText.setText("m/s");
                break;
            case Humidity:
                measurementTextView.setText(Integer.toString(weatherData.getHumidity()));
                measurementIcon1.setVisibility(View.INVISIBLE);
                measurementIcon2.setVisibility(View.VISIBLE);
                measurementIcon3.setVisibility(View.VISIBLE);
                measurementIcon4.setVisibility(View.INVISIBLE);
                measurementIconText.setVisibility(View.VISIBLE);
                measurementIconText.setVisibility(View.INVISIBLE);
                break;
            case WindChill:
                measurementTextView.setText(Float.toString(weatherData.getWindChill()));
                measurementIcon1.setVisibility(View.VISIBLE);
                measurementIcon2.setVisibility(View.INVISIBLE);
                measurementIcon3.setVisibility(View.INVISIBLE);
                measurementIcon4.setVisibility(View.VISIBLE);
                measurementIconText.setVisibility(View.VISIBLE);
                measurementIconText.setText("°C");
                break;
            case DiscomfortIndex:
                measurementTextView.setText(Float.toString(weatherData.getDiscomfortIndex()));
                measurementIcon1.setVisibility(View.INVISIBLE);
                measurementIcon2.setVisibility(View.VISIBLE);
                measurementIcon3.setVisibility(View.VISIBLE);
                measurementIcon4.setVisibility(View.VISIBLE);
                measurementIconText.setVisibility(View.VISIBLE);
                measurementIconText.setText("°C");
                break;
        }
        measurementTextView.setVisibility(View.VISIBLE);
    }

    private void invisibleKestrelMeasurementViews() {
        measurementTextView.setVisibility(View.INVISIBLE);
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
        }
        frontBackButton.setEnabled(true);

    }

    public LocationHandling GetLocationHandling() {
        return this.locationHandling;
    }

    private void setIsPowerOn(boolean ispo) {
        this.isPowerOn = ispo;
    }

    private boolean getIsPowerOn() {
        return this.isPowerOn;
    }

    void setLocationSettingItem(MenuItem locationSettingItem) {
        this.locationSettingItem = locationSettingItem;
        locationHandling.setLocationSettingItem(locationSettingItem);
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


}
