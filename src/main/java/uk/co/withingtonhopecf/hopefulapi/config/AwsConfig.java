package uk.co.withingtonhopecf.hopefulapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {

	private final HopefulApiConfigurationProperties hapiConfig;

	@Bean
	public CognitoIdentityProviderClient cognitoIdentityProviderClient() {
		return CognitoIdentityProviderClient.builder()
			.credentialsProvider(DefaultCredentialsProvider.create())
			.endpointOverride(hapiConfig.cognitoUrl())
			.region(Region.EU_WEST_1)
			.build();
	}

	@Bean
	public DynamoDbClient dynamoDbClient() {
		return DynamoDbClient.builder()
			.credentialsProvider(DefaultCredentialsProvider.create())
			.region(Region.EU_WEST_1)
			.endpointOverride(hapiConfig.dynamoDbUrl())
			.build();
	}

	@Bean
	public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
		return DynamoDbEnhancedClient.builder()
			.dynamoDbClient(dynamoDbClient())
			.build();
	}
}
