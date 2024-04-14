package uk.co.withingtonhopecf.hopefulapi.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import uk.co.withingtonhopecf.hopefulapi.model.AvailabilityUpdateRequest;
import uk.co.withingtonhopecf.hopefulapi.model.Match;
import uk.co.withingtonhopecf.hopefulapi.repository.MatchRepository;

@Service
@RequiredArgsConstructor
public class MatchService {

	private final MatchRepository matchRepository;

	private static final List<String> PUBLIC_ATTRIBUTES = List.of("id", "kickOffDateTime", "opponent", "address", "played", "isHomeGame", "homeGoals", "awayGoals", "withyGoalScorers");
	private static final List<String> AVAILABILITY_ATTRIBUTES = List.of("id", "kickOffDateTime", "opponent", "address", "played", "isHomeGame", "isHomeKit", "pitchType", "playerAvailability");

	public List<Match> getMatchesPublic() {
		return flatMapPages(matchRepository.publicListWithAttributes(PUBLIC_ATTRIBUTES));
	}

	public List<Match> getMatchesForAvailability() {
		return flatMapPages(matchRepository.listWithAttributes(AVAILABILITY_ATTRIBUTES));
	}

	public void upsertAvailability(AvailabilityUpdateRequest request) {
		matchRepository.upsertAvailability(request.matchId(), request.userSub(), request.availability());
	}

	private static List<Match> flatMapPages(PageIterable<Match> pages) {
		return pages.stream()
			.flatMap(page -> page.items().stream())
			.toList();
	}

}
