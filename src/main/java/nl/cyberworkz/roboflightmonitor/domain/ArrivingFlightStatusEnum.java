package nl.cyberworkz.roboflightmonitor.domain;


/**
 * @see also https://developer.schiphol.nl/apis/flight-api/flight-statuses.
 * 
 */
public enum ArrivingFlightStatusEnum {
	
	SCH("Flight scheduled"),
	AIR("Airborne"),
	EXP("Expected Landing"),
	FIR("FLight in Dutch Airspace"),
	LND("Landed on runway or taxiing to gate"),
	FIB("First bagage on the belt");
	
	private String description;
	
	private ArrivingFlightStatusEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
