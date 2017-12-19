/**
 * 
 */
package nl.cyberworkz.roboflightmonitor.domain;

import java.util.List;

/**
 * Response from Schiphol API.
 * 
 * @author haiko
 *
 */
public class SchipholFlightsResponse {
	
	private List<Flight> flights;
	
	private String version;

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	

}
