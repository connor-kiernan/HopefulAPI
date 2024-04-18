package uk.co.withingtonhopecf.hopefulapi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import uk.co.withingtonhopecf.hopefulapi.model.enums.PitchType;

@Builder
public record AddEventRequest(
	@NotBlank String opponent,
	@NotBlank String kickOffDateTime,
	PitchType pitchType,
	@NotBlank String address1,
	String address2,
	@NotBlank String postcode,
	Boolean isHomeGame,
	Boolean isHomeKit,
	@NotBlank String eventType
) {}