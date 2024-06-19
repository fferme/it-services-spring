package com.ferme.itservices.security.infra.security;

import com.ferme.itservices.security.auditing.services.ApplicationAuditAware;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final SecurityFilter securityFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
		MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

		http
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers(mvcMatcherBuilder.pattern("/auth/login")).permitAll()
				.requestMatchers(mvcMatcherBuilder.pattern("/auth/**")).permitAll()
				.requestMatchers(mvcMatcherBuilder.pattern("/api/**")).hasRole("ADMIN")

            .requestMatchers(mvcMatcherBuilder.pattern("/v3/api-docs/**")).permitAll()
            .requestMatchers(mvcMatcherBuilder.pattern("/swagger-ui/**")).permitAll()
				.anyRequest().authenticated()
		).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean()
	public AuditorAware<String> auditorAware() {
		return new ApplicationAuditAware();
	}
}
