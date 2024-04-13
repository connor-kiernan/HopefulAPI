package uk.co.withingtonhopecf.hopefulapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import uk.co.withingtonhopecf.hopefulapi.converter.AvailabilityStatusConverter;
import uk.co.withingtonhopecf.hopefulapi.model.enums.AvailabilityStatus;

@Value
@Builder
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@DynamoDbImmutable(builder = Availability.AvailabilityBuilder.class)
public class Availability {

	@Getter(onMethod_ = @DynamoDbConvertedBy(AvailabilityStatusConverter.class))
	AvailabilityStatus status;

	String comment;

}
