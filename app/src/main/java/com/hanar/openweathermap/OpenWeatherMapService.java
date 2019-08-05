package com.hanar.openweathermap;

import android.util.JsonReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import org.json.*;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.*;

/**
 * The  OpenWeatherMapService which provides the weather information.
 * 
 * @author Shay Cohen and Lior Krengel and Aviv Shrem.
 */

public class OpenWeatherMapService implements IWeatherDataService{
	private final String APP_ID = "7babd752067089ea9e326668a34536e1";
	private static OpenWeatherMapService serviceInstance = null;
	
	private OpenWeatherMapService() { }

	/**
	 * Implemented as a singleton.
	 * Gets or creates the service instance.
	 *
	 * @return the instance of OpenWeatherMapService
	 */

	public static OpenWeatherMapService getServiceInstance()      
    {
        if (serviceInstance == null)
        {
        	serviceInstance = new OpenWeatherMapService();
        }

        return serviceInstance;
    }

	/**
	 * Gets the weather data in a specific location.
	 *
	 * @param location - the location which contains city or country and city.
	 * @return the WeatherData object which contains the information about the weather a the location
	 * @throws WeatherDataServiceException - the service exception.
	 */

	@Override
	public WeatherData getWeatherData(Location location) throws WeatherDataServiceException
	{

		URL url;

			try {
				if(location.getCountry() == null)
				{
					url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+location.getCity()+"&APPID="+APP_ID+"&units=metric");		

				}
				else
				{
					url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+location.getCity()+","+location.getCountry()+"&APPID="+APP_ID+"&units=metric");
				}
			} catch (MalformedURLException e) {
				throw new WeatherDataServiceException("Url is not valid",e);
			}

		//return jsonWeatherParse(url);
		return new WeatherData();
	}


	/**
	 * Gets the weather data by parsing the json data from the url.
	 *
	 * @param url - the site address which contains weather data in json
	 * @return the WeatherData object which contains the information about the weather in the location
	 * @throws WeatherDataServiceException - the service exception.
	 */
	/*
	private static WeatherData jsonWeatherParse(URL url) throws WeatherDataServiceException
	{
		JSONArray ja;
		JSONObject main;
		JSONObject wind;
		JSONObject rain;
		JSONObject sys;
		WeatherData weatherData = new WeatherData();
		String cityServerName, countryServerName, weatherState, weatherDescription, temperature;
		String  humidity, minimumTemperature, maximumTemperature, windSpeed, windDegree, rainVolume;
		InputStreamReader inr;
		JsonReader rdr;
		JSONObject jo;
		
		try {
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			rdr = new JsonReader(in);
		//	jo = rdr.nextString();
			in.close();

			ja = jo.getJSONArray("weather");
			weatherState = ja.getJSONObject(0).getString("main");
			weatherData.setWeatherState(weatherState);
			weatherDescription = ja.getJSONObject(0).getString("description").toString();
			weatherData.setWeatherDescription(weatherDescription);
			main = 	jo.getJSONObject("main");
			temperature = main.get("temp").toString();
			weatherData.setTemperature(temperature);
			humidity = main.get("humidity").toString();
			weatherData.setHumidity(humidity);
			minimumTemperature = main.get("temp_min").toString();
			weatherData.setMinimumTemperature(minimumTemperature);
			maximumTemperature = main.get("temp_max").toString();
			weatherData.setMaximumTemperature(maximumTemperature);
			wind = jo.getJSONObject("wind");
			windSpeed = wind.get("speed").toString();
			weatherData.setWindSpeed(windSpeed);
			if(wind.has("deg"))
			{
			windDegree = wind.get("deg").toString();
			weatherData.setWindDegree(windDegree);
			}
			if(jo.has("rain"))
			{
				rain = jo.getJSONObject("rain");
				rainVolume = rain.get("3h").toString();
				weatherData.setRainVolume(rainVolume);
			}
			else
			{
				weatherData.setRainVolume("0");
			}
			sys = jo.getJSONObject("sys");		
			cityServerName = sys.getString("country");
			weatherData.setServerCountry(cityServerName);		
			countryServerName = jo.getString("name");
			weatherData.setServerCity(countryServerName);
		} catch (ProtocolException e) {
			throw new WeatherDataServiceException(e.getMessage(),e);
		}
		 catch (IOException e) {
			throw new WeatherDataServiceException("IO Error or a bad location input",e);	
		}
		 catch (ClassCastException e) {
			throw new WeatherDataServiceException("Information Parsing Error",e);
		}
		catch (NullPointerException e) {
			throw new WeatherDataServiceException("Information Parsing Error",e);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return weatherData;
	}
	*/

}


