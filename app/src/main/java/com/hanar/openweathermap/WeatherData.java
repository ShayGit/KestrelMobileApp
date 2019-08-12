package com.hanar.openweathermap;

import java.util.Objects;

/**
 * The Class WeatherData represent the weather details.
 *
 * @author Shay Cohen and Lior Krengel and Aviv Shrem.
 */
public class WeatherData {


    private float temperature; //Celsius
    private int humidity; //%
    private float windSpeed; //meter/sec
    private  double windChill; //Celsius
    private float discomfortIndex; //Celsius

    /**
     * Constructor, initializing the fields containing the weather data.
     */
    public WeatherData() {
    }

    /**
     * Gets the temperature.
     *
     * @return the temperature (Float)
     */
    public float getTemperature() {
        return temperature;
    }

    /**
     * Sets the temperature.
     *
     * @param temperature the new temperature (Float)
     */
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    /**
     * Gets the humidity.
     *
     * @return the humidity (int)
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     * Sets the humidity.
     *
     * @param humidity the new humidity (int)
     */
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    /**
     * Gets the wind speed.
     *
     * @return the wind speed (Float)
     */
    public float getWindSpeed() {
        return windSpeed;
    }

    /**
     * Sets the wind speed.
     *
     * @param windSpeed (Float)
     */
    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * Gets the wind chill.
     *
     * @return the wind chill (Float)
     */
    public double getWindChill() {
        return windChill;
    }

    /**
     * Sets the wind chil.
     *
     * @param windChill (Float)
     */
    public void setWindChill(double windChill) {
        this.windChill = windChill;
    }

    /**
     * Gets the wind chill.
     *
     * @return the discomfortIndex (Float)
     */
    public float getDiscomfortIndex() {
        return discomfortIndex;
    }

    /**
     * Sets the wind chil.
     *
     * @param discomfortIndex (Float)
     */
    public void setDiscomfortIndex(float discomfortIndex) {
        this.discomfortIndex = discomfortIndex;
    }




    /**
     * Overriding hashCode method for WeatherData object
     *
     * @return the rain volume (String)
     */
    @Override
    public int hashCode() {
        return Objects.hash(temperature, humidity, windSpeed);
    }

    /**
     * Overriding toString method for WeatherData object
     *
     * @return the String (String)
     */
    @Override
    public String toString() {
        return " Temperature=" + temperature + " humidity="
                + humidity + ", windSpeed=" + windSpeed+", windChill="+windChill+", discomfortIndex="+discomfortIndex;
    }

    /**
     * Overriding equals method for WeatherData object
     *
     * @param obj the other WeatherData object (Object)
     * @return the objects equal or not (boolean)
     */
    @Override
    public boolean equals(Object obj) {
        WeatherData otherWeatherData = (WeatherData) obj;

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        if (this.humidity == 0) {
            if (otherWeatherData.humidity != 0)
                return false;
        } else if (!Integer.toString(this.humidity).equals(Integer.toString(otherWeatherData.humidity)))
            return false;

        if (this.temperature == 0.0) {
            if (otherWeatherData.temperature != 0.0)
                return false;
        } else if (!Float.toString(this.temperature).equals(Float.toString(otherWeatherData.temperature)))
            return false;

        if (this.windSpeed == 0.0) {
            if (otherWeatherData.windSpeed != 0.0)
                return false;
        } else if (!Float.toString(this.windSpeed).equals(Float.toString(otherWeatherData.windSpeed))) {
            return false;
        }

        return true;
    }
}
