package nl.cyberworkz.roboflightmonitor;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import nl.cyberworkz.roboflightmonitor.domain.Flight;
import nl.cyberworkz.roboflightmonitor.domain.FlightResponse;
import nl.cyberworkz.roboflightmonitor.exceptions.BadRequestException;
import nl.cyberworkz.roboflightmonitor.exceptions.NotFoundException;

import javax.servlet.http.HttpServletResponse;

/**
 * Handles incoming requests
 * 
 * @author haiko
 *
 */
@RestController
@ExposesResourceFor(Flight.class)
@RequestMapping(value = "/flights")
@Import(RoboFlightMonitorService.class)
public class RoboFlightMonitorController {

	private static final Logger LOG = LoggerFactory.getLogger(RoboFlightMonitorController.class);

	@Autowired
	private RoboFlightMonitorService service;

	/**
	 * Get Flights
	 *
	 * @param page
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws BadRequestException
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<FlightResponse> getCurrentArrivingFlights(@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String scheduleTime)
			throws JsonParseException, JsonMappingException, BadRequestException, IOException {

		if (page > 30) {
			throw new BadRequestException("page param is invalid!");
		}

		FlightResponse flightResponse = null;

		if (scheduleTime == null) {
			flightResponse = service.getArrivingFlights(page);
		} else {
			DateTime time = DateTime.parse(scheduleTime, DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm"));
			flightResponse = service.getArrivingFlights(page, time);
		}

		return new ResponseEntity<FlightResponse>(flightResponse, HttpStatus.OK);
	}

	/**
	 * Get a specific {@link Flight} for given id.
	 *
	 * @param flightId
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{flightId}", produces = "application/json")
	public ResponseEntity<Flight> getFlight(@PathVariable String flightId) throws NotFoundException, JsonParseException, JsonMappingException, IOException {
		return new ResponseEntity<Flight>(service.getFlight(flightId), HttpStatus.OK);
	}

	@RequestMapping(value= "/**", method=RequestMethod.OPTIONS)
	public void corsHeaders(HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
		response.addHeader("Access-Control-Max-Age", "3600");
	}
}
