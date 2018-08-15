/**
 * 
 */
package nl.cyberworkz.roboflightmonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

import nl.cyberworkz.roboflightmonitor.domain.Destination;

/**
 * Interfaces with DestinationRepo.
 * 
 * @author haiko
 *
 */
@Service
public class DestinationRepository {
	
	private static Logger LOG = LoggerFactory.getLogger(DestinationRepository.class);
	
	@Value("${destinations.tablename}")
	private String destinationTable;
	
	@Autowired
	private AmazonDynamoDB dynamoDBClient;	
	
	
	public Destination getDestination(String iataCode) {
		
		if(StringUtils.hasText(iataCode)) {
			DynamoDB db = new DynamoDB(dynamoDBClient);
			
			Table table =  db.getTable(destinationTable);
			GetItemSpec spec = new GetItemSpec().withPrimaryKey("iata", iataCode);
			
			Item outcome = table.getItem(spec);
			if(outcome != null) {
				return Destination.create(outcome.getString("iata"), outcome.getString("city"), outcome.getString("country"), 
						outcome.getString("airportName_NL"), outcome.getString("airportName_EN"));
			}
		}
		
		return new Destination();
	}

}
