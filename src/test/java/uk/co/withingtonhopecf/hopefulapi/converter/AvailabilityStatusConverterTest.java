package uk.co.withingtonhopecf.hopefulapi.converter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import uk.co.withingtonhopecf.hopefulapi.model.enums.AvailabilityStatus;

class AvailabilityStatusConverterTest {
	
	private final AvailabilityStatusConverter availabilityStatusConverter = new AvailabilityStatusConverter();

	@ParameterizedTest
	@EnumSource(AvailabilityStatus.class)
	void transformFrom(AvailabilityStatus availabilityStatus) {
		AttributeValue attributeValue = availabilityStatusConverter.transformFrom(availabilityStatus);

		assertEquals(AttributeValue.fromS(availabilityStatus.toString()), attributeValue);
	}

	@ParameterizedTest
	@EnumSource(AvailabilityStatus.class)
	void transformTo(AvailabilityStatus availabilityStatus) {
		AvailabilityStatus actualAvailabilityStatus = availabilityStatusConverter.transformTo(AttributeValue.fromS(availabilityStatus.toString()));

		assertEquals(availabilityStatus, actualAvailabilityStatus);
	}
}