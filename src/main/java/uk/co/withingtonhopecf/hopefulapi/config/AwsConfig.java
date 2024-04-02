package uk.co.withingtonhopecf.hopefulapi.config;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {

	private final HopefulApiConfigurationProperties hapiConfig;

	@Bean
	@Profile("dev")
	public CognitoIdentityProviderClient devCognitoIdentityProviderClient() {
		return CognitoIdentityProviderClient.builder()
			.credentialsProvider(DefaultCredentialsProvider.create())
			.endpointOverride(URI.create(hapiConfig.cognitoUrl()))
			.region(Region.EU_WEST_1)
			.build();
	}

	@Bean
	@Profile("production")
	public CognitoIdentityProviderClient cognitoIdentityProviderClient() {
		return CognitoIdentityProviderClient.builder()
			.credentialsProvider(DefaultCredentialsProvider.create())
			.region(Region.EU_WEST_1)
			.build();
	}
}
