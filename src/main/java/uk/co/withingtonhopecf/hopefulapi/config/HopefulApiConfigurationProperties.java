package uk.co.withingtonhopecf.hopefulapi.config;

import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
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
	List<String> frontendUrls,
	@NotBlank String matchesTableName
) {}

