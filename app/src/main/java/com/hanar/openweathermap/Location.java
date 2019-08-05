package com.hanar.openweathermap;

/**
 * The Class Location describing location by city name or city and country.
 * 
 *  @author Shay Cohen and Lior Krengel and Aviv Shrem.
 */
public class Location {


	private String city;
	private String country;

	/**
	 * Constructor, initializing the city and country fields of the location object.
	 * 
	 */
	public Location() {
		city = new String();
		country = new String();
	}
	
	/**
	 * Location constructor with only city. 
	 * 
	 * @param city (String).
	 */
	public Location(String city) {
		super();
		this.city = city;
	}
	/**
	 * Location constructor with city and country.
	 * 
	 * @param city (String).
	 * @param country (String).
	 */
	public Location(String city, String country) {
		super();
		this.city = city;
		this.country = country;
	}
	
	/**
	 * Gets the city name.
	 * 
	 * @return the city (String)
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * Sets the city name.
	 *
	 * @param the city (String)
	 */
	public void setCity(String city) {
		
		this.city = city.replaceAll("\\s+","");;
	}
	
	/**
	 * Gets the country name.
	 *
	 * @return the country (String)
	 */
	public String getCountry() {
		return country;
	}
	
	/**
	 * Sets the country name.
	 *
	 * @param country (String)
	 */
	public void setCountry(String country) {
		this.country = country;
	}

}
