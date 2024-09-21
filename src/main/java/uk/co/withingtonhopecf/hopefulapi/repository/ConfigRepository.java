package uk.co.withingtonhopecf.hopefulapi.repository;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import uk.co.withingtonhopecf.hopefulapi.factory.DynamoTableFactory;
import uk.co.withingtonhopecf.hopefulapi.model.ConfigProperty;

@Repository
public class ConfigRepository {

	private final DynamoDbTable<ConfigProperty> table;

	public ConfigRepository(DynamoTableFactory dynamoTableFactory) {
		this.table = dynamoTableFactory.getTable("config", ConfigProperty.class);
	}

	public ConfigProperty getCarouselImages() {
		return table.getItem(Key.builder().partitionValue("carousel").build());
	}

	public ConfigProperty getHighlightTweet() {
		return table.getItem(Key.builder().partitionValue("tweet").build());
	}

}
