package com.bernardguiang.SnackOverflow.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bernardguiang.SnackOverflow.dto.AuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// This class validates credentials. Spring already does this but we want to make our own
// Requests go through every Request Filter before reaching the API
// Request Filters do some form of validation before reaching the API
// The order of Request Filters is not guaranteed
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	
	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
		this.authenticationManager = authenticationManager;
		this.jwtProvider = jwtProvider;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {	
		System.out.println("Attempting Authentication");
		try {
			UsernameAndPasswordAuthenticationRequest usernameAndPasswordAuthenticationRequest = 
					new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					usernameAndPasswordAuthenticationRequest.getUsername(),
					usernameAndPasswordAuthenticationRequest.getPassword()
			);
			
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
		
		// Create token payload
		//		{
		//			  "sub": "bernard",
		//			  "authorities": [
		//			    {
		//			      "authority": "product:write"
		//			    },
		//			    {
		//			      "authority": "category:read"
		//			    },
		//			    {
		//			      "authority": "ROLE_ADMIN"
		//			    },
		//			    {
		//			      "authority": "category:write"
		//			    },
		//			    {
		//			      "authority": "product:read"
		//			    }
		//			  ],
		//			  "iat": 1624931480,
		//			  "exp": 1626073200
		//		}
		
		
		// Create and Sign JWT
		
		String token = jwtProvider.generateToken(authResult);
		// TODO: Create Refresh Token and store inside HttpOnly Cookie
		// TODO: Send JWT as response instead of header?
		
		// Add token to response header to return to client
		System.out.println("Authentication Success. Returning Username and JWT Access Token in response. Returning Refresh Cookie");
		System.out.println("Bearer " + token);
		response.addHeader("Authorization", "Bearer " + token);
		
		response.setContentType("application/json");
		AuthenticationResponse responseBody = new AuthenticationResponse(token, authResult.getName());
		Gson gson = new Gson();
		String responseBodyJSONString = gson.toJson(responseBody);
		
		PrintWriter writer = response.getWriter();
	    writer.write(responseBodyJSONString);

		//response.addCookie(cookie);
	}
	
	
	
}
