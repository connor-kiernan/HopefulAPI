package uk.co.withingtonhopecf.hopefulapi.config;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpMethod.GET;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final HopefulApiConfigurationProperties config;

	@Profile("!dev & !docker")
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
			.cors(corsConfigurer -> corsConfigurer.configurationSource(request -> getCorsConfiguration()))
			.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
				authorizationManagerRequestMatcherRegistry
					.requestMatchers("/actuator/**").permitAll()
					.requestMatchers("/auth/*").permitAll()
					.requestMatchers("/players").permitAll()
					.requestMatchers("/matches").permitAll()
					.requestMatchers(GET, "/config").permitAll()
					.requestMatchers("/addEvent").hasAuthority("Admin")
					.requestMatchers("/editEvent").hasAuthority("Admin")
					.requestMatchers("/deleteEvent*").hasAuthority("Admin")
					.requestMatchers("/completeEvent").hasAuthority("Admin")
					.anyRequest().authenticated()
			).oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(grantedAuthoritiesExtractor())))
			.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.build();
	}

	private CorsConfiguration getCorsConfiguration() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(config.frontendUrls());
		configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(List.of("*"));

		return configuration;
	}

	private static JwtAuthenticationConverter grantedAuthoritiesExtractor() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
			List<String> cognitoGroups = jwt.getClaimAsStringList("cognito:groups");

			if (cognitoGroups != null) {
				return cognitoGroups.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toCollection(ArrayList::new));
			}

			return emptyList();
		});

		jwtAuthenticationConverter.setPrincipalClaimName("sub");

		return jwtAuthenticationConverter;
	}

	@Profile({"dev", "docker"})
	@Bean
	public SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
			.cors(corsConfigurer -> corsConfigurer.configurationSource(request -> getCorsConfiguration()))
			.addFilterBefore((request, response, filterChain) -> filter((HttpServletRequest) request, response, filterChain), BasicAuthenticationFilter.class)
			.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
				authorizationManagerRequestMatcherRegistry
					.requestMatchers("/actuator/**").permitAll()
					.requestMatchers("/auth/*").permitAll()
					.requestMatchers("/players").permitAll()
					.requestMatchers("/matches").permitAll()
					.requestMatchers(GET, "/config").permitAll()
					.requestMatchers("/addEvent").hasAuthority("Admin")
					.requestMatchers("/editEvent").hasAuthority("Admin")
					.requestMatchers("/deleteEvent*").hasAuthority("Admin")
					.requestMatchers("/completeEvent").hasAuthority("Admin")
					.anyRequest().authenticated()
			).httpBasic(Customizer.withDefaults())
			.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.build();
	}


	@Profile({"dev", "docker"})
	@Bean
	public InMemoryUserDetailsManager userDetailsManager() {
		UserDetails user = User.withUsername("19")
			.password("{noop}password")
			.authorities(new SimpleGrantedAuthority("Admin"))
			.build();

		return new InMemoryUserDetailsManager(user);
	}

	private static void filter(HttpServletRequest request, ServletResponse response, FilterChain filterChain)
		throws IOException, ServletException {
		HttpServletRequestWrapper modified = new HttpServletRequestWrapper(request) {
			@Override
			public String getHeader(String name) {
				if (name.equalsIgnoreCase("Authorization")) {
					return "Basic MTk6cGFzc3dvcmQ=";
				}

				return super.getHeader(name);
			}
		};

		filterChain.doFilter(modified, response);
	}

}