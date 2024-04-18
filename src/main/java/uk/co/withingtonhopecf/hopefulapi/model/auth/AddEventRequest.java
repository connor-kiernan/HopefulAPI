package uk.co.withingtonhopecf.hopefulapi.model.auth;

import lombok.Builder;
import uk.co.withingtonhopecf.hopefulapi.model.enums.PitchType;

@Builder
public record AddEventRequest(
	String opponent,
	String kickOffDateTime,
	PitchType pitchType,
	String address1,
	String address2,
	String postcode,
	Boolean isHomeGame,
	Boolean isHomeKit,
	String eventType
) {}
