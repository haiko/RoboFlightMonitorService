package nl.cyberworkz.roboflightmonitor.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightState {
	
	@JsonProperty("flightStates")
	private List<String> states;

	public List<String> getStates() {
		return states;
	}

	public void setStates(List<String> states) {
		this.states = states;
	}
}
