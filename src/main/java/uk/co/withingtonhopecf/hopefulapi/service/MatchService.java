package uk.co.withingtonhopecf.hopefulapi.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.co.withingtonhopecf.hopefulapi.model.Match;
import uk.co.withingtonhopecf.hopefulapi.repository.MatchRepository;

@Service
@RequiredArgsConstructor
public class MatchService {

	private final MatchRepository matchRepository;

	private static final List<String> PUBLIC_ATTRIBUTES = List.of("id", "kickOffDateTime", "opponent", "address", "played", "isHomeGame", "homeGoals", "awayGoals", "withyGoalScorers");
	private static final List<String> AVAILABILITY_ATTRIBUTES = List.of("id", "kickOffDateTime", "opponent", "address", "played", "isHomeGame", "isHomeKit", "pitchType", "playerAvailability");

	public List<Match> getMatchesPublic() {
		return getMatchesWithList(PUBLIC_ATTRIBUTES);
	}

	public List<Match> getMatchesForAvailability() {
		return getMatchesWithList(AVAILABILITY_ATTRIBUTES);
	}

	private List<Match> getMatchesWithList(List<String> availabilityAttributes) {
		return matchRepository.listWithAttributes(availabilityAttributes).stream()
			.flatMap(page -> page.items().stream())
			.toList();
	}

}
