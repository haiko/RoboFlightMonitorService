package nl.cyberworkz.roboflightmonitor.domain;


public class Destination {
	
	private String iata;
	
	private String city;
	
	private String country;

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
	public static Destination create(String iata, String city, String country) {
		Destination dest = new Destination();
		
		dest.setIata(iata);
		dest.setCity(city);
		dest.setCountry(country);
		
		return dest;
	}

}
