package com.hanar.openweathermap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WeatherHttpClient {
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String APP_ID = "7babd752067089ea9e326668a34536e1";

    public static String getWeatherData(Double lon, Double lat) throws WeatherDataServiceException {
        HttpURLConnection con = null;
        InputStream is = null;
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&APPID=" + APP_ID + "&units=metric");
            con = (HttpURLConnection) (url).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null)
                buffer.append(line + "rn");

            return buffer.toString();

        } catch (MalformedURLException e) {
            throw new WeatherDataServiceException("שגיאה בכתובת שרת מזג האוויר", e);
        } catch (ProtocolException e) {
            throw new WeatherDataServiceException(e.getMessage(), e);
        } catch (IOException e) {
            throw new WeatherDataServiceException("שגיאה בקלט או פלט נתוני מזג האוויר מהשרת", e);
        } catch (ClassCastException e) {
           throw new WeatherDataServiceException("שגיאה בהמרת המידע", e);
        } catch (NullPointerException e) {
            throw new WeatherDataServiceException("שגיאה בהמרת המידע", e);
        } finally {
            try {
                is.close();
                con.disconnect();
            } catch (Throwable t) {
            }
        }
    }
}
