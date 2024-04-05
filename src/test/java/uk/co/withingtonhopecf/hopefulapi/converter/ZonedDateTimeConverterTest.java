package uk.co.withingtonhopecf.hopefulapi.converter;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

class ZonedDateTimeConverterTest {

	private final ZonedDateTimeConverter zonedDateTimeConverter = new ZonedDateTimeConverter();

	@Test
	void transformFrom() {
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(14062002), ZoneId.of("Europe/London"));
		AttributeValue attributeValue = zonedDateTimeConverter.transformFrom(zonedDateTime);

		assertEquals(AttributeValue.fromS("14062002"), attributeValue);
	}

	@Test
	void transformTo() {
		ZonedDateTime actualZoneDateTime = zonedDateTimeConverter.transformTo(AttributeValue.fromS("14062002"));
		ZonedDateTime expectedZonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(14062002), ZoneId.of("Europe/London"));

		assertEquals(expectedZonedDateTime, actualZoneDateTime);
	}
}