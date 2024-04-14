package uk.co.withingtonhopecf.hopefulapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import uk.co.withingtonhopecf.hopefulapi.model.Availability;
import uk.co.withingtonhopecf.hopefulapi.model.AvailabilityUpdateRequest;
import uk.co.withingtonhopecf.hopefulapi.model.Match;
import uk.co.withingtonhopecf.hopefulapi.model.enums.AvailabilityStatus;
import uk.co.withingtonhopecf.hopefulapi.model.enums.PitchType;
import uk.co.withingtonhopecf.hopefulapi.repository.MatchRepository;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

	@Mock
	private MatchRepository matchRepository;

	@InjectMocks
	private MatchService matchService;

	@Mock
	private PageIterable<Match> mockPageIterable;

	@Test
	void getMatchesPublic() {
		when(matchRepository.publicListWithAttributes(List.of("id", "kickOffDateTime", "opponent", "address", "played", "isHomeGame","homeGoals", "awayGoals", "withyGoalScorers")))
			.thenReturn(mockPageIterable);

		Match match = Match.builder()
			.id("id123")
			.address(Map.of("addressLine1", "123 Fake Street"))
			.kickOffDateTime(ZonedDateTime.ofInstant(Instant.ofEpochSecond(123), ZoneId.of("Europe/London")))
			.opponent("Everton FC")
			.played(false)
			.isHomeGame(true)
			.build();

		when(mockPageIterable.stream()).thenReturn(Stream.of(Page.builder(Match.class).items(List.of(match)).build()));

		List<Match> actualMatches = matchService.getMatchesPublic();

		assertEquals(List.of(match), actualMatches);
	}

	@Test
	void getMatchesForAvailabilityTest() {
		when(matchRepository.listWithAttributes(List.of("id", "kickOffDateTime", "opponent", "address", "played", "isHomeGame","isHomeKit", "pitchType", "playerAvailability")))
			.thenReturn(mockPageIterable);

		Match match = Match.builder()
			.id("id123")
			.address(Map.of("addressLine1", "123 Fake Street"))
			.kickOffDateTime(ZonedDateTime.ofInstant(Instant.ofEpochSecond(123), ZoneId.of("Europe/London")))
			.opponent("Everton FC")
			.played(false)
			.isHomeGame(true)
			.isHomeKit(true)
			.pitchType(PitchType.GRASS)
			.playerAvailability(Map.of("connor.kiernan", Availability.builder()
				.status(AvailabilityStatus.AVAILABLE)
				.comment("Comment")
				.build()))
			.build();

		when(mockPageIterable.stream()).thenReturn(Stream.of(Page.builder(Match.class).items(List.of(match)).build()));

		List<Match> actualMatches = matchService.getMatchesForAvailability();

		assertEquals(List.of(match), actualMatches);
	}

	@Test
	void upsertAvailability() {
		final Availability availability = new Availability(AvailabilityStatus.AVAILABLE, "comment");
		matchService.upsertAvailability(new AvailabilityUpdateRequest("userSub", "matchId", availability));

		verify(matchRepository, times(1)).upsertAvailability("matchId", "userSub", availability );
	}
}