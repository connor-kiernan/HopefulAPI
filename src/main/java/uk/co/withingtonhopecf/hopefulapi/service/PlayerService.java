package uk.co.withingtonhopecf.hopefulapi.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import uk.co.withingtonhopecf.hopefulapi.config.HopefulApiConfigurationProperties;
import uk.co.withingtonhopecf.hopefulapi.model.Player;
import uk.co.withingtonhopecf.hopefulapi.transformer.UserTypeToPlayerTransformer;

@Component
@RequiredArgsConstructor
public class PlayerService {

	private final CognitoIdentityProviderClient cognitoIdentityProviderClient;
	private final HopefulApiConfigurationProperties config;

	private final UserTypeToPlayerTransformer userTypeToPlayerTransformer = new UserTypeToPlayerTransformer();

public List<Player> listAllPlayers() {
		ListUsersRequest request = ListUsersRequest.builder()
			.userPoolId(config.userPoolId())
			.build();

		ListUsersResponse response = cognitoIdentityProviderClient.listUsers(request);

		return userTypeToPlayerTransformer.transform(response.users());
	}
}
