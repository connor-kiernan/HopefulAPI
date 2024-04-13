package uk.co.withingtonhopecf.hopefulapi.transformer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;
import uk.co.withingtonhopecf.hopefulapi.model.Player;
import uk.co.withingtonhopecf.hopefulapi.model.enums.Position;

public class UserTypeToPlayerTransformer {

	public List<Player> transform(List<UserType> userTypeList) {
		return userTypeList.stream().map(this::transform).toList();
	}

	public Player transform(UserType userType) {
		Map<String, String> attributesByName = userType.attributes().stream()
			.collect(Collectors.toMap(AttributeType::name, AttributeType::value));

		return new Player(
			attributesByName.get("sub"),
			userType.username(),
			attributesByName.get("given_name"),
			attributesByName.get("family_name"),
			attributesByName.containsKey("custom:kit_number") ? Integer.valueOf(attributesByName.get("custom:kit_number")) : null,
			Position.valueOf(attributesByName.get("custom:position")),
			attributesByName.get("picture"));
	}
}
