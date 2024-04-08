package uk.co.withingtonhopecf.hopefulapi.model.auth;

public record AuthCodeRequest (
	String authCode,
	String redirectUri
) {}
