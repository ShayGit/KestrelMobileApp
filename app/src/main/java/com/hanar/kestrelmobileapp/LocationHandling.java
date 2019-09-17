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
import android.view.MenuItem;
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
import com.hanar.openweathermap.IWeatherDataService;
import com.hanar.openweathermap.JSONWeatherParser;
import com.hanar.openweathermap.WeatherData;
import com.hanar.openweathermap.WeatherDataServiceFactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Random;

class LocationHandling implements WeatherTask.Callback {
    private FusedLocationProviderClient fusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Double longtitude = 0.0, latitude = 0.0;
    private ProgressBar pB;


    private boolean isRandomValues;
    private AppCompatActivity activity;
    private IWeatherDataService weatherService;
    private WeatherData weatherData;
    onUpdateUIListener uiListener;
    private boolean isLocationOrNetworkDisabled;
    private MenuItem locationSettingItem;
    private WeatherTask weatherTask = null;
    private boolean isPowerOn;


    interface onUpdateUIListener {
        void onUpdateUI(WeatherData wd);
    }

    LocationHandling(AppCompatActivity activityChain) {
        activity = activityChain;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        isRandomValues = false;
        pB = activity.findViewById(R.id.pb);
        weatherService = WeatherDataServiceFactory.getWeatherDataService(WeatherDataServiceFactory.eServiceType.OpenWeatherMap);
        weatherData = null;
        isLocationOrNetworkDisabled = false;
    }

