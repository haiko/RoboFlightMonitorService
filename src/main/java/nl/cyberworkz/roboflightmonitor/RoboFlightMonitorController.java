package nl.cyberworkz.roboflightmonitor;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import nl.cyberworkz.roboflightmonitor.domain.Flight;
import nl.cyberworkz.roboflightmonitor.exceptions.BadRequestException;

/**
 * Handles incoming requests
 * 
 * @author haiko
 *
 */
@RestController
@RequestMapping(value = "/flights")
public class RoboFlightMonitorController {

	private static final Logger LOG = LogManager.getLogger(RoboFlightMonitorController.class);

	@Autowired
	private RoboFlightMonitorService service;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Flight>> getCurrentArrivingFlights(@RequestParam(defaultValue = "0") int page)
			throws JsonParseException, JsonMappingException, BadRequestException, IOException {
		
		if (page > 20) {
			throw new BadRequestException("page param is invalid!");
		}
		
		List<Flight> flights = service.getFlights(page);
		return new ResponseEntity<List<Flight>>(flights, HttpStatus.OK);
	}
}
