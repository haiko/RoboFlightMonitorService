package nl.cyberworkz.roboflightmonitor.domain;

import java.util.List;

/**
 * @author haiko
 *
 */
public class FlightResponse {
	
	private List<Flight> arrivingFlights;
	
	private String nextLink;
	
	private String previousLink;

	public List<Flight> getArrivingFlights() {
		return arrivingFlights;
	}

	public void setArrivingFlights(List<Flight> arrivingFlights) {
		this.arrivingFlights = arrivingFlights;
	}

	public String getNextLink() {
		return nextLink;
	}

	public void setNextLink(String nextLink) {
		this.nextLink = nextLink;
	}

	public String getPreviousLink() {
		return previousLink;
	}

	public void setPreviousLink(String previousLink) {
		this.previousLink = previousLink;
	}

}
