package uk.co.withingtonhopecf.hopefulapi.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final HopefulApiConfigurationProperties config;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
			.cors(corsConfigurer -> corsConfigurer.configurationSource(request -> {
				CorsConfiguration configuration = new CorsConfiguration();
				configuration.setAllowedOrigins(List.of(config.frontendUrl()));
				configuration.setAllowedMethods(List.of("GET", "POST"));
				configuration.setAllowCredentials(true);
				configuration.setAllowedHeaders(List.of("*"));

				return configuration;
			}))
			.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
				authorizationManagerRequestMatcherRegistry
					.requestMatchers("/auth/*").permitAll()
					.requestMatchers("/players").permitAll()
					.requestMatchers("/matches").permitAll()
					.anyRequest().authenticated()
			).oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(grantedAuthoritiesExtractor())))
			.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.build();
	}

	private JwtAuthenticationConverter grantedAuthoritiesExtractor() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
			List<String> l = jwt.getClaimAsStringList("cognito:groups");

			return l.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toCollection(ArrayList::new));
		});

		jwtAuthenticationConverter.setPrincipalClaimName("cognito:username");

		return jwtAuthenticationConverter;
	}

}