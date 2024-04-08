package uk.co.withingtonhopecf.hopefulapi.config;

import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "hopeful-api")
public record HopefulApiConfigurationProperties(
	@NotBlank String userPoolId,
	@NotBlank String clientId,
	@NotBlank String tokenUrl,
	URI cognitoUrl,
	URI dynamoDbUrl,
	String frontendUrl,
	@NotBlank String matchesTableName
) {}

