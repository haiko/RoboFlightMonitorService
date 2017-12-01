package nl.cyberworkz.roboflightmonitor.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a current Flight from/ to airport.
 * 
 * @author haiko
 *
 */
public class Flight {
	
	private String id;
	
	private String flightNamen;
	
	private FlightDirection flightDirection;
	
	private LocalDate scheduledDate;
	
	private LocalTime scheduleTime;
	
	private String flightNumber;
	
	private String iataCode;
	
	private LocalDateTime estimatedLandingTime;
	
	private LocalDateTime actualLandingTime;
	
	

}
