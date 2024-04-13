package uk.co.withingtonhopecf.hopefulapi.repository;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
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
		when(dynamoDbEnhancedClient.table("tableName", TableSchema.fromImmutableClass(Match.class))).thenReturn(mockDynamoDbTable);
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
}