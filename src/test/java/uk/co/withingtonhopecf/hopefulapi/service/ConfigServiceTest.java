package uk.co.withingtonhopecf.hopefulapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.withingtonhopecf.hopefulapi.model.CarouselImage;
import uk.co.withingtonhopecf.hopefulapi.model.ConfigProperty;
import uk.co.withingtonhopecf.hopefulapi.repository.ConfigRepository;

@ExtendWith(MockitoExtension.class)
class ConfigServiceTest {

	@Mock
	private ConfigRepository configRepository;

	@InjectMocks
	private ConfigService configService;

	@Test
	void getCarouselImages() {
		when(configRepository.getCarouselImages()).thenReturn(ConfigProperty.builder()
			.id("carousel")
			.value("{\"0\":{\"description\":\"Image 1\",\"url\":\"imageOne.jpg\"}}")
			.build()
		);

		Map<Integer, CarouselImage> actualImages = configService.getCarouselImages();

		Map<Integer, CarouselImage> expectedImages = Map.of(
			0, new CarouselImage("imageOne.jpg", "Image 1")
		);

		assertEquals(expectedImages, actualImages);
	}

	@Test
	void getHighlightTweet() {
		when(configRepository.getHighlightTweet()).thenReturn(ConfigProperty.builder()
			.id("tweet")
			.value("12345")
			.build()
		);

		String actual = configService.getHighlightTweet();

		assertEquals("12345", actual);
	}
}