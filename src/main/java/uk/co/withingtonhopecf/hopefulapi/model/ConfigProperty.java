package uk.co.withingtonhopecf.hopefulapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Value
@Builder
@JsonInclude(Include.NON_NULL)
@DynamoDbImmutable(builder = ConfigProperty.ConfigPropertyBuilder.class)
public class ConfigProperty {

	@Getter(onMethod_ = @DynamoDbPartitionKey)
	@NotBlank
	String id;

	@NotBlank
	String value;

}
