/**
 * 
 */
package nl.cyberworkz.roboflightmonitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

/**
 * @author haiko
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class, IntegrationTestConfig.class })
@WebAppConfiguration
@TestExecutionListeners(inheritListeners = false, listeners = { DependencyInjectionTestExecutionListener.class })
@TestPropertySource("classpath:application-test.properties")
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
		// when
		AwsProxyRequest request = new AwsProxyRequestBuilder("/flights", "GET")
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE).build();
		AwsProxyResponse response = handler.proxy(request, lambdaContext);

		// then
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());

		response = handler.proxy(request, lambdaContext);
		String responseBody = response.getBody().toString();
		
		Flight[] flights= mapper.readValue(responseBody, Flight[].class);
		assertNotNull(flights);
		
		// test for self-link
		assertEquals("self", flights[0].getLinks().get(0).getRel());
	}

}
