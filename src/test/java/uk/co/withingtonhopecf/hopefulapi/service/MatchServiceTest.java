package uk.co.withingtonhopecf.hopefulapi.service;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.withingtonhopecf.hopefulapi.model.enums.PitchType.GRASS;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import uk.co.withingtonhopecf.hopefulapi.model.Availability;
import uk.co.withingtonhopecf.hopefulapi.model.request.AvailabilityUpdateRequest;
import uk.co.withingtonhopecf.hopefulapi.model.Match;
import uk.co.withingtonhopecf.hopefulapi.model.request.AddEventRequest;
import uk.co.withingtonhopecf.hopefulapi.model.enums.AvailabilityStatus;
import uk.co.withingtonhopecf.hopefulapi.model.request.EditEventRequest;
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
		when(matchRepository.publicListWithAttributes(
			List.of("id", "kickOffDateTime", "opponent", "address", "played", "isHomeGame", "homeGoals", "awayGoals",
				"withyGoalScorers")))
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
		when(matchRepository.listWithAttributes(
			List.of("id", "kickOffDateTime", "opponent", "address", "played", "isHomeGame", "isHomeKit", "pitchType",
				"eventType", "playerAvailability")))
			.thenReturn(mockPageIterable);

		Match match = Match.builder()
			.id("id123")
			.address(Map.of("addressLine1", "123 Fake Street"))
			.kickOffDateTime(ZonedDateTime.ofInstant(Instant.ofEpochSecond(123), ZoneId.of("Europe/London")))
			.opponent("Everton FC")
			.played(false)
			.isHomeGame(true)
			.isHomeKit(true)
			.pitchType(GRASS)
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

		verify(matchRepository, times(1)).upsertAvailability("matchId", "userSub", availability);
	}

	@ParameterizedTest
	@CsvSource({"GAME, gme", "TRAINING, trn", "Social, evn"})
	void addEventTest(String eventType, String idPrefix) {
		final AddEventRequest addEventRequest = new AddEventRequest(
			"Opponent Name",
			"2024-06-14T10:15",
			GRASS,
			"123 Fake Street",
			"Fake Area",
			"A12 3BC",
			true,
			true,
			eventType
		);

		Instant instant = Instant.ofEpochSecond(123456789L);
		MockedStatic<Instant> mockStaticInstant = mockStatic(Instant.class, CALLS_REAL_METHODS);
		try (mockStaticInstant) {
			mockStaticInstant.when(Instant::now).thenReturn(instant);

			matchService.addEvent(addEventRequest);
		}

		Match expectedMatch = Match.builder()
			.id(idPrefix + "123456789")
			.kickOffDateTime(ZonedDateTime.ofInstant(Instant.ofEpochSecond(1718356500), ZoneId.of("Europe/London")))
			.pitchType(GRASS)
			.opponent("Opponent Name")
			.address(Map.of(
				"line1", "123 Fake Street",
				"line2", "Fake Area",
				"postcode", "A12 3BC"
			))
			.isHomeGame(true)
			.isHomeKit(true)
			.eventType(eventType)
			.playerAvailability(emptyMap())
			.build();

		verify(matchRepository, times(1)).addEvent(expectedMatch);
	}

	@Test
	void addEventTestNoLine2() {
		final AddEventRequest addEventRequest = AddEventRequest.builder()
			.opponent("Opponent Name")
			.kickOffDateTime("2024-06-14T10:15")
			.pitchType(GRASS)
			.address1("123 Fake Street")
			.postcode("A12 3BC")
			.isHomeGame(true)
			.isHomeKit(true)
			.eventType("GAME")
			.build();

		Instant instant = Instant.ofEpochSecond(123456789L);
		MockedStatic<Instant> mockStaticInstant = mockStatic(Instant.class, CALLS_REAL_METHODS);
		try (mockStaticInstant) {
			mockStaticInstant.when(Instant::now).thenReturn(instant);

			matchService.addEvent(addEventRequest);
		}

		Match expectedMatch = Match.builder()
			.id("gme123456789")
			.kickOffDateTime(ZonedDateTime.ofInstant(Instant.ofEpochSecond(1718356500), ZoneId.of("Europe/London")))
			.pitchType(GRASS)
			.opponent("Opponent Name")
			.address(Map.of(
				"line1", "123 Fake Street",
				"postcode", "A12 3BC"
			))
			.isHomeGame(true)
			.isHomeKit(true)
			.eventType("GAME")
			.playerAvailability(emptyMap())
			.build();

		verify(matchRepository, times(1)).addEvent(expectedMatch);
	}

	@Test
	void editEventTest() {
		final EditEventRequest editEventRequest = EditEventRequest.builder()
			.id("gme1")
			.opponent("Opponent Name")
			.kickOffDateTime("2024-06-14T10:15")
			.pitchType(GRASS)
			.address1("123 Fake Street")
			.postcode("A12 3BC")
			.isHomeGame(true)
			.isHomeKit(true)
			.build();

		matchService.editEvent(editEventRequest);

		Match expectedMatch = Match.builder()
			.id("gme1")
			.kickOffDateTime(ZonedDateTime.ofInstant(Instant.ofEpochSecond(1718356500), ZoneId.of("Europe/London")))
			.pitchType(GRASS)
			.opponent("Opponent Name")
			.address(Map.of(
				"line1", "123 Fake Street",
				"postcode", "A12 3BC"
			))
			.isHomeGame(true)
			.isHomeKit(true)
			.build();

		verify(matchRepository, times(1)).updateEvent(expectedMatch);
	}
}