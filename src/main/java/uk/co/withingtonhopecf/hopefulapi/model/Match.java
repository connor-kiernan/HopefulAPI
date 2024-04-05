package uk.co.withingtonhopecf.hopefulapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import uk.co.withingtonhopecf.hopefulapi.converter.PitchTypeConverter;
import uk.co.withingtonhopecf.hopefulapi.converter.ZonedDateTimeConverter;
import uk.co.withingtonhopecf.hopefulapi.model.enums.PitchType;

@Value
@Builder
@JsonInclude(Include.NON_NULL)
@DynamoDbImmutable(builder = Match.MatchBuilder.class)
public class Match {

	@Getter(onMethod_ = @DynamoDbPartitionKey)
	String id;

	@Getter(onMethod_ = @DynamoDbConvertedBy(ZonedDateTimeConverter.class))
	ZonedDateTime kickOffDateTime;

	@Getter(onMethod_ = @DynamoDbConvertedBy(PitchTypeConverter.class))
	PitchType pitchType;

	String opponent;
	Map<String, String> address;
	Boolean played;
	Boolean isHomeGame;
	Boolean isHomeKit;

	@Builder.Default
	int homeGoals = -1;

	@Builder.Default
	int awayGoals = -1;

	Map<String, Integer> withyGoalScorers;

}