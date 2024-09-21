package uk.co.withingtonhopecf.hopefulapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import uk.co.withingtonhopecf.hopefulapi.model.CarouselImage;
import uk.co.withingtonhopecf.hopefulapi.repository.ConfigRepository;

@Service
@RequiredArgsConstructor
public class ConfigService {

	private final ConfigRepository configRepository;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@SneakyThrows(JsonProcessingException.class)
	public Map<Integer, CarouselImage> getCarouselImages() {
		return objectMapper.readValue(configRepository.getCarouselImages().getValue(), new TypeReference<>() {});
	}

	public String getHighlightTweet() {
		return configRepository.getHighlightTweet().getValue();
	}

}
