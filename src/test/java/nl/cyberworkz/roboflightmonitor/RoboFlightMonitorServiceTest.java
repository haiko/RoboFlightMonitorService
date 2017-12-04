/**
 * 
 */
package nl.cyberworkz.roboflightmonitor;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import nl.cyberworkz.roboflightmonitor.domain.Flight;
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
		List<Flight> flights = service.getFlights(0);
		
		assertTrue(!flights.isEmpty());
	
	}

}
