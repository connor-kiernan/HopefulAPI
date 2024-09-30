package uk.co.withingtonhopecf.hopefulapi.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.withingtonhopecf.hopefulapi.service.ConfigService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/config")
public class ConfigController {

	private final ConfigService configService;

	@GetMapping
	public Map<String, Object> getConfig() {
		return Map.of(
			"carousel", configService.getCarouselImages(),
			"tweet", configService.getHighlightTweet()
		);
	}

}
