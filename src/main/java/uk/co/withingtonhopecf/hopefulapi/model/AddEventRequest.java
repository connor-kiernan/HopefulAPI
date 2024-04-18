package uk.co.withingtonhopecf.hopefulapi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;
import uk.co.withingtonhopecf.hopefulapi.model.enums.PitchType;

@Builder
@Validated
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
