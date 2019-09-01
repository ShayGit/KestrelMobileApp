
package com.hanar.openweathermap;


/**
 * A factory for creating WeatherDataService objects.
 * 
 *  @author Shay Cohen and Lior Krengel and Aviv Shrem.
 */

public class WeatherDataServiceFactory{


/**
	 * The Enum eServiceType.
	 */

	public enum eServiceType
	{		
		OpenWeatherMap
	}
	

/**
	 * Gets the weather data service.
	 *
	 * @param  serviceType (eServiceType)
     * @return the weather data service object(IWeatherDataService)
	 */

	public static IWeatherDataService getWeatherDataService(eServiceType serviceType)
	{
		if(serviceType == eServiceType.OpenWeatherMap)
		{
			return OpenWeatherMapService.getServiceInstance();
		}
		return null;
	}
}

