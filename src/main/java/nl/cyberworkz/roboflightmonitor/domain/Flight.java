package nl.cyberworkz.roboflightmonitor.domain;

import java.time.LocalDateTime;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a current Flight from/ to airport.
 * 
 * @author haiko
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Flight extends ResourceSupport {

	@JsonProperty("id")
	private String flightId;

	private String flightName;

	private FlightDirection flightDirection;
	
	@JsonProperty("publicFlightState")
	private FlightState flightState;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private DateTime scheduleDate;

	@JsonFormat(pattern = "HH:mm:ss", timezone= "Europe/Berlin")
	private DateTime scheduleTime;

	private String flightNumber;

	private boolean timeDeviation;

	private Route route;

	@JsonProperty(value = "prefixIATA")
	private String iataCode;

	@JsonFormat(timezone = "Europe/Berlin")
	private DateTime estimatedLandingTime;
	
	// either scheduled or estimatedLandingTime
	@JsonFormat(timezone = "Europe/Berlin")
	private LocalDateTime derivedLandingTime;

	private String serviceType;

    @JsonFormat(timezone = "Europe/Berlin")
	private DateTime expectedTimeOnBelt;
	
	@JsonFormat(timezone = "Europe/Berlin")
	private DateTime actualLandingTime;

	private String terminal;

	private String gate;

	private Destination origin;

	public DateTime getExpectedTimeOnBelt() {
		return expectedTimeOnBelt;
	}

	public void setExpectedTimeOnBelt(DateTime expectedTimeOnBelt) {
		this.expectedTimeOnBelt = expectedTimeOnBelt;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public LocalDateTime getDerivedLandingTime() {
		return derivedLandingTime;
	}

	public void setDerivedLandingTime(LocalDateTime derivedLandingTime) {
		this.derivedLandingTime = derivedLandingTime;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Destination getOrigin() {
		return origin;
	}

	public void setOrigin(Destination origin) {
		this.origin = origin;
	}

	/**
	 * Derive the landingtime on the basis if there is a deviation of the flight
	 * state.
	 */
	public void deriveLandingTime() {
		if (this.getFlightState().getStates().contains("EXP")) {			
			this.derivedLandingTime = LocalDateTime.of(this.estimatedLandingTime.getYear(), this.estimatedLandingTime.getMonthOfYear(), this.estimatedLandingTime.getDayOfMonth(),
					this.estimatedLandingTime.getHourOfDay(), this.estimatedLandingTime.getMinuteOfHour(), this.estimatedLandingTime.getSecondOfMinute());
		} else {
			this.derivedLandingTime = LocalDateTime.of(this.scheduleDate.getYear(), this.scheduleDate.getMonthOfYear(), this.scheduleDate.getDayOfMonth(),
					this.scheduleTime.getHourOfDay(), this.scheduleTime.getMinuteOfHour(), this.scheduleTime.getSecondOfMinute());	
		}
	}

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

	public String getFlightId() {
		return flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}


	public boolean isTimeDeviation() {
		return timeDeviation;
	}

	public void setTimeDeviation(boolean timeDeviation) {
		this.timeDeviation = timeDeviation;
	}

}
