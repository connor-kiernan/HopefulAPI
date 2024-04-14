package uk.co.withingtonhopecf.hopefulapi.repository;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import uk.co.withingtonhopecf.hopefulapi.config.HopefulApiConfigurationProperties;
import uk.co.withingtonhopecf.hopefulapi.model.Match;

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
	void publicListWithAttributes() {
		final List<String> attributes = List.of("id");

		ScanEnhancedRequest request = ScanEnhancedRequest.builder()
			.attributesToProject(attributes)
			.filterExpression(Expression.builder().expression("attribute_not_exists (eventType) OR eventType = GAME").build())
			.build();

		matchRepository.publicListWithAttributes(attributes);

		verify(mockDynamoDbTable, times(1)).scan(request);
	}
}