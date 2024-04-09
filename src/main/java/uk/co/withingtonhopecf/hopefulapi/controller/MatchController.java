package uk.co.withingtonhopecf.hopefulapi.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.withingtonhopecf.hopefulapi.model.Match;
import uk.co.withingtonhopecf.hopefulapi.service.MatchService;

@RestController
@RequiredArgsConstructor
public class MatchController {

	private final MatchService matchService;

	@GetMapping("/matches")
	public List<Match> getMatches() {
		return matchService.getMatchesPublic();
	}

	@GetMapping("/matchesForAvailability")
	public List<Match> getMatchesForAvailability() {
		return matchService.getMatchesForAvailability();
	}
}
