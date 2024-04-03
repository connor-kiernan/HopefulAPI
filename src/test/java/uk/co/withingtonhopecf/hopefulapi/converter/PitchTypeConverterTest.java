package uk.co.withingtonhopecf.hopefulapi.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import uk.co.withingtonhopecf.hopefulapi.model.enums.PitchType;

class PitchTypeConverterTest {

	private final PitchTypeConverter pitchTypeConverter = new PitchTypeConverter();

	@ParameterizedTest
	@EnumSource(PitchType.class)
	void transformFrom(PitchType pitchType) {
		AttributeValue attributeValue = pitchTypeConverter.transformFrom(pitchType);

		assertEquals(AttributeValue.fromS(pitchType.toString()), attributeValue);
	}

	@ParameterizedTest
	@EnumSource(PitchType.class)
	void transformTo(PitchType pitchType) {
		PitchType actualPitchType = pitchTypeConverter.transformTo(AttributeValue.fromS(pitchType.toString()));

		assertEquals(pitchType, actualPitchType);
	}
}