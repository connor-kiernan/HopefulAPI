package uk.co.withingtonhopecf.hopefulapi.controller;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.withingtonhopecf.hopefulapi.exception.WrongUserException;
import uk.co.withingtonhopecf.hopefulapi.model.AvailabilityUpdateRequest;
import uk.co.withingtonhopecf.hopefulapi.model.Match;
import uk.co.withingtonhopecf.hopefulapi.model.auth.AddEventRequest;
import uk.co.withingtonhopecf.hopefulapi.service.MatchService;

@Slf4j
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

	@PatchMapping("/availability")
	public void updateAvailability(@RequestBody AvailabilityUpdateRequest availabilityUpdateRequest, Principal principal) {
		if (!availabilityUpdateRequest.userSub().equals(principal.getName())) {
			throw new WrongUserException();
		}

		matchService.upsertAvailability(availabilityUpdateRequest);
	}

	@PostMapping("/addEvent")
	public void addEvent(@RequestBody AddEventRequest addEventRequest, Principal principal) {
		log.info("User: {}, adding event: {}", principal.getName(), addEventRequest);
		matchService.addEvent(addEventRequest);
	}
}
