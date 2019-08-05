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
	private String weatherState;
	private String weatherDescription;
	private String temperature; //Celsius
	private String humidity; //%
	private String minimumTemperature; //Celsius
	private String maximumTemperature; //Celsius
	private String windSpeed; //meter/sec
	private String windDegree; //degrees
	private String rainVolume;
	
	/**
	 * Constructor, initializing the fields containing the weather data.
	 */
	public WeatherData() {
		serverCity = new String();
		serverCountry = new String();
		weatherState = new String();
		weatherDescription = new String();
		temperature = new String();
		humidity = new String();
		minimumTemperature = new String();
		maximumTemperature = new String();
		windSpeed = new String();
		windDegree = new String();
		rainVolume = new String();
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
	 * Gets the weather state.
	 *
	 * @return the weather state (String)
	 */
	public String getWeatherState() {
		return weatherState;
	}
	
	/**
	 * Sets the weather state.
	 *
	 * @param weatherState the new weather state (String)
	 */
	public void setWeatherState(String weatherState) {
		this.weatherState = weatherState;
	}
	
	/**
	 * Gets the weather description.
	 *
	 * @return the weather description (String)
	 */
	public String getWeatherDescription() {
		return weatherDescription;
	}
	
	/**
	 * Sets the weather description.
	 *
	 * @param weatherDescription the new weather description (String)
	 */
	public void setWeatherDescription(String weatherDescription) {
		this.weatherDescription = weatherDescription;
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
	 * Gets the minimum temperature.
	 *
	 * @return the minimum temperature (String)
	 */
	public String getMinimumTemperature() {
		return minimumTemperature;
	}
	
	/**
	 * Sets the minimum temperature.
	 *
	 * @param minimumTemperature the new minimum temperature (String)
	 */
	public void setMinimumTemperature(String minimumTemperature) {
		this.minimumTemperature =  minimumTemperature + "�C";
	}
	
	/**
	 * Gets the maximum temperature.
	 *
	 * @return the maximum temperature (String)
	 */
	public String getMaximumTemperature() {
		return maximumTemperature;
	}
	
	/**
	 * Sets the maximum temperature.
	 *
	 * @param  maximum temperature (String)
	 */
	public void setMaximumTemperature(String maximumTemperature) {
		this.maximumTemperature = maximumTemperature+"�C";
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
	 * @param the wind speed (String)
	 */
	public void setWindSpeed(String windSpeed) {
		float windSpeedKmtOHour;
		windSpeedKmtOHour = Float.parseFloat(windSpeed);
		windSpeedKmtOHour *= 3.6;
		this.windSpeed = String.valueOf(windSpeedKmtOHour)+ "km/h";
	}
	
	/**
	 * Gets the wind degree.
	 *
	 * @return the wind degree (String)
	 */
	public String getWindDegree() {
		return windDegree;
	}
	
	/**
	 * Sets the wind degree.
	 *
	 * @param the wind degree (String)
	 */
	public void setWindDegree(String windDegree) {
		this.windDegree = windDegree;
	}
	
	/**
	 * Gets the rain volume.
	 *
	 * @return the rain volume (String)
	 */
	public String getRainVolume() {
		return rainVolume;
	}
	
	/**
	 * Sets the rain volume.
	 *
	 * @param the rain volume (String)
	 */
	public void setRainVolume(String rainVolume) {
		this.rainVolume = rainVolume;
	}
	
	/**
	 * Overriding hashCode method for WeatherData object
	 *
	 * @return the rain volume (String)
	 */
    @Override
    public int hashCode() {
        return Objects.hash(serverCity, serverCountry, weatherState, weatherDescription, temperature, humidity, minimumTemperature,
               maximumTemperature, windSpeed, windDegree, rainVolume);
    }
    
	/**
	 * Overriding toString method for WeatherData object
	 *
	 * @return the String (String)
	 */
	@Override
	public String toString() {
		return "The weather at " + serverCity + ", " + serverCountry + " weatherState=" + weatherState + ", weatherDescription=" + weatherDescription
				+ ", temperature=" + temperature + " humidity="
				+ humidity + ", minimumTemperature=" + minimumTemperature + ", maximumTemperature=" + maximumTemperature
				+ ", windSpeed=" + windSpeed + ", windDegree=" + windDegree + ", rainVolume=" + rainVolume;
	}
	
	/**
	 * Overriding equals method for WeatherData object
	 *
	 * @return the objects equal or not (boolean)
	 * @param the rain volume (String)
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
		
		if(this.maximumTemperature == null)
		{	
			if(otherWeatherData.maximumTemperature != null)
				return false;
		}
		else if(!this.maximumTemperature.equals(otherWeatherData.maximumTemperature))
			return false;
		
		if(this.minimumTemperature == null)
		{	
			if(otherWeatherData.minimumTemperature != null)
				return false;
		}
		else if(!this.minimumTemperature.equals(otherWeatherData.minimumTemperature))
			return false;
		
		if(this.rainVolume == null)
		{	
			if(otherWeatherData.rainVolume != null)
				return false;
		}
		else if(!this.rainVolume.equals(otherWeatherData.rainVolume))
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
		
		if(this.weatherDescription == null)
		{	
			if(otherWeatherData.weatherDescription != null)
				return false;
		}
		else if(!this.weatherDescription.equals(otherWeatherData.weatherDescription))
			return false;
		
		if(this.weatherState == null)
		{	
			if(otherWeatherData.weatherState != null)
				return false;
		}
		else if(!this.weatherState.equals(otherWeatherData.weatherState))
			return false;
		
		if(this.windDegree == null)
		{	
			if(otherWeatherData.windDegree != null)
				return false;
		}
		else if(!this.windDegree.equals(otherWeatherData.windDegree))
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
