package uk.co.withingtonhopecf.hopefulapi.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.withingtonhopecf.hopefulapi.model.Player;
import uk.co.withingtonhopecf.hopefulapi.service.PlayerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayerController {

	private final PlayerService playerService;

	@GetMapping
	public List<Player> listAllUsers() {
		return playerService.listAllPlayers();
	}
}
