package nl.cyberworkz.roboflightmonitor.domain;


public class Destination {
	
	private String iata;
	
	private String city;
	
	private String country;
	
	private String airportName_NL;
	
	private String airportName_EN;

	public String getAirportName_NL() {
		return airportName_NL;
	}

	public void setAirportName_NL(String airportName_NL) {
		this.airportName_NL = airportName_NL;
	}

	public String getAirportName_EN() {
		return airportName_EN;
	}

	public void setAirportName_EN(String airportName_EN) {
		this.airportName_EN = airportName_EN;
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	

	/**
	 * 
	 * Static factory method.
	 * 
	 * @param iata
	 * @param city
	 * @param country
	 * 
	 * @return a fresh {@link Destination}.
	 */
	public static Destination create(String iata, String city, String country, String airportName_NL, String airportName_EN) {
		Destination dest = new Destination();
		dest.setAirportName_EN(airportName_EN);
		dest.setAirportName_NL(airportName_NL);
		dest.setIata(iata);
		dest.setCity(city);
		dest.setCountry(country);
		
		if(city.equals("Berlin") || city.equals("London") || city.equals("Paris") || city.equals("New York") 
				|| city.equals("Moscow") || city.equals("Copenhagen") || city.equals("Milan") || city.equals("Frankfurt") 
				|| city.equals("Warsaw") || city.equals("Oslo") || city.equals("Barcelona")  || city.equals("Miami")) {
			dest.setCity(airportName_EN);
		}
		
		return dest;
	}

}
