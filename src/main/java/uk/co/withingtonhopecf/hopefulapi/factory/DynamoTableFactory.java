package uk.co.withingtonhopecf.hopefulapi.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Component
@RequiredArgsConstructor
public class DynamoTableFactory {

	private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

	public <T> DynamoDbTable<T> getTable(String tableName, Class<T> recordType) {
		return dynamoDbEnhancedClient.table(tableName, TableSchema.fromImmutableClass(recordType));
	}
}
