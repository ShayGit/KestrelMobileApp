package com.hanar.kestrelmobileapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import static android.content.Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT;

@SuppressLint("ClickableViewAccessibility")

public class MainActivity extends AppCompatActivity {
    private boolean isFront;
    private MaterialButton frontBackButton;
    private TransitionDrawable td;
    private KestrelLogic kestrelLogic;
    private boolean locationPref;
    private SequenceExample sq;
    private boolean showGuide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        kestrelLogic = new KestrelLogic(this, frontBackButton);
        SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", MODE_PRIVATE);
        locationPref = settings.getBoolean("locationPref", true);
        CoordinatorLayout coordinatorlayout = findViewById(R.id.appmainlayout);
        coordinatorlayout.getBackground().setAlpha(190);

    }

    @Override
    protected void onResume() {
        super.onResume();



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
        sq = new SequenceExample(this);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        kestrelLogic.getLocationHandling().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public MaterialButton getFrontBackButton() {
        return frontBackButton;
    }


    public void changeFrontBackImage() {
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

        SharedPreferences settings = getApplicationContext().getSharedPreferences("mySettings", MODE_PRIVATE);
        showGuide = settings.getBoolean("showGuide", true);

        if(showGuide)
        {
            sq.onClick(sq.getUserGuideButton());
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("showGuide", false);
        editor.apply();
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
                if(!item.isChecked()) {
                    if (kestrelLogic.getLocationHandling().isDeniedPermissions()) {
                        new MaterialAlertDialogBuilder(this).
                                setCancelable(false)
                                .setMessage("אינך יכול להשתמש באפשרות זו בעקבות היעדר גישה להרשאות מיקום, הורד את האפליקצייה מחדש או אפשר הרשאת מיקום לאפליקציה בהגדרות המכשיר על מנת לאפשר זאת.")
                                .setPositiveButton("אוקיי", (dialog, which) -> {
                                }).create()
                                .show();
                    }
                    else
                    {
                        item.setChecked(!item.isChecked());
                        kestrelLogic.getLocationHandling().setIsRandomValues(!item.isChecked());
                    }
                }
                else{
                    item.setChecked(!item.isChecked());
                    kestrelLogic.getLocationHandling().setIsRandomValues(!item.isChecked());
                }
                return true;
            }
            case R.id.user_guide:
            {
                String url = getString(R.string.user_guide_link);
                Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(url))
                        .setFlags(FLAG_ACTIVITY_LAUNCH_ADJACENT |
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                startActivity(i);
                return true;
            }
            case R.id.user_guide_english:
            {
                String url = getString(R.string.user_guide_english_link);
                Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(url)).setFlags(FLAG_ACTIVITY_LAUNCH_ADJACENT |
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(i);
                return true;
            }
            case R.id.share:
            {
                //shareActionProvider = (ShareActionProvider)menuItem.getActionProvider();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Kestrel Meter App");
                String msg = "\nאפליקציית קסטרל להורדה:\n\n" + "https://play.google.com/store/apps/details?id=" +  BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
                startActivity(Intent.createChooser(shareIntent, "שיתוף באמצעות:"));
                return  true;
            }
            case R.id.contact:
            {
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("mailto:hanarmekarparapps@gmail.com"));
                i.putExtra(Intent.EXTRA_SUBJECT ,"");
                i.putExtra(Intent.EXTRA_TEXT ,"");
                try{
                    startActivity(Intent.createChooser(i,"שלח מייל..."));
                } catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(MainActivity.this,"לא מותקן שירות מייל", Toast.LENGTH_LONG).show();
                }
                return  true;
            }
            case R.id.privacy_agreement:
            {
                Intent intent = new Intent(getApplicationContext(),
                        PrivacyPolicyActivity.class);
                startActivity(intent);
            }


        }

        return super.onOptionsItemSelected(item);
    }

    public boolean getIsFront()
    {
        return this.isFront;
    }

    public KestrelLogic getKestrelLogic()
    {
        return this.kestrelLogic;
    }

}
