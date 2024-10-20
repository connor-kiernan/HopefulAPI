package uk.co.withingtonhopecf.hopefulapi.service;

import static java.util.Collections.emptyMap;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import uk.co.withingtonhopecf.hopefulapi.model.request.AvailabilityUpdateRequest;
import uk.co.withingtonhopecf.hopefulapi.model.request.CompleteMatchRequest;
import uk.co.withingtonhopecf.hopefulapi.model.request.EditEventRequest;
import uk.co.withingtonhopecf.hopefulapi.model.Match;
import uk.co.withingtonhopecf.hopefulapi.model.request.AddEventRequest;
import uk.co.withingtonhopecf.hopefulapi.repository.MatchRepository;

@Service
@RequiredArgsConstructor
public class MatchService {

	private final MatchRepository matchRepository;

	private static final List<String> PUBLIC_ATTRIBUTES = List.of("id", "kickOffDateTime", "opponent", "address", "played", "isHomeGame", "homeGoals", "awayGoals", "withyGoalScorers", "season");
	private static final List<String> AVAILABILITY_ATTRIBUTES = List.of("id", "kickOffDateTime", "opponent", "address", "played", "isHomeGame", "isHomeKit", "pitchType", "eventType", "playerAvailability");

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

	public void editEvent(EditEventRequest editEventRequest) {
		Map<String, String> address = createAddress(editEventRequest.address1(), editEventRequest.postcode(), editEventRequest.address2());

		final ZonedDateTime kickOffDateTime = resolveKickOffTime(editEventRequest.kickOffDateTime());
		Match match = Match.builder()
			.id(editEventRequest.id())
			.opponent(editEventRequest.opponent())
			.address(address)
			.kickOffDateTime(kickOffDateTime)
			.pitchType(editEventRequest.pitchType())
			.isHomeGame(editEventRequest.isHomeGame())
			.isHomeKit(editEventRequest.isHomeKit())
			.season(resolveSeason(kickOffDateTime))
			.build();

		matchRepository.updateEvent(match);
	}

	public void addEvent(AddEventRequest addEventRequest) {
		String idPrefix = switch (addEventRequest.eventType()) {
			case "GAME" -> "gme";
			case "TRAINING" -> "trn";
			default -> "evn";
		};

		Map<String, String> address = createAddress(addEventRequest.address1(), addEventRequest.postcode(), addEventRequest.address2());

		final ZonedDateTime kickOffDateTime = resolveKickOffTime(addEventRequest.kickOffDateTime());
		Match match = Match.builder()
			.id(idPrefix + Instant.now().getEpochSecond())
			.opponent(addEventRequest.opponent())
			.address(address)
			.kickOffDateTime(kickOffDateTime)
			.pitchType(addEventRequest.pitchType())
			.isHomeGame(addEventRequest.isHomeGame())
			.isHomeKit(addEventRequest.isHomeKit())
			.playerAvailability(emptyMap())
			.eventType(addEventRequest.eventType())
			.season(resolveSeason(kickOffDateTime))
			.build();

		matchRepository.addEvent(match);
	}

	public void deleteEvent(String eventId) {
		matchRepository.deleteEvent(eventId);
	}

	public void completeMatch(CompleteMatchRequest completeMatchRequest) {
		Match match = Match.builder()
			.id(completeMatchRequest.id())
			.homeGoals(completeMatchRequest.homeGoals())
			.awayGoals(completeMatchRequest.awayGoals())
			.played(true)
			.withyGoalScorers(completeMatchRequest.withyGoalScorers())
			.build();

		matchRepository.completeMatch(match);
	}

	private static Map<String, String> createAddress(String line1, String postcode, String line2) {
		Map<String, String> address = new HashMap<>();
		address.put("line1", line1);
		address.put("postcode", postcode);

		if (line2 !=  null) {
			address.put("line2", line2);
		}

		return address;
	}

	private static ZonedDateTime resolveKickOffTime(String kickOffTime) {
		LocalDateTime localDateTime = LocalDateTime.parse(kickOffTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

		return ZonedDateTime.of(localDateTime, ZoneId.of("Europe/London"));
	}

	private static String resolveSeason(ZonedDateTime kickOffDateTime) {
		int year = kickOffDateTime.getYear();
		final LocalDate seasonBorder = LocalDate.of(year, 7, 1);

		if (kickOffDateTime.isBefore(seasonBorder.atStartOfDay(ZoneId.of("Europe/London")))) {
			return "%d/%d".formatted(year - 1, year % 100);
		}

		return "%d/%d".formatted(year, (year + 1) % 100);
	}
}
