package uk.co.withingtonhopecf.hopefulapi.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.withingtonhopecf.hopefulapi.model.auth.AuthCodeRequest;
import uk.co.withingtonhopecf.hopefulapi.model.auth.AuthResponse;
import uk.co.withingtonhopecf.hopefulapi.model.auth.Tokens;
import uk.co.withingtonhopecf.hopefulapi.service.AuthService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/code")
	public AuthResponse authenticate(@RequestBody AuthCodeRequest authCodeRequest, HttpServletResponse response) {
		log.info("Received auth code request: {}", authCodeRequest);
		final Tokens tokens = authService.getTokensFromAuthCode(authCodeRequest);

		log.info("Got tokens: {}", tokens);
		final Cookie cookie = new Cookie("refresh", tokens.refreshToken());
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

		return new AuthResponse(tokens.accessToken());
	}

	@GetMapping("/refresh")
	public AuthResponse refresh(@CookieValue("refresh") String refreshToken) {
		log.info("REFRESH TOKEN: {}",  refreshToken);
		String accessTokenFromRefreshToken = authService.getAccessTokenFromRefreshToken(refreshToken);
		log.info("RESPONSE: {}", accessTokenFromRefreshToken);

		return new AuthResponse(accessTokenFromRefreshToken);
	}

	@PostMapping("logout")
	public void logout(HttpServletResponse response) {
		final Cookie cookie = new Cookie("refresh", null);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);

		response.addCookie(cookie);
	}
}
