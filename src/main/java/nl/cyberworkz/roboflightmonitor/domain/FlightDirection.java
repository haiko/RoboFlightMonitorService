package nl.cyberworkz.roboflightmonitor.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public enum FlightDirection {
	
	
	ARRIVING("A"), DEPARTING("D");
	
	private static final String FLIGHT_DIRECTION = "flightDirection";
	
	private String direction;
	
	FlightDirection(String direction){
		this.direction = direction;
	}
	
	@JsonCreator
	public static FlightDirection fromNode(JsonNode node) {
		if(!node.has(FLIGHT_DIRECTION)) {
			return null;
		}
		String direction = node.get(FLIGHT_DIRECTION).asText();
		
		return FlightDirection.valueOf(direction);
	}

	@JsonProperty
	public String getDirection() {
		return direction;
	}

}
