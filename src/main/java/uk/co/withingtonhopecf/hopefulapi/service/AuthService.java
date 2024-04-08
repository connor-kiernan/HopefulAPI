package uk.co.withingtonhopecf.hopefulapi.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import uk.co.withingtonhopecf.hopefulapi.config.HopefulApiConfigurationProperties;
import uk.co.withingtonhopecf.hopefulapi.model.auth.AuthCodeRequest;
import uk.co.withingtonhopecf.hopefulapi.model.auth.Tokens;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final RestClient restClient;
	private final HopefulApiConfigurationProperties config;

	public Tokens getTokensFromAuthCode(AuthCodeRequest authCodeRequest) {
		final String uri = "%s/oauth2/token".formatted(config.tokenUrl());
		final String body = "grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s"
			.formatted(config.clientId(),
				URLEncoder.encode(authCodeRequest.redirectUri(), StandardCharsets.UTF_8),
				authCodeRequest.authCode());

		return restClient.post()
			.uri(uri)
			.body(body)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.retrieve()
			.toEntity(Tokens.class)
			.getBody();
	}

	public String getAccessTokenFromRefreshToken(String refreshToken) {
		final String uri = "%s/oauth2/token".formatted(config.tokenUrl());
		final String body = "grant_type=refresh_token&client_id=56aos06b5upnvkhho12k6lcfa6&refresh_token=%s"
			.formatted(refreshToken);

		final Tokens response = restClient.post()
			.uri(uri)
			.body(body)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.retrieve()
			.toEntity(Tokens.class)
			.getBody();

		return response == null ? null : response.accessToken();
	}
}
