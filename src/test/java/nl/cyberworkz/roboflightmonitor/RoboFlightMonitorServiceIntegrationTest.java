package nl.cyberworkz.roboflightmonitor;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import nl.cyberworkz.roboflightmonitor.domain.FlightResponse;
import nl.cyberworkz.roboflightmonitor.exceptions.BadRequestException;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
public class RoboFlightMonitorServiceIntegrationTest {
	
	private String URL = "https://api.schiphol.nl/public-flights/flights?"
			+ "flightDirection=A&page=0&fromDateTime=2019-02-14T21:31:00&includeDelays=false&searchDateTimeField=scheduleDateTime&sort=+scheduleTime";

	private MockRestServiceServer mockServer;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private RoboFlightMonitorService service;
	
	@Before
	public void setUp() throws Exception {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void shouldFilterFreightFlights() throws IOException, BadRequestException {
		mockServer.expect(ExpectedCount.once(), requestTo(URL)).
		andRespond(withSuccess(IOUtils.toString(new ClassPathResource("schiphol-response-freight.json").getInputStream(), Charset.forName("UTF-8")), MediaType.APPLICATION_JSON));
	
		DateTime time = new DateTime().withTime(21, 31, 0, 0);
		
		FlightResponse response = service.getArrivingFlights(0, time);
		
		mockServer.verify();
		
		//expect two to be filtered out
		assertEquals(16, response.getArrivingFlights().size());
	}

}
