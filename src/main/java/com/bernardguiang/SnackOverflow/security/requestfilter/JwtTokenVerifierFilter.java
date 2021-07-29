package com.bernardguiang.SnackOverflow.security.requestfilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bernardguiang.SnackOverflow.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JwtTokenVerifierFilter extends OncePerRequestFilter{
	
	private final JwtService jwtService;
	
	public JwtTokenVerifierFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}
	
	public static String getBody(HttpServletRequest request) throws IOException {
	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = stringBuilder.toString();
	    return body;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// Get Token From Request Header
		String authorizationHeader = request.getHeader("Authorization");
		
		// Validate
		
		// ... check if token exists
		if(	authorizationHeader == null 
				|| authorizationHeader.isEmpty() 
				|| !authorizationHeader.startsWith("Bearer ")) 
		{
			// We don't reject here for the case where the User is getting his/her username and password authenticated to get a token back (login)
			
			// Pass on request and response to the next filter and initiate
			System.out.println("Verifying JWT: AUTH IS NULL OR EMPTY");
			filterChain.doFilter(request, response);
			return;
		}
		
		// JWS is just a signed JWT
		String token = authorizationHeader.replace("Bearer ",  "");
		try {
			Claims tokenPayload = jwtService.getTokenPayload(token);
			
			String username = tokenPayload.getSubject();
			// Get Authorities from the body
			List<Map<String, String>> authorities = (List<Map<String, String>>) tokenPayload.get("authorities");
			// Convert Authorities to SimpleGrantedAuthorities
			Set<SimpleGrantedAuthority> simpleGrantedAuthorities = 
					authorities.stream()
					.map(authMap -> new SimpleGrantedAuthority(authMap.get("authority")))
					.collect(Collectors.toSet());
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					username,
					null,
					simpleGrantedAuthorities
			);
			
			// IF no errors are thrown when parsing the token claims, then the JWT is valid
			// Set this request as authenticated
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// Pass on request and response to the next filter and initiate
			filterChain.doFilter(request, response);
			return;
			
		} catch(JwtException e) {
			throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
		}
	}

}
