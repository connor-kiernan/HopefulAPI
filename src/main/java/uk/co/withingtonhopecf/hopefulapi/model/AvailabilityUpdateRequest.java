package uk.co.withingtonhopecf.hopefulapi.model;

public record AvailabilityUpdateRequest(
	String userSub,
	String matchId,
	Availability availability
) {}
