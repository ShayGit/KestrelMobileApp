package com.hanar.openweathermap;

import org.json.JSONException;
import org.json.JSONObject;
import java.lang.Math;

import java.net.URL;

public class JSONWeatherParser {
    /**
     * Gets the weather data by parsing the json data from the url.
     *
     * @param data - the site address which contains weather data in json
     * @return the WeatherData object which contains the information about the weather in the location
     * @throws WeatherDataServiceException - the service exception.
     */


    public static WeatherData jsonWeatherParse(String data) throws WeatherDataServiceException {
        WeatherData weatherData = null;
        try {
            // We start extracting the info
            weatherData = new WeatherData();
            JSONObject jObj = new JSONObject(data);
            JSONObject mainObj = getObject("main", jObj);
            weatherData.setTemperature(getFloat("temp", mainObj));
            weatherData.setHumidity(getInt("humidity", mainObj));

            JSONObject windObj = getObject("wind", jObj);
            weatherData.setWindSpeed(getFloat("speed", windObj));
            if(weatherData.getTemperature()<=10 && weatherData.getWindSpeed()>1.3)
            {
                weatherData.setWindChill(getWindChillByFormula(weatherData.getTemperature(),weatherData.getWindSpeed()));
            }
        }catch (JSONException e)
        {
            throw new WeatherDataServiceException("בעיה בJSON",e);
        }
        return weatherData;
    }

    private static double getWindChillByFormula(float temperature,float windspeed)
    {
        double windspeedKMH = windspeed * 3600/1000;
        double windChill = 13.12+ (0.6215*temperature)-(11.37*Math.pow(windspeedKMH,0.16))+ (0.3965*temperature*Math.pow(windspeedKMH,0.16));
        return windChill;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }


    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

}
