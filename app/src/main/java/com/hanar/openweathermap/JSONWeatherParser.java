package com.hanar.openweathermap;

import org.json.JSONException;
import org.json.JSONObject;
import java.lang.Math;

import java.math.BigDecimal;
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
            weatherData.setDiscomfortIndex(getDiscomfortIndexByFormula(weatherData.getTemperature(),weatherData.getHumidity()));


        }catch (JSONException e)
        {
            throw new WeatherDataServiceException("בעיה בJSON",e);
        }
        return weatherData;
    }

    public static float getWindChillByFormula(float temperature,float windspeed)
    {
        float windspeedKMH = windspeed * 3600/1000;
        BigDecimal windChill = new BigDecimal(13.12+ (0.6215*temperature)-(11.37*Math.pow(windspeedKMH,0.16))+ (0.3965*temperature*Math.pow(windspeedKMH,0.16)));
        return windChill.floatValue();
    }
    public static float getDiscomfortIndexByFormula(float temperature,int humidity)
    {
        BigDecimal discomfortIndex = new BigDecimal(-2.39+ 0.785*temperature +(0.0222*humidity)+ (0.0024*temperature*humidity));
        return discomfortIndex.floatValue();
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
