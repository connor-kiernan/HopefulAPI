package uk.co.withingtonhopecf.hopefulapi.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "hopeful-api")
public record HopefulApiConfigurationProperties(
	@NotBlank String userPoolId,
	String cognitoUrl,
	String frontendUrl
) {}

