package nl.cyberworkz.roboflightmonitor.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FlightDirection {
	
	@JsonProperty("A")
	ARRIVING("A"),
	@JsonProperty("D")
	DEPARTING("D");
	
	private static final String FLIGHT_DIRECTION = "flightDirection";
	
	private String direction;
	
	FlightDirection(String direction){
		this.direction = direction;
	}

	@JsonProperty
	public String getDirection() {
		return direction;
	}

}
