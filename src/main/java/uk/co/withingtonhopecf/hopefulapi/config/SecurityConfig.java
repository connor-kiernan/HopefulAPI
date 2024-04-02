package uk.co.withingtonhopecf.hopefulapi.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
				configuration.setAllowedMethods(List.of("GET"));

				return configuration;
			}))
			.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
				authorizationManagerRequestMatcherRegistry
					.requestMatchers("/players").permitAll()
					.anyRequest().authenticated()
			).oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
			.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(List.of("GET"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}



