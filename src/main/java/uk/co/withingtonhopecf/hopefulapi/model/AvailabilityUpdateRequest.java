package uk.co.withingtonhopecf.hopefulapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record AvailabilityUpdateRequest(
	@NotBlank String userSub,
	@NotBlank String matchId,
	@NotNull Availability availability
) {}
