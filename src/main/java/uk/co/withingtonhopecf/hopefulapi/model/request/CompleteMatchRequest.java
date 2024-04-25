package uk.co.withingtonhopecf.hopefulapi.model.request;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record CompleteMatchRequest(
	@NotBlank  String matchId,
	int homeGoals,
	int awayGoals,
	Map<String, Integer> withyGoalScorers
) {}
