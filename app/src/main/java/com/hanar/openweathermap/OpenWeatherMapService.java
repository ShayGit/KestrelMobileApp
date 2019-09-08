
package com.hanar.openweathermap;
import com.hanar.kestrelmobileapp.AsyncTaskResult;


/**
 * The  OpenWeatherMapService which provides the weather information.
 *
 * @author Shay Cohen and Lior Krengel and Aviv Shrem.
 */


public class OpenWeatherMapService implements IWeatherDataService {
    private final String APP_ID = "7babd752067089ea9e326668a34536e1";
    private static OpenWeatherMapService serviceInstance = null;

    private OpenWeatherMapService() {
    }


    /**
     * Implemented as a singleton.
     * Gets or creates the service instance.
     *
     * @return the instance of OpenWeatherMapService
     */


    public static OpenWeatherMapService getServiceInstance() {
        if (serviceInstance == null) {
            serviceInstance = new OpenWeatherMapService();
        }

        return serviceInstance;
    }


    /**
     * Gets the weather data in a specific location.
     *
     * @param lon,lat - the location which contains city or country and city.
     * @return the WeatherData object which contains the information about the weather a the location
     * @throws WeatherDataServiceException - the service exception.
     */

    @Override
    public WeatherData getWeatherData(Double lon, Double lat) throws WeatherDataServiceException {

        WeatherData weatherData = null;
        String data;
        AsyncTaskResult<WeatherData> atr = null;

        try {
            data = WeatherHttpClient.getWeatherData(lon, lat);
            if (data != null) {
                weatherData = JSONWeatherParser.jsonWeatherParse(data);
            }
        } catch (WeatherDataServiceException e) {
            throw  e;
        }
        return weatherData;
    }

}


