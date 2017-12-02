/**
 * 
 */
package nl.cyberworkz.roboflightmonitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.cyberworkz.roboflightmonitor.domain.FlightStatus;

/**
 * @author haiko
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class, IntegrationTestConfig.class })
@WebAppConfiguration
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class })
public class RoboFlightMonitorIntegrationTest {

	@Autowired
	private MockLambdaContext lambdaContext;
	
	
	@Autowired
	private ObjectMapper mapper;

	private Logger LOG = LogManager.getLogger(RoboFlightMonitorIntegrationTest.class);

	@Autowired
	protected SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
	
	
	@Test
	public void shouldTestCall() throws IOException {
		//when
		AwsProxyRequest request = new AwsProxyRequestBuilder("/flights", "GET").
				header("Content-Type", MediaType.APPLICATION_JSON_VALUE).
				build();
		AwsProxyResponse response = handler.proxy(request, lambdaContext);
		
		
		// then
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		
		
		
		response = handler.proxy(request, lambdaContext);
		String responseBody = response.getBody().toString();
		assertTrue(responseBody.contains("test ok"));
		
	}
	
	@Test
	public void shouldFindFlight() throws IOException {
		// when
		AwsProxyRequest request = new AwsProxyRequestBuilder("/flights/1234/status", "GET").
				header("Content-Type", MediaType.APPLICATION_JSON_VALUE).
				build();
		AwsProxyResponse response = handler.proxy(request, lambdaContext);
		
		
		// then
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		
		
		
		response = handler.proxy(request, lambdaContext);
		FlightStatus status = mapper.readValue(response.getBody(), FlightStatus.class);
		assertEquals("test status ok", status.getStatus());
		
	}
	
	
}
