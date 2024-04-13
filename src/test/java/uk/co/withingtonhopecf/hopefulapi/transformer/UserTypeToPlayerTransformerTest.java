package uk.co.withingtonhopecf.hopefulapi.transformer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;
import uk.co.withingtonhopecf.hopefulapi.model.Player;
import uk.co.withingtonhopecf.hopefulapi.model.enums.Position;

class UserTypeToPlayerTransformerTest {

	private final UserTypeToPlayerTransformer transformer = new UserTypeToPlayerTransformer();

	@Test
	void transform() {
		List<Player> players = transformer.transform(List.of(buildUserType("connor", "kiernan", "37", "LB")));

		List<Player> expectedPlayers = List.of(new Player("1", "connorkiernan", "connor", "kiernan", 37, Position.LB, "http://localhost/image.jpg"));

		assertEquals(expectedPlayers, players, "Players did not match");
	}

	private UserType buildUserType(String givenName, String familyName, String kitNumber, String position) {
		return UserType.builder()
			.username(givenName + familyName)
			.attributes(
				AttributeType.builder()
					.name("sub")
					.value("1")
					.build(),
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
					.build(),
				AttributeType.builder()
					.name("picture")
					.value("http://localhost/image.jpg")
					.build()
			)
			.build();
	}
}