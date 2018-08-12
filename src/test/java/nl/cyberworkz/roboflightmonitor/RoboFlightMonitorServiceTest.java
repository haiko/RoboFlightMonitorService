/**
 * 
 */
package nl.cyberworkz.roboflightmonitor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import nl.cyberworkz.roboflightmonitor.domain.Flight;
import nl.cyberworkz.roboflightmonitor.domain.FlightResponse;
import nl.cyberworkz.roboflightmonitor.exceptions.BadRequestException;

/**
 * @author haiko
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@WebAppConfiguration
@TestPropertySource("classpath:application.properties")
public class RoboFlightMonitorServiceTest {
	
	private static Logger LOG = LoggerFactory.getLogger(RoboFlightMonitorServiceTest.class);
	
	@Autowired
	private RoboFlightMonitorService service;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link nl.cyberworkz.roboflightmonitor.RoboFlightMonitorService#getFlights()}.
	 * @throws BadRequestException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Test
	public void testGetFlights() throws BadRequestException, JsonParseException, JsonMappingException, IOException {
		FlightResponse response = service.getArrivingFlights(0);
		
		assertTrue(!response.getArrivingFlights().isEmpty());
		
		//get a flight and validate non null fields
		Flight flight = response.getArrivingFlights().get(3);
		assertNotNull(flight.getFlightId());
		assertNotNull(flight.getFlightName());
		assertNotNull(flight.getScheduledDate());	
		
		List<Flight> flights = response.getArrivingFlights();
		
		for (Flight aFlight : flights) {
			LOG.debug("schedule time: " + aFlight.getScheduleTime().toString("HH:mm") + "  from: " + aFlight.getOrigin().getCity() + "  number: " + aFlight.getFlightName());
			LOG.debug("estimated time: " + aFlight.getEstimatedLandingTime().toString("HH:mm") + "  from: " + aFlight.getOrigin().getCity() + "  number: " + aFlight.getFlightName());
			LOG.debug("derived time: " + aFlight.getDerivedLandingTime().toString("HH:mm") + "  from: " + aFlight.getOrigin().getCity() + "  number: " + aFlight.getFlightName());

		}
		
		//test sorting
		//TODO sometime
	}

}
