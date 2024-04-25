package uk.co.withingtonhopecf.hopefulapi.repository;

import static software.amazon.awssdk.enhanced.dynamodb.internal.AttributeValues.stringValue;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import uk.co.withingtonhopecf.hopefulapi.config.HopefulApiConfigurationProperties;
import uk.co.withingtonhopecf.hopefulapi.model.Availability;
import uk.co.withingtonhopecf.hopefulapi.model.Match;

@Repository
@RequiredArgsConstructor
public class MatchRepository {

	private final HopefulApiConfigurationProperties config;
	private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

	public PageIterable<Match> publicListWithAttributes(List<String> attributes) {
		ScanEnhancedRequest request = ScanEnhancedRequest.builder()
			.attributesToProject(attributes)
			.filterExpression(Expression.builder()
				.expression("attribute_not_exists (eventType) OR eventType = :eventType")
				.putExpressionValue(":eventType", stringValue("GAME"))
				.build())
			.build();

		return getTable().scan(request);
	}

	public PageIterable<Match> listWithAttributes(List<String> attributes) {
		ScanEnhancedRequest request = ScanEnhancedRequest.builder()
			.attributesToProject(attributes)
			.build();

		return getTable().scan(request);
	}

	public void upsertAvailability(String matchId, String userSub, Availability availability) {
		DynamoDbTable<Match> table = getTable();

		try {
			Match match = table.getItem(Key.builder().partitionValue(matchId).build());

			match.getPlayerAvailability().put(userSub, availability);

			table.updateItem(match);
		} catch (ResourceNotFoundException e) {
			throw new IllegalArgumentException("Match not found");
		}
	}

	public void deleteEvent(String eventId) {
		getTable().deleteItem(Key.builder().partitionValue(eventId).build());
	}

	private DynamoDbTable<Match> getTable() {
		return dynamoDbEnhancedClient.table(config.matchesTableName(), TableSchema.fromImmutableClass(Match.class));
	}

	public void addEvent(Match match) {
		getTable().putItem(match);
	}

	public void updateEvent(Match match) {
		UpdateItemEnhancedRequest<Match> updateItemEnhancedRequest = UpdateItemEnhancedRequest.builder(Match.class)
			.item(match)
			.ignoreNulls(true)
			.build();

		getTable().updateItem(updateItemEnhancedRequest);
	}

	public void completeMatch(Match match) {
		DynamoDbTable<Match> table = getTable();

		try {
			Match matchInDb = table.getItem(Key.builder().partitionValue(match.getId()).build());

			if (!matchInDb.getEventType().equals("GAME")) {
				throw new IllegalArgumentException("Cannot complete an event that is not a game");
			}

			updateEvent(match);
		} catch (ResourceNotFoundException e) {
			throw new IllegalArgumentException("Match not found");
		}
	}
}
