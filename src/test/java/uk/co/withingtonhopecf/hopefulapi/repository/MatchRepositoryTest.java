package uk.co.withingtonhopecf.hopefulapi.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static software.amazon.awssdk.enhanced.dynamodb.internal.AttributeValues.stringValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import uk.co.withingtonhopecf.hopefulapi.config.HopefulApiConfigurationProperties;
import uk.co.withingtonhopecf.hopefulapi.model.Availability;
import uk.co.withingtonhopecf.hopefulapi.model.Match;
import uk.co.withingtonhopecf.hopefulapi.model.enums.AvailabilityStatus;

@ExtendWith(MockitoExtension.class)
class MatchRepositoryTest {

	@Mock
	private HopefulApiConfigurationProperties config;

	@Mock
	private DynamoDbEnhancedClient dynamoDbEnhancedClient;

	@InjectMocks
	private MatchRepository matchRepository;

	@Mock
	private DynamoDbTable<Match> mockDynamoDbTable;

	@BeforeEach
	void setUp() {
		when(config.matchesTableName()).thenReturn("tableName");
		when(dynamoDbEnhancedClient.table("tableName", TableSchema.fromImmutableClass(Match.class))).thenReturn(
			mockDynamoDbTable);
	}

	@Test
	void listWithAttributes() {
		final List<String> attributes = List.of("id");
		ScanEnhancedRequest request = ScanEnhancedRequest.builder()
			.attributesToProject(attributes)
			.build();

		matchRepository.listWithAttributes(attributes);

		verify(mockDynamoDbTable, times(1)).scan(request);
	}

	@Test
	void publicListWithAttributes() {
		final List<String> attributes = List.of("id");

		ScanEnhancedRequest request = ScanEnhancedRequest.builder()
			.attributesToProject(attributes)
			.filterExpression(Expression.builder()
				.expression("attribute_not_exists (eventType) OR eventType = :eventType")
				.putExpressionValue(":eventType", stringValue("GAME"))
				.build())
			.build();

		matchRepository.publicListWithAttributes(attributes);

		verify(mockDynamoDbTable, times(1)).scan(request);
	}

	@Test
	void upsertAvailability() {
		Map<String, Availability> playerAvailability = new HashMap<>();
		playerAvailability.put("userSub", Availability.builder().build());

		when(mockDynamoDbTable.getItem(Key.builder().partitionValue("matchId").build()))
			.thenReturn(Match.builder()
				.playerAvailability(playerAvailability)
				.build());

		final Availability availability = new Availability(AvailabilityStatus.AVAILABLE, "comment");
		matchRepository.upsertAvailability("matchId", "userSub", availability);

		verify(mockDynamoDbTable, times(1)).updateItem(Match.builder()
			.playerAvailability(
				Map.of("userSub", availability)
			)
			.build());
	}

	@Test
	void addEvent() {
		Match match = Match.builder().id("id").build();

		matchRepository.addEvent(match);

		verify(mockDynamoDbTable, times(1)).putItem(match);
	}

	@Test
	void updateEvent() {
		Match match = Match.builder().id("id").build();

		matchRepository.updateEvent(match);

		UpdateItemEnhancedRequest<Match> expectedRequest = UpdateItemEnhancedRequest.builder(Match.class)
			.item(match)
			.ignoreNulls(true)
			.build();

		verify(mockDynamoDbTable, times(1)).updateItem(expectedRequest);
	}

	@Test
	void deleteEvent() {
		matchRepository.deleteEvent("eventId");

		verify(mockDynamoDbTable, times(1)).deleteItem(Key.builder().partitionValue("eventId").build());
	}

	@Test
	void completeMatch() {
		Match matchInDb = Match.builder().id("id").eventType("GAME").build();
		when(mockDynamoDbTable.getItem(Key.builder().partitionValue("id").build())).thenReturn(matchInDb);

		Match match = Match.builder().id("id").build();

		matchRepository.completeMatch(match);

		UpdateItemEnhancedRequest<Match> expectedRequest = UpdateItemEnhancedRequest.builder(Match.class)
			.item(match)
			.ignoreNulls(true)
			.build();

		verify(mockDynamoDbTable, times(1)).updateItem(expectedRequest);
	}

	@Test
	void completeMatchWrongType() {
		Match matchInDb = Match.builder().id("id").eventType("TRAINING").build();
		when(mockDynamoDbTable.getItem(Key.builder().partitionValue("id").build())).thenReturn(matchInDb);

		Match match = Match.builder().id("id").build();

		assertThrows(IllegalArgumentException.class, () -> matchRepository.completeMatch(match));

		verifyNoMoreInteractions(mockDynamoDbTable);
	}
}