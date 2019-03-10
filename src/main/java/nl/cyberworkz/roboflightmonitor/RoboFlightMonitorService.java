/**
 * 
 */
package nl.cyberworkz.roboflightmonitor;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.*;
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
@Import(DestinationRepository.class)
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

	private TimeZone tz;

	private DateTimeZone dtz;

	public RoboFlightMonitorService() {
		tz = TimeZone.getTimeZone("Europe/Amsterdam");
		dtz = DateTimeZone.forTimeZone(tz);
	}

	public FlightResponse getArrivingFlights(int page)
			throws BadRequestException, JsonParseException, JsonMappingException, IOException {

		DateTime scheduleTime = new DateTime(dtz).minusMinutes(15);

		return this.getArrivingFlights(page, scheduleTime);
	}

	public FlightResponse getArrivingFlights(int page, DateTime scheduleTime)
			throws BadRequestException, JsonParseException, JsonMappingException, IOException {

		URI uri = UriComponentsBuilder.fromUriString(baseUrl + flightsResource)
				.queryParam("flightDirection", FlightDirection.ARRIVING.getDirection())
				.queryParam("page", page)
				.queryParam("fromDateTime", scheduleTime.toString("yyyy-MM-dd").concat("T").
						concat(scheduleTime.toString("HH:mm:ss")))
                .queryParam("includeDelays", "false")
				.queryParam("searchDateTimeField", "estimatedLandingTime")
				.queryParam("sort", "+estimatedLandingTime").build().toUri();

		LOG.debug("URI:" + uri.toString());

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, getHeaders(), String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {

			List<Flight> flights = mapper.readValue(responseEntity.getBody(), SchipholFlightsResponse.class)
					.getFlights();

			// exclude flights on freight
			flights = flights.parallelStream().filter(f -> {

				if (f.getServiceType() != null
						&& (f.getServiceType().equalsIgnoreCase("J") || f.getServiceType().equalsIgnoreCase("C"))) {
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
			response.setNextLink(buildLink(page + 1, scheduleTime));

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

    private HttpEntity<Object> getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("ResourceVersion", "v4");
        headers.set("app_id", apiId);
        headers.set("app_key", apiKey);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return (HttpEntity<Object>) new HttpEntity(headers);
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
				.build().toUri();

		LOG.debug("URI:" + uri.toString());

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, getHeaders(), String.class);

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
		builder.queryParam("scheduleTime", scheduleTime.toString("yyyy-MM-dd'T'HH:mm"));
		return builder.toUriString();
	}

}
