package uk.co.withingtonhopecf.hopefulapi.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AccessTokenPayload(
	@JsonProperty String sub,
	@JsonProperty("cognito:groups") List<String> groups
) {}