    void locationRequestOnKestrelStart() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (!isRandomValues) {

                new MaterialAlertDialogBuilder(activity).
                        setCancelable(false)
                        .setMessage("האפליקציה מעוניינת להשתמש במיקומך האחרון על מנת שהקסטרל יציג נתונים על פי מיקומך, במידה ואינך מעוניינ/ת בכך, הקסטרל יציג נתונים אקראיים")
                        .setPositiveButton("הבנתי", (dialog, which) -> ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET},
                                MY_PERMISSIONS_REQUEST_LOCATION)).setNegativeButton("לא מעוניינ/ת", (dialog, which) -> setUiRandomValues()).create()
                        .show();

            } else {
                setUiRandomValues();
            }
        } else {
            if (isRandomValues) {
                setUiRandomValues();
            } else {
                getLocationAndUpdateUI();
            }
        }
    }

    private void setUiRandomValues() {
        isRandomValues = true;
        locationSettingItem.setChecked(false);
        Random rn = new Random();
        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature(rn.nextFloat() * 60 - 10);
        weatherData.setWindSpeed(rn.nextFloat() * 30);
        weatherData.setHumidity(rn.nextInt(96) + 5);
        if (weatherData.getTemperature() <= 10 && weatherData.getWindSpeed() > 1.3) {
            weatherData.setWindChill(JSONWeatherParser.getWindChillByFormula(weatherData.getTemperature(), weatherData.getWindSpeed()));
        } else {
            weatherData.setWindChill(weatherData.getTemperature());
        }
        weatherData.setDiscomfortIndex(JSONWeatherParser.getDiscomfortIndexByFormula(weatherData.getTemperature(), weatherData.getHumidity()));
        uiListener.onUpdateUI(weatherData);

    }

    @SuppressLint("MissingPermission")
    private void getLocationAndUpdateUI() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            boolean uiUpdated = false;

            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    setProgressBar(true);
                    locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationRequest.setInterval(5 * 1000);
                    locationRequest.setNumUpdates(1);
                    locationRequest.setMaxWaitTime(20 * 1000);
                    locationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            if (locationResult == null) {
                                if (fusedLocationClient != null && uiUpdated) {
                                    fusedLocationClient.removeLocationUpdates(this);
                                }
                                return;
                            }
                            for (Location location : locationResult.getLocations()) {
                                if (location != null) {
                                    if (!uiUpdated) {
                                        weatherTask = new WeatherTask(weatherService, LocationHandling.this::postWeatherRetrieval);
                                        weatherTask.execute(location.getLongitude(), location.getLatitude());
                                        uiUpdated = true;
                                    }
                                    break;
                                }
                            }
                            if (fusedLocationClient != null && uiUpdated) {
                                fusedLocationClient.removeLocationUpdates(this);
                            }
                        }

                        @Override
                        public void onLocationAvailability(LocationAvailability locationAvailability) {
                            if (!locationAvailability.isLocationAvailable()) {
                                setProgressBar(false);
                                if (!uiUpdated) {
                                    if (longtitude == 0.0 && latitude == 0.0) {
                                        isLocationEnabled();
                                    } else {
                                        weatherTask = new WeatherTask(weatherService, LocationHandling.this::postWeatherRetrieval);
                                        weatherTask.execute(longtitude, latitude);

                                    }
                                    uiUpdated = true;
                                }
                            }
                        }
                    };
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                } else {
                    setProgressBar(true);
                    weatherTask = new WeatherTask(weatherService, LocationHandling.this::postWeatherRetrieval);
                    weatherTask.execute(location.getLongitude(), location.getLatitude());
                }
            }
        }).addOnFailureListener(activity, new OnFailureListener() {
            boolean uiUpdated = false;

            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    // try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    if (!uiUpdated) {
                        if (longtitude == 0.0 && latitude == 0.0) {
                            // ResolvableApiException resolvable = (ResolvableApiException) e;
                            // resolvable.startResolutionForResult(MainActivity.this,
                            //       REQUEST_CHECK_SETTINGS);
                            isLocationEnabled();
                        } else {

                            weatherTask = new WeatherTask(weatherService, LocationHandling.this::postWeatherRetrieval);
                            weatherTask.execute(longtitude, latitude);
                            //  retrieveWeatherByLocation(longtitude, latitude);
                            //updateKestrelUI();
                        }
                        uiUpdated = true;
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
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isLocationOrNetworkDisabled = true;
            buildAlertMessageNoGps();
        } else {
            isLocationOrNetworkDisabled = false;
            if (checkNetworkConnected()) {
                isLocationOrNetworkDisabled = false;
                if (isPowerOn) {
                    final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
                    builder.setMessage("נראה שהמיקום מופעל אך בכל זאת יש בעיה כלשהי, נאלץ לבטל את פיצ'ר זה ויוצגו ערכים אקראיים, לנסיון נוסף הפעל את האפליקציה מחדש.")
                            .setCancelable(false)
                            .setPositiveButton("הבנתי", (dialog, id) -> {
                                setUiRandomValues();
                                dialog.dismiss();
                            }).create().show();
                }
            } else {
                isLocationOrNetworkDisabled = true;
            }
        }
    }

    @Override
    public void postWeatherRetrieval(AsyncTaskResult<WeatherData> atr) {
        if (atr == null) {
            Toast.makeText(activity, "שגיאה בקבלת נתונים מהשרת, נסה שנית או בטל נתונים על פי מיקום.", Toast.LENGTH_SHORT).show();

        } else if (atr.getError() != null) {
            Toast.makeText(activity, atr.getError().getMessage(), Toast.LENGTH_SHORT).show();
            if (weatherData == null) {
                checkNetworkConnected();
            }
            atr.getError().printStackTrace();
        } else if (atr.getResult() != null) {
            weatherData = atr.getResult();
        }
        updateKestrelUI();
    }
   /* private void retrieveWeatherByLocation(Double i_longtitude, Double i_latitude) {
        longtitude = i_longtitude;
        latitude = i_latitude;
        AsyncTaskResult<WeatherData> atr = null;
        try {
            atr = weatherService.getWeatherData(longtitude, latitude);

            if (atr == null) {
                Toast.makeText(activity, "תוצאת התהליך לקבלת מידע מזג האוויר null", Toast.LENGTH_SHORT).show();

            } else if (atr.getError() != null) {
                Toast.makeText(activity, atr.getError().getMessage(), Toast.LENGTH_SHORT).show();
                if (weatherData == null) {
                    checkNetworkConnected();
                }
                atr.getError().printStackTrace();
            } else if (atr.getResult() != null) {
                weatherData = atr.getResult();
            }
        } catch (WeatherDataServiceException e) {
            Toast.makeText(activity, atr.getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/

    private void updateKestrelUI() {
// invoke kestrelLogicListener
        setProgressBar(false);
        if (uiListener != null) {
            uiListener.onUpdateUI(weatherData);
        }
    }


    private void buildAlertMessageNoGps() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        builder.setMessage("אנא הפעל את המיקום במכשירך על מנת שפיצ'ר זה יעבוד, במידה ואינך מעוניין, לחץ על 'ביטול' וערכי הקסטרל יהיו רנדומליים.")
                .setCancelable(false)
                .setPositiveButton("הפעל מיקום", (dialog, id) -> {
                    activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    dialog.dismiss();
                    updateKestrelUI();
                })
                .setNegativeButton("ביטול", (dialog, id) -> {
                    dialog.cancel();
                    setUiRandomValues();
                }).create().show();
    }

    private boolean checkNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected = false;
        connected = (activeNetworkInfo != null && activeNetworkInfo.isConnected());
        if (!connected) {
            setProgressBar(false);
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
            builder.setMessage("אנא הפעל את האינטרנט במכשירך על מנת שפיצ'ר זה יעבוד, במידה ואינך מעוניין, לחץ על 'ביטול' וערכי הקסטרל יהיו רנדומליים.")
                    .setCancelable(false)
                    .setPositiveButton("הפעל נתונים סלולריים", (dialog, id) -> {
                        dialog.dismiss();
                        activity.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                        updateKestrelUI();
                    }).setNegativeButton("הפעל Wi-Fi", (dialog, id) -> {
                dialog.dismiss();
                activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                updateKestrelUI();
            })
                    .setNeutralButton("ביטול", (dialog, id) -> {
                        dialog.dismiss();
                        setUiRandomValues();
                    }).create().show();

        }
        return connected;
    }

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET)) {
                        //denied
                        //Log.e("denied", permission);
                        new MaterialAlertDialogBuilder(activity).
                                setCancelable(false)
                                .setMessage(" בטוח/ה? שימוש במיקום מאפשר קבלת נתונים רלוונטים למיקומך, אחרת יתקבלו ערכים אקראיים.")
                                .setPositiveButton("אוקיי, מעוניין", (dialog, which) -> ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET},
                                        MY_PERMISSIONS_REQUEST_LOCATION)).setNegativeButton("לא מעוניינ/ת", (dialog, which) -> setUiRandomValues()).create()
                                .show();

                    } else {
                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                            locationRequestOnKestrelStart();
                        } else {
                            setUiRandomValues();
                        }
                    }
                    //Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean isDeniedPermissions()
    {
        boolean is = false;
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET)) {

        } else {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            } else {
                is = true;
            }
        }
        return is;
    }

    public void setIsRandomValues(boolean i_isRandomValues) {
        isRandomValues = i_isRandomValues;
    }

    public boolean getIsRandomValues() {
        return isRandomValues;
    }

    boolean getIsLocationOrNetworkDisabled() {
        return this.isLocationOrNetworkDisabled;
    }

    public void setProgressBar(boolean isVisible) {
        if (isVisible)
            pB.setVisibility(View.VISIBLE);
        else
            pB.setVisibility(View.INVISIBLE);

    }

    public ProgressBar getProgressBar() {
        return this.pB;

    }

    public void setLocationSettingItem(MenuItem locationSettingItem) {
        this.locationSettingItem = locationSettingItem;
    }

    WeatherTask getWeatherTask() {
        return this.weatherTask;
    }

    void setIsPowerOn(boolean ispo) {
        this.isPowerOn = ispo;
    }
}
