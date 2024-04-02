package uk.co.withingtonhopecf.hopefulapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;
import uk.co.withingtonhopecf.hopefulapi.model.Player;
import uk.co.withingtonhopecf.hopefulapi.model.enums.Position;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

	@Mock
	private CognitoIdentityProviderClient cognitoIdentityProviderClient;

	@InjectMocks
	private PlayerService playerService;

	@Test
	void listAllPlayersTest() {
		final ListUsersRequest listUsersRequest = ListUsersRequest.builder()
			.userPoolId("eu-west-1_EWIfXTFmF")
			.build();

		final ListUsersResponse listUsersResponse = ListUsersResponse.builder()
			.users(
				buildUserType("Connor", "Kiernan", "37", "LB"),
				buildUserType("Jo", "Doe", "17", "CF")
			)
			.build();

		when(cognitoIdentityProviderClient.listUsers(listUsersRequest)).thenReturn(listUsersResponse);

		List<Player> actualPlayers = playerService.listAllPlayers();

		List<Player> expectedPlayers = List.of(
			new Player("ConnorKiernan", "Connor", "Kiernan", 37, Position.LB),
			new Player("JoDoe", "Jo", "Doe", 17, Position.CF)
		);

		assertEquals(expectedPlayers, actualPlayers, "Players did not match");
	}

	private UserType buildUserType(String givenName, String familyName, String kitNumber, String position) {
		return UserType.builder()
			.username(givenName + familyName)
			.attributes(
				AttributeType.builder()
					.name("given_name")
					.value(givenName)
					.build(),
				AttributeType.builder()
					.name("family_name")
					.value(familyName)
					.build(),
				AttributeType.builder()
					.name("custom:kit_number")
					.value(kitNumber)
					.build(),
				AttributeType.builder()
					.name("custom:position")
					.value(position)
					.build())
			.build();
	}
}