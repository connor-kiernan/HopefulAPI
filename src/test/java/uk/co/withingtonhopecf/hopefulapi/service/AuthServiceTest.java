package uk.co.withingtonhopecf.hopefulapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import uk.co.withingtonhopecf.hopefulapi.config.HopefulApiConfigurationProperties;
import uk.co.withingtonhopecf.hopefulapi.model.auth.AuthCodeRequest;
import uk.co.withingtonhopecf.hopefulapi.model.auth.Tokens;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private RestClient restClient;

	@Mock
	private HopefulApiConfigurationProperties config;

	@InjectMocks
	private AuthService authService;

	@Test
	void getTokensFromAuthCode() {
		when(config.tokenUrl()).thenReturn("http://localhost");
		when(config.clientId()).thenReturn("clientId");

		final Tokens tokens = new Tokens("accessToken", "refreshToken");
		when(
			restClient.post()
				.uri("http://localhost/oauth2/token")
				.body("grant_type=authorization_code&client_id=clientId&redirect_uri=http%3A%2F%2Flocalhost%3A3000&code=authCode")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.retrieve()
				.toEntity(Tokens.class)
				.getBody())
			.thenReturn(tokens);

		final Tokens actualTokens = authService.getTokensFromAuthCode(new AuthCodeRequest("authCode", "http://localhost:3000"));

		final Tokens expectedToken = new Tokens("accessToken", "refreshToken");

		assertEquals(expectedToken, actualTokens);
	}

	@Test
	void getAccessTokenFromRefreshToken() {
		when(config.tokenUrl()).thenReturn("http://localhost");
		when(config.clientId()).thenReturn("clientId");

		final Tokens tokens = new Tokens("accessToken", "refreshToken");
		when(
			restClient.post()
				.uri("http://localhost/oauth2/token")
				.body("grant_type=refresh_token&client_id=clientId&refresh_token=refreshToken")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.retrieve()
				.toEntity(Tokens.class)
				.getBody())
			.thenReturn(tokens);

		final String actualToken = authService.getAccessTokenFromRefreshToken("refreshToken");

		final String expectedToken = "accessToken";

		assertEquals(expectedToken, actualToken);
	}
}