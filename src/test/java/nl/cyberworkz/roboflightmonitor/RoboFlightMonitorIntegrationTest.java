/**
 * 
 */
package nl.cyberworkz.roboflightmonitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.cyberworkz.roboflightmonitor.domain.Flight;
import nl.cyberworkz.roboflightmonitor.domain.FlightResponse;

/**
 * @author haiko
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class, IntegrationTestConfig.class })
@WebAppConfiguration
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class })
@TestPropertySource("classpath:application.properties")
public class RoboFlightMonitorIntegrationTest {

	@Autowired
	private MockLambdaContext lambdaContext;

	@Autowired
	private ObjectMapper mapper;


	@Autowired
	protected SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

	@Test
	public void shouldGetFlights() throws IOException {
		// when
		AwsProxyRequest request = new AwsProxyRequestBuilder("/flights", "GET")
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE).build();
		AwsProxyResponse response = handler.proxy(request, lambdaContext);

		// then
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());

		response = handler.proxy(request, lambdaContext);
		String responseBody = response.getBody().toString();
		
		FlightResponse flightResponse= mapper.readValue(responseBody, FlightResponse.class);
		assertNotNull(flightResponse.getArrivingFlights());
		
		// test for self-link
		//assertEquals("self", flightResponse.getArrivingFlights().get(0).getLinks().get(0).getRel());
	}
	
	@Test
	public void shouldGetSpecificFlight() throws IOException {
		// when
		AwsProxyRequest request = new AwsProxyRequestBuilder("/flights", "GET")
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE).build();
		AwsProxyResponse response = handler.proxy(request, lambdaContext);

		// then
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());

		response = handler.proxy(request, lambdaContext);
		String responseBody = response.getBody().toString();
		
		FlightResponse flightResponse= mapper.readValue(responseBody, FlightResponse.class);
		assertNotNull(flightResponse);
		
		// get ID
		Long flightId = flightResponse.getArrivingFlights().get(0).getFlightId();
		
		String path = "/flights/" + flightId.toString();
		
		AwsProxyRequest requestForFlight = new AwsProxyRequestBuilder(path, "GET")
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE).build();
		AwsProxyResponse responseForFlight = handler.proxy(requestForFlight, lambdaContext);

		assertNotNull(responseForFlight.getBody());
		
		Flight testFlight = mapper.readValue(responseForFlight.getBody(), Flight.class);
		assertNotNull(testFlight);
		assertEquals(flightId, testFlight.getFlightId());
		 
	}

}
