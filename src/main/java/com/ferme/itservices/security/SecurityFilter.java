package com.ferme.itservices.security;

import com.ferme.itservices.security.repositories.UserRepository;
import com.ferme.itservices.security.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class SecurityFilter extends OncePerRequestFilter {
	private TokenService tokenService;
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		var token = this.retrieveToken(request);
		if(token != null){
			var login = tokenService.validateToken(token);
			UserDetails user = userRepository.findByUsername(login);

			var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

	private String retrieveToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		return (authHeader == null)
				  ? null
				  : authHeader.replace("Bearer ", "");
	}
}
