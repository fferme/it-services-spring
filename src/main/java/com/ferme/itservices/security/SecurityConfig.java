package com.ferme.itservices.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
private SecurityFilter securityFilter;

	@Bean
	public SecurityFilterChain filters(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//			.authorizeHttpRequests(authorize -> authorize
//				.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
//				.requestMatchers(HttpMethod.GET, "/auth/users").hasRole("Owner")
//				.requestMatchers(HttpMethod.POST, "/auth/users").permitAll()
//				.anyRequest().authenticated()
//			)
//				  .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}