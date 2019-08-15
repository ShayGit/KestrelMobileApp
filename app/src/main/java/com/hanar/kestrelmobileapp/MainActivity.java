package com.hanar.kestrelmobileapp;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import com.google.android.material.button.MaterialButton;

@SuppressLint("ClickableViewAccessibility")

public class MainActivity extends AppCompatActivity {
    private boolean isFront;
    private MaterialButton frontBackButton;
    private TransitionDrawable td;
    KestrelLogic kestrelLogic;
    ImageView kestrelImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
      kestrelLogic = new KestrelLogic(this);

    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frontBackButton = findViewById(R.id.frontBackButton);
         kestrelImage= findViewById(R.id.kestrel);
        td = new TransitionDrawable(new Drawable[]{
                getResources().getDrawable(R.drawable.kestrelfront),
                getResources().getDrawable(R.drawable.kestrelback)
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
       kestrelLogic.getLocationHandling().onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    private void changeFrontBackImage() {
        if (isFront) {
            td.startTransition(500);
            kestrelLogic.invisibleKestrelButtons();
            kestrelLogic.fadeOutKestrelMeasurementViews();
            frontBackButton.setText(R.string.back_Button);
        } else {
            td.reverseTransition(500);
            kestrelLogic.fadeInKestrelMeasurementViews();
            kestrelLogic.visibleKestrelButtons();
            frontBackButton.setText(R.string.front_button);
        }
        isFront = !isFront;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            kestrelLogic.getLocationHandling().setIsRandomValues(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
