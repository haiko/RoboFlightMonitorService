/**
 * 
 */
package nl.cyberworkz.roboflightmonitor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import nl.cyberworkz.roboflightmonitor.exceptions.BadRequestException;

/**
 * @author haiko
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@WebAppConfiguration
@TestPropertySource("classpath:application-test.properties")
public class RoboMonitorControllerTest {
	
	@InjectMocks
	private RoboFlightMonitorController controller;
	
	@Mock
	private RoboFlightMonitorService service;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test method for {@link nl.cyberworkz.roboflightmonitor.RoboFlightMonitorController#getCurrentArrivingFlights(int)}.
	 * @throws IOException 
	 * @throws BadRequestException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Test
	public void shouldPassPageParamGetCurrentArrivingFlights() throws JsonParseException, JsonMappingException, BadRequestException, IOException {
		controller.getCurrentArrivingFlights(9);
		
		//verify
		verify(service).getFlights(9);
	}

}
