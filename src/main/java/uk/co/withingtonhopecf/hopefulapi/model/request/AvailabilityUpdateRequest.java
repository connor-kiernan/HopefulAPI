package uk.co.withingtonhopecf.hopefulapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import uk.co.withingtonhopecf.hopefulapi.model.Availability;

@Validated
public record AvailabilityUpdateRequest(
	@NotBlank String userSub,
	@NotBlank String matchId,
	@NotNull Availability availability
) {}
