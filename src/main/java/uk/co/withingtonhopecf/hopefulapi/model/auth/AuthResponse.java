package uk.co.withingtonhopecf.hopefulapi.model.auth;

public record AuthResponse(
	String accessToken,
	String userSub
) {}
