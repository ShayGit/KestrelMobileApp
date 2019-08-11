
package com.hanar.openweathermap;


import androidx.appcompat.app.AppCompatActivity;

/**
 * The Interface IWeatherDataService.
 * An Interface for the developers.
 * Contains methods for weather analyzing.
 *
 *  @author Shay Cohen and Lior Krengel and Aviv Shrem.
 */

public interface IWeatherDataService {


/**
	 * Gets the weather data.
	 *
	 * @param  lon,lat (Location)
	 * @return WeatherData which includes the weather information.
	 * @throws WeatherDataServiceException
	 */

	public abstract AsyncTaskResult getWeatherData(Double lon, Double lat)throws WeatherDataServiceException;
}

