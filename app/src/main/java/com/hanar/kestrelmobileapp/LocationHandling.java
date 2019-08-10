package com.hanar.kestrelmobileapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class LocationHandling {
    private FusedLocationProviderClient fusedLocationClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Double longtitude = 0.0, latitude = 0.0;
    private ProgressBar pB;
    private boolean isRandomValues;
    private AppCompatActivity activity;

    public LocationHandling(AppCompatActivity activityChain) {
        activity = activityChain;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        isRandomValues = false;
        pB = (ProgressBar) activity.findViewById(R.id.pb);

    }

    public void locationRequestOnKestrelStart() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (!isRandomValues) {
                new MaterialAlertDialogBuilder(activity).
                        setCancelable(false)
                        .setMessage("האפליקציה מעוניינת להשתמש במיקומך האחרון על מנת שהקסטרל יציג נתונים על פי מיקומך, במידה ואינך מעוניינ/ת בכך, הקסטרל יציג נתונים אקראיים")
                        .setPositiveButton("הבנתי", (dialog, which) -> {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        }).setNegativeButton("לא מעוניינ/ת", (dialog, which) -> {
                    setUiRandomValues();
                }).create()
                        .show();
            } else {
                setUiRandomValues();
            }
        } else {
            Toast.makeText(activity, "Permission granted", Toast.LENGTH_SHORT).show();
            // locationAndInternetCheck();
            if (!isRandomValues)
                getLocationAndUpdateUI();
        }
    }

    private void setUiRandomValues() {
        isRandomValues = true;
    }

    @SuppressLint("MissingPermission")
    private void getLocationAndUpdateUI() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    pB.setVisibility(View.VISIBLE);
                    locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationRequest.setInterval(0);
                    locationRequest.setFastestInterval(0);
                    locationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            if (locationResult == null) {
                                return;
                            }
                            for (Location location : locationResult.getLocations()) {
                                if (location != null) {
                                    //  if (longtitude == 0.0) {
                                    Toast.makeText(activity, Double.toString(location.getLongitude()), Toast.LENGTH_SHORT).show();
                                    pB.setVisibility(View.INVISIBLE);
                                    retrieveWeatherByLocation(location.getLongitude(), location.getLatitude());
                                    updateKestrelUI();

                                    //}
                                    break;
                                }
                            }
                            if (fusedLocationClient != null) {
                                fusedLocationClient.removeLocationUpdates(this);
                            }
                            Toast.makeText(activity, Double.toString(longtitude), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onLocationAvailability(LocationAvailability locationAvailability) {
                            if (!locationAvailability.isLocationAvailable()) {
                                pB.setVisibility(View.INVISIBLE);
                                Toast.makeText(activity, Double.toString(longtitude), Toast.LENGTH_SHORT).show();
                                if (longtitude == 0.0 && latitude == 0.0) {
                                    isLocationEnabled();
                                } else {
                                    retrieveWeatherByLocation(longtitude, latitude);
                                    updateKestrelUI();
                                }
                            }
                        }
                    };
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                } else {
                    Toast.makeText(activity, Double.toString(location.getLongitude()), Toast.LENGTH_SHORT).show();
                    retrieveWeatherByLocation(location.getLongitude(), location.getLatitude());
                    updateKestrelUI();
                }
            }
        }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    // try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    if (longtitude == 0.0 && latitude == 0.0) {
                        // ResolvableApiException resolvable = (ResolvableApiException) e;
                        // resolvable.startResolutionForResult(MainActivity.this,
                        //       REQUEST_CHECK_SETTINGS);
                        isLocationEnabled();
                    } else {
                        retrieveWeatherByLocation(longtitude, latitude);
                        updateKestrelUI();
                    }
                    //   } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                    //  }
                }
            }
        });
    }

    private void isLocationEnabled() {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
            builder.setMessage("נראה שהמיקום מופעל אך בכל זאת יש בעיה כלשהי, נאלץ לבטל את פיצ'ר זה ויוצגו ערכים אקראיים, לנסיון נוסף הפעל את האפליקציה מחדש")
                    .setCancelable(false)
                    .setPositiveButton("הבנתי", (dialog, id) -> {
                        setUiRandomValues();
                        dialog.dismiss();
                    }).create().show();
        }
    }

    private void retrieveWeatherByLocation(Double i_longtitude, Double i_latitude) {
        longtitude = i_longtitude;
        latitude = i_latitude;

        try {
            checkNetworkConnected();

        } catch (Exception e) {
        }
    }

    private void updateKestrelUI() {
// invoke kestrelLogicListener
    }

    private void buildAlertMessageNoGps() {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        builder.setMessage("אנא הפעל את המיקום במכשירך על מנת שפיצ'ר זה יעבוד, במידה ואינך מעוניין, לחץ על 'ביטול' וערכי הקסטרל יהיו רנדומליים.")
                .setCancelable(false)
                .setPositiveButton("הפעל מיקום", (dialog, id) -> {
                    activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                })
                .setNegativeButton("ביטול", (dialog, id) -> {
                    dialog.cancel();
                    setUiRandomValues();
                }).create().show();
    }

    private void checkNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnected())) {
            final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
            builder.setMessage("אנא הפעל את האינטרנט במכשירך על מנת שפיצ'ר זה יעבוד, במידה ואינך מעוניין, לחץ על 'ביטול' וערכי הקסטרל יהיו רנדומליים.")
                    .setCancelable(false)
                    .setPositiveButton("הפעל נתונים סלולריים", (dialog, id) -> {
                        activity.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                    }).setNegativeButton("הפעל Wi-Fi", (dialog, id) -> {
                activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            })
                    .setNeutralButton("ביטול", (dialog, id) -> {
                        dialog.cancel();
                        setUiRandomValues();
                    }).create().show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // locationAndInternetCheck();
                    if (!isRandomValues)
                        getLocationAndUpdateUI();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
