package uk.co.withingtonhopecf.hopefulapi.model.auth;

import java.util.List;

public record AuthResponse(
	String accessToken,
	String userSub,
	List<String> roles
) {}
