package com.hanar.kestrelmobileapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
@SuppressLint("ClickableViewAccessibility")

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView kestrelImage = findViewById(R.id.kestrel);
        Button frontButton = findViewById(R.id.front_button);
        Button backButton = findViewById(R.id.back_Button);

        frontButton.setOnTouchListener((v, event)-> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    kestrelImage.setImageResource(R.drawable.kestrelfront);
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    v.performClick();
                    frontButton.setPressed(true);
                    backButton.setPressed(false);
                    break;
                }
                default:
                    break;
            }
           return  true;
        });

        backButton.setOnTouchListener((v, event)-> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    kestrelImage.setImageResource(R.drawable.kestrelback);
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    v.performClick();
                    frontButton.setPressed(false);
                    backButton.setPressed(true);
                    break;
                }
                default:
                    break;
            }
            return  true;
        });

        frontButton.setPressed(true);


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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
