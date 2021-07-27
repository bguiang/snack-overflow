package com.bernardguiang.SnackOverflow.security.requestfilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bernardguiang.SnackOverflow.dto.request.UsernameAndPasswordAuthenticationRequest;
import com.bernardguiang.SnackOverflow.dto.response.AuthenticationResponse;
import com.bernardguiang.SnackOverflow.service.AuthService;
import com.bernardguiang.SnackOverflow.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

// This class validates credentials. Spring already does this but we want to make our own
// Requests go through every Request Filter before reaching the API
// Request Filters do some form of validation before reaching the API
// The order of Request Filters is not guaranteed
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final AuthService authService;
	
	public JwtUsernameAndPasswordAuthenticationFilter(
			AuthenticationManager authenticationManager, 
			JwtService jwtService, AuthService authService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.authService = authService;
		
		setFilterProcessesUrl("/api/v1/auth/login"); // modify login url
	}
	
	public Authentication getAuthentication(
			@Valid UsernameAndPasswordAuthenticationRequest usernameAndPasswordAuthenticationRequest) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(
		usernameAndPasswordAuthenticationRequest.getUsername(),
		usernameAndPasswordAuthenticationRequest.getPassword());
		
		return authentication;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {	
		System.out.println("Attempting Authentication");
		try {
			UsernameAndPasswordAuthenticationRequest usernameAndPasswordAuthenticationRequest = 
					new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
			Authentication authentication = getAuthentication(usernameAndPasswordAuthenticationRequest);
			
			Authentication authenticated =  authenticationManager.authenticate(authentication);
			
			return authenticated;
		} 
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	// This method is called on successful authentication attempt
	// Creates and adds token to the response header to send back to the client
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		// Create and Sign JWT
		Instant iat = Instant.now();
		String token = jwtService.generateJwt(authResult.getName(), authResult.getAuthorities(), iat);
		
		// Create Refresh Token and store inside HttpOnly Cookie
		Cookie refreshCookie = authService.generateRefreshTokenCookie(authResult.getName());
		response.addCookie(refreshCookie);
		
		// Add token to response header to return to client
		// response.addHeader("Authorization", "Bearer " + token);
		
		response.setContentType("application/json");
		AuthenticationResponse responseBody = new AuthenticationResponse(token, authResult.getName());
		Gson gson = new Gson();
		String responseBodyJSONString = gson.toJson(responseBody);
		PrintWriter writer = response.getWriter();
	    writer.write(responseBodyJSONString);
	}
	
	
	
}
