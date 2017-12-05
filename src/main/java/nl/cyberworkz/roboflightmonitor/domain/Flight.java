package nl.cyberworkz.roboflightmonitor.domain;

import org.joda.time.DateTime;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a current Flight from/ to airport.
 * 
 * @author haiko
 *
 */
public class Flight extends ResourceSupport{

	@JsonProperty("id")
	private Long flightId;
	
	private String flightName;

	
	private FlightDirection flightDirection;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private DateTime scheduleDate;

	@JsonFormat(pattern = "HH:mm:ss")
	private DateTime scheduleTime;

	private String flightNumber;

	@JsonProperty(value="prefixIATA")
	private String iataCode;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private DateTime estimatedLandingTime;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private DateTime actualLandingTime;
	
	private String terminal;
	
	private String gate;
	
	@JsonProperty("publicFlightState")
	private FlightState flightState;
	

	public FlightDirection getFlightDirection() {
		return flightDirection;
	}

	public void setFlightDirection(FlightDirection flightDirection) {
		this.flightDirection = flightDirection;
	}


	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getIataCode() {
		return iataCode;
	}

	public void setIataCode(String iataCode) {
		this.iataCode = iataCode;
	}

	public DateTime getScheduledDate() {
		return scheduleDate;
	}

	public void setScheduledDate(DateTime scheduledDate) {
		this.scheduleDate = scheduledDate;
	}

	public DateTime getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(DateTime scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public DateTime getEstimatedLandingTime() {
		return estimatedLandingTime;
	}

	public void setEstimatedLandingTime(DateTime estimatedLandingTime) {
		this.estimatedLandingTime = estimatedLandingTime;
	}

	public DateTime getActualLandingTime() {
		return actualLandingTime;
	}

	public void setActualLandingTime(DateTime actualLandingTime) {
		this.actualLandingTime = actualLandingTime;
	}

	public String getFlightName() {
		return flightName;
	}

	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getGate() {
		return gate;
	}

	public void setGate(String gate) {
		this.gate = gate;
	}

	public FlightState getFlightState() {
		return flightState;
	}

	public void setFlightState(FlightState flightState) {
		this.flightState = flightState;
	}

	public Long getFlightId() {
		return flightId;
	}

	public void setFlightId(Long flightId) {
		this.flightId = flightId;
	}	
}
