package nl.cyberworkz.roboflightmonitor.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightStatus {
	
	@JsonProperty("status")
	private String status;
	
	public FlightStatus() {
		// TODO Auto-generated constructor stub
	}
	
	public FlightStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
