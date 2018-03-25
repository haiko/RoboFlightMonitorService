/**
 * 
 */
package nl.cyberworkz.roboflightmonitor;

import java.io.IOException;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityLinks;
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
import nl.cyberworkz.roboflightmonitor.domain.FlightResponse;
import nl.cyberworkz.roboflightmonitor.domain.SchipholFlightsResponse;
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
	private DestinationRepository destinationRepo;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private EntityLinks entityLinks;
	
	public FlightResponse getArrivingFlights(int page)
			throws BadRequestException, JsonParseException, JsonMappingException, IOException {
		DateTime scheduleTime = new DateTime(DateTimeZone.forOffsetHours(1)).minusMinutes(15);
		
		return this.getArrivingFlights(page, scheduleTime);
	}

	public FlightResponse getArrivingFlights(int page, DateTime scheduleTime)
			throws BadRequestException, JsonParseException, JsonMappingException, IOException {
		

		URI uri = UriComponentsBuilder.fromUriString(baseUrl + flightsResource).queryParam("app_id", apiId)
				.queryParam("app_key", apiKey).queryParam("flightdirection", FlightDirection.ARRIVING.getDirection())
				.queryParam("page", page)
				.queryParam("scheduletime",
						scheduleTime.toString("HH:mm"))
				.queryParam("sort", "+scheduletime").build().toUri();

		LOG.debug("URI:" + uri.toString());

		HttpHeaders headers = new HttpHeaders();
		headers.set("ResourceVersion", "v3");
		HttpEntity<Object> entity = new HttpEntity(headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			List<Flight> flights = mapper.readValue(responseEntity.getBody(), SchipholFlightsResponse.class)
					.getFlights();
			
			// exclude flights on freight
			flights = flights.parallelStream().filter(f -> {
				
				if(f.getServiceType() != null && (f.getServiceType().equalsIgnoreCase("J") || f.getServiceType().equalsIgnoreCase("C"))){
					return true;
				}
				return false;
			}).collect(Collectors.toList());
			
			// enrich Flight
			flights.stream().forEach((flight) -> {
				flight.add(entityLinks.linkToSingleResource(Flight.class, flight.getFlightId()));
				flight.setOrigin(destinationRepo.getDestination(flight.getRoute().getDestinations().get(0)));
				flight.deriveLandingTime();
			});
			
			flights.sort(Comparator.comparing(Flight::getDerivedLandingTime));

			// build response
			FlightResponse response = new FlightResponse();
			response.setArrivingFlights(flights);
			response.setNextLink(buildLink(page + 2, scheduleTime));
		
			if (page != 0) {
				response.setPreviousLink(buildLink(page - 1, scheduleTime));
			}

			return response;
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
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Flight getFlight(String flightId)
			throws NotFoundException, JsonParseException, JsonMappingException, IOException {
		URI uri = UriComponentsBuilder.fromUriString(baseUrl + flightsResource).path("/").path(flightId.toString())
				.queryParam("app_id", apiId).queryParam("app_key", apiKey).build().toUri();

		LOG.debug("URI:" + uri.toString());

		HttpHeaders headers = new HttpHeaders();
		headers.set("ResourceVersion", "v3");
		HttpEntity<Object> entity = new HttpEntity(headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {

			Flight flight = mapper.readValue(responseEntity.getBody(), Flight.class);
			flight.setOrigin(destinationRepo.getDestination(flight.getRoute().getDestinations().get(0)));
			flight.deriveLandingTime();
			
			return flight;
		} else if (responseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			throw new NotFoundException("flight: " + flightId + " not found");
		} else {
			throw new RuntimeException("failed to retrieve flight with id:" + flightId + "\n"
					+ responseEntity.getStatusCodeValue() + " " + responseEntity.getStatusCode().getReasonPhrase());
		}
	}


	private String buildLink(int page, DateTime scheduleTime) {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.path("flights");
		builder.queryParam("page", page);
		builder.queryParam("scheduletime", scheduleTime.toString("HH:mm"));
		return builder.toUriString();
	}

}
