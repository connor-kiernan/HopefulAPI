package uk.co.withingtonhopecf.hopefulapi.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import uk.co.withingtonhopecf.hopefulapi.config.HopefulApiConfigurationProperties;
import uk.co.withingtonhopecf.hopefulapi.model.Match;

@Repository
@RequiredArgsConstructor
public class MatchRepository {

	private final HopefulApiConfigurationProperties config;
	private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

	public PageIterable<Match> listWithAttributes(List<String> attributes) {
		ScanEnhancedRequest request = ScanEnhancedRequest.builder()
			.attributesToProject(attributes)
			.build();

		return getTable().scan(request);
	}

	private DynamoDbTable<Match> getTable() {
		return dynamoDbEnhancedClient.table(config.matchesTableName(), TableSchema.fromImmutableClass(Match.class));
	}
}
