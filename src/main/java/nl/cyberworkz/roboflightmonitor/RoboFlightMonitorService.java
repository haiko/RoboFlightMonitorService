/**
 * 
 */
package nl.cyberworkz.roboflightmonitor;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.cyberworkz.roboflightmonitor.domain.Flight;
import nl.cyberworkz.roboflightmonitor.domain.FlightDirection;
import nl.cyberworkz.roboflightmonitor.domain.FlightsResponse;
import nl.cyberworkz.roboflightmonitor.exceptions.BadRequestException;
import nl.cyberworkz.roboflightmonitor.exceptions.NotFoundException;

/**
 * Handles business logic for flights.
 * 
 * @author haiko
 *
 */
@Service
public class RoboFlightMonitorService {

	private static final Logger LOG = LoggerFactory.getLogger(RoboFlightMonitorService.class);

	@Value("${RFM_APP_KEY}")
	private String apiKey;

	@Value("${RFM_APP_ID}")
	private String apiId;

	@Value("${schiphol.api.baseurl}")
	private String baseUrl;

	@Value("${schiphol.api.resource.flights}")
	private String flightsResource;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	public List<Flight> getArrivingFlights(int page)
			throws BadRequestException, JsonParseException, JsonMappingException, IOException {

		URI uri = UriComponentsBuilder.fromUriString(baseUrl + flightsResource).queryParam("app_id", apiId)
				.queryParam("app_key", apiKey).queryParam("flightdirection", FlightDirection.ARRIVING.getDirection())
				.queryParam("page", page)
				.queryParam("scheduletime", new DateTime().toString("HH:mm"))
				.build().toUri();
		
		LOG.debug("URI:" + uri.toString());

		HttpHeaders headers = new HttpHeaders();
		headers.set("ResourceVersion", "v3");
		HttpEntity<Object> entity = new HttpEntity(headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			List<Flight> flights = mapper.readValue(responseEntity.getBody(), FlightsResponse.class).getFlights();
			flights.stream().forEach((flight) -> {
				flight.add(linkTo(Flight.class).slash(flight.getFlightId()).withSelfRel());
			});

			return flights;
		} else {
			LOG.error("request to airport API failed with code " + responseEntity.getStatusCodeValue());
			LOG.error("reason:" + responseEntity.getStatusCode().getReasonPhrase());
			throw new BadRequestException("failed API call with code " + responseEntity.getStatusCodeValue());
		}
	}

	/**
	 * Get Flight for given id.
	 * 
	 * @param flightId
	 * @return Found Flight
	 * @throws NotFoundException
	 */
	public Flight getFlight(Long flightId) throws NotFoundException {
		URI uri = UriComponentsBuilder.fromUriString(baseUrl + flightsResource).path("/").path(flightId.toString())
				.queryParam("app_id", apiId).queryParam("app_key", apiKey).build().toUri();
		
		LOG.debug("URI:" + uri.toString());

		HttpHeaders headers = new HttpHeaders();
		headers.set("ResourceVersion", "v3");
		HttpEntity<Object> entity = new HttpEntity(headers);

		ResponseEntity<Flight> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, Flight.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			return responseEntity.getBody();
		} else if (responseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			throw new NotFoundException("flight: " + flightId + " not found");
		} else {
			throw new RuntimeException("failed to retrieve flight with id:" + flightId + "\n"
					+ responseEntity.getStatusCodeValue() + " " + responseEntity.getStatusCode().getReasonPhrase());
		}
	}

}
