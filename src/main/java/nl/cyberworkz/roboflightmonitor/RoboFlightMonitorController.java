package nl.cyberworkz.roboflightmonitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles incoming requests
 * 
 * @author haiko
 *
 */
@RestController
@RequestMapping(value="/flightstatus")
public class RoboFlightMonitorController {

	
	private static final Logger LOG = LogManager.getLogger(RoboFlightMonitorController.class);
	
	@Autowired
	private RoboFlightMonitorService service;
	
	@RequestMapping(method=RequestMethod.GET, value="${id}")
	public ResponseEntity<FlightStatus> getFlightStatus(@PathVariable String id) {
		
		LOG.debug("flight:" + id);
		
		return new ResponseEntity<FlightStatus> (new FlightStatus(), HttpStatus.OK);
		
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<String> testCall() {
		return new ResponseEntity<String>("test ok", HttpStatus.OK);
	}
}
