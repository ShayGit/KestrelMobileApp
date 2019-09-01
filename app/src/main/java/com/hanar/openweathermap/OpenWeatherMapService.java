
package com.hanar.openweathermap;
import java.util.concurrent.ExecutionException;



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
    public AsyncTaskResult<WeatherData> getWeatherData(Double lon, Double lat) throws WeatherDataServiceException {
        JSONWeatherTask task = new JSONWeatherTask();
        AsyncTaskResult<WeatherData> atr = null;
        try {
             atr = task.execute(lon, lat).get();

        } catch (ExecutionException e) {
            throw new WeatherDataServiceException("Thread execution problem",e);
        } catch (InterruptedException e) {
            throw new WeatherDataServiceException("Thread execution problem",e);
        }

        return atr;
    }
}


