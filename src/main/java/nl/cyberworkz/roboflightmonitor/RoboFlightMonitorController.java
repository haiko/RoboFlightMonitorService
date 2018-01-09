package nl.cyberworkz.roboflightmonitor;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import nl.cyberworkz.roboflightmonitor.domain.Flight;
import nl.cyberworkz.roboflightmonitor.domain.FlightResponse;
import nl.cyberworkz.roboflightmonitor.exceptions.BadRequestException;
import nl.cyberworkz.roboflightmonitor.exceptions.NotFoundException;

/**
 * Handles incoming requests
 * 
 * @author haiko
 *
 */
@RestController
@ExposesResourceFor(Flight.class)
@RequestMapping(value = "/flights")
public class RoboFlightMonitorController {

	private static final Logger LOG = LoggerFactory.getLogger(RoboFlightMonitorController.class);

	@Autowired
	private RoboFlightMonitorService service;

	/**
	 * 
	 * Get Flights
	 * 
	 * @param page
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws BadRequestException
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<FlightResponse> getCurrentArrivingFlights(@RequestParam(defaultValue = "0") int page)
			throws JsonParseException, JsonMappingException, BadRequestException, IOException {

		if (page > 20) {
			throw new BadRequestException("page param is invalid!");
		}

		FlightResponse flightResponse = service.getArrivingFlights(page);
		return new ResponseEntity<FlightResponse>(flightResponse, HttpStatus.OK);
	}

	/**
	 * Get a specific {@link Flight} for given id.
	 * 
	 * @param flightId
	 * @return
	 * @throws NotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{flightId}")
	public ResponseEntity<Flight> getFlight(@PathVariable Long flightId) throws NotFoundException {
		return new ResponseEntity<Flight>(service.getFlight(flightId), HttpStatus.OK);
	}
}
