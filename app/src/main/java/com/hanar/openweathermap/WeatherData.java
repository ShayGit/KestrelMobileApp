package com.hanar.openweathermap;

import java.util.Objects;

/**
 * The Class WeatherData represent the weather details.
 * 
 *  @author Shay Cohen and Lior Krengel and Aviv Shrem.
 */
public class WeatherData {
	
	private String serverCity;
	private String serverCountry;
	private String temperature; //Celsius
	private String humidity; //%
	private String windSpeed; //meter/sec
	
	/**
	 * Constructor, initializing the fields containing the weather data.
	 */
	public WeatherData() {
		serverCity = new String();
		serverCountry = new String();
		temperature = new String();
		humidity = new String();
		windSpeed = new String();
	}
	
	/**
	 * Gets the server city.
	 *
	 * @return the server city  (String)
	 */
	public String getServerCity() {
		return serverCity;
	}

	/**
	 * Sets the server city to .
	 *
	 * @param serverCity the new server city (String)
	 */
	public void setServerCity(String serverCity) {
		this.serverCity = serverCity;
	}

	/**
	 * Gets the server country.
	 *
	 * @return the server country (String)
	 */
	public String getServerCountry() {
		return serverCountry;
	}

	/**
	 * Sets the server country.
	 *
	 * @param serverCountry the new server country (String)
	 */
	public void setServerCountry(String serverCountry) {
		this.serverCountry = serverCountry;
	}

	
	/**
	 * Gets the temperature.
	 *
	 * @return the temperature (String)
	 */
	public String getTemperature() {
		return temperature;
	}
	
	/**
	 * Sets the temperature.
	 *
	 * @param temperature the new temperature (String)
	 */
	public void setTemperature(String temperature) {
		this.temperature = temperature + "�C";
	}

	/**
	 * Gets the humidity.
	 *
	 * @return the humidity (String)
	 */
	public String getHumidity() {
		return humidity;
	}
	
	/**
	 * Sets the humidity.
	 *
	 * @param humidity the new humidity (String)
	 */
	public void setHumidity(String humidity) {
		this.humidity = humidity +"%";
	}
	
	/**
	 * Gets the wind speed.
	 *
	 * @return the wind speed (String)
	 */
	public String getWindSpeed() {
		return windSpeed;
	}
	
	/**
	 * Sets the wind speed.
	 *
	 * @param  windSpeed (String)
	 */
	public void setWindSpeed(String windSpeed) {
		float windSpeedKmtOHour;
		windSpeedKmtOHour = Float.parseFloat(windSpeed);
		windSpeedKmtOHour *= 3.6;
		this.windSpeed = String.valueOf(windSpeedKmtOHour)+ "km/h";
	}

	
	/**
	 * Overriding hashCode method for WeatherData object
	 *
	 * @return the rain volume (String)
	 */
    @Override
    public int hashCode() {
        return Objects.hash(serverCity, serverCountry, temperature, humidity, windSpeed);
    }
    
	/**
	 * Overriding toString method for WeatherData object
	 *
	 * @return the String (String)
	 */
	@Override
	public String toString() {
		return "The weather at " + serverCity + ", " + serverCountry + ", temperature=" + temperature + " humidity="
				+ humidity + ", maximumTemperature="+ ", windSpeed=" + windSpeed;
	}
	
	/**
	 * Overriding equals method for WeatherData object
	 *
	 * @return the objects equal or not (boolean)
	 * @param obj the other WeatherData object (Object)
	 */
	@Override 
	public boolean equals(Object obj)
	{
		WeatherData otherWeatherData = (WeatherData)obj;
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;		
		
		if(this.humidity == null)
		{	
			if(otherWeatherData.humidity != null)
				return false;
		}
		else if(!this.humidity.equals(otherWeatherData.humidity))
			return false;
		
		if(this.serverCity == null)
		{	
			if(otherWeatherData.serverCity != null)
				return false;
		}
		else if(!this.serverCity.equals(otherWeatherData.serverCity))
			return false;
		
		if(this.serverCountry == null)
		{	
			if(otherWeatherData.serverCountry != null)
				return false;
		}
		else if(!this.serverCountry.equals(otherWeatherData.serverCountry))
			return false;
		
		if(this.temperature == null)
		{	
			if(otherWeatherData.temperature != null)
				return false;
		}
		else if(!this.temperature.equals(otherWeatherData.temperature))
			return false;

		if(this.windSpeed == null)
		{	
			if(otherWeatherData.windSpeed != null)
				return false;
		}
		else if(!this.windSpeed.equals(otherWeatherData.windSpeed))
			return false;
		
		return true;			
	}
}
