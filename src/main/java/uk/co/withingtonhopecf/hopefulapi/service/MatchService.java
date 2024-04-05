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

	private static final List<String> PUBLIC_ATTRIBUTES = List.of("id", "kickOffDateTime", "opponent", "address", "played", "isHomeGame", "homeGoals", "awayGoals");

	public List<Match> getMatchesPublic() {
		return matchRepository.listWithAttributes(PUBLIC_ATTRIBUTES).stream()
			.flatMap(page -> page.items().stream())
			.toList();
	}

}
