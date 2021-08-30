package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;
import com.bernardguiang.SnackOverflow.security.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.Keys;

class JwtServiceTest {

	private JwtService underTest;
	
	private JwtConfig jwtConfig;
	
	private final String secretKeyString = "shhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";
	private final Long tokenExpiration = 900000L;
	private final SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
	
	@BeforeEach
	void setUp() throws Exception {
		jwtConfig = Mockito.mock(JwtConfig.class);
		underTest = new JwtService(jwtConfig);
	}

	// This test might fail when changes are made to ApplicationUserRoles as the token string has a fixed payload
	@Test
	void itShouldGenerateJwt() {
		// Given
		String username = "customer";
		Instant iat = Instant.ofEpochMilli(1629696048000L);
		Collection<? extends GrantedAuthority> authorities = ApplicationUserRole.CUSTOMER.getGrantedAuthorities();
		
		String tokenExpected = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJjdXN0b21lciIsImF1dGhvcml0aWVzIjpbeyJhdXRob3Jp"
				+ "dHkiOiJST0xFX0NVU1RPTUVSIn0seyJhdXRob3JpdHkiOiJvcmRlcjp3cml0ZSJ9LHsiYXV0aG9yaXR5Ijoib3JkZXI"
				+ "6cmVhZCJ9LHsiYXV0aG9yaXR5IjoiY2F0ZWdvcnk6cmVhZCJ9LHsiYXV0aG9yaXR5IjoicHJvZHVjdDpyZWFkIn1dLC"
				+ "JpYXQiOjE2Mjk2OTYwNDgsImV4cCI6MTYyOTY5Njk0OH0.6EhglTyrgw8wssv2A4ZHLQZANhjhUt5r8a48dvWmX3HNJ"
				+ "rqR96MBoSpcRpYBIeb3";
				
		
		// When
		when(jwtConfig.getTokenExpirationMilliSeconds()).thenReturn(tokenExpiration);
		when(jwtConfig.getSecretKeyForSigning()).thenReturn(secretKey);
		
		String token = underTest.generateJwt(username, authorities, iat);
		
		// Then
		assertEquals(tokenExpected, token);
	}
	
	// This test might fail when changes are made to ApplicationUserRoles as the token string has a fixed payload
	@Test
	void itShouldGetTokenPayload() {
		// Given
		String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJjdXN0b21lciIsImF1dGhvcml0aWVzIjpbeyJhdXRob3Jp"
				+ "dHkiOiJST0xFX0NVU1RPTUVSIn0seyJhdXRob3JpdHkiOiJvcmRlcjp3cml0ZSJ9LHsiYXV0aG9yaXR5Ijoib3JkZXI"
				+ "6cmVhZCJ9LHsiYXV0aG9yaXR5IjoiY2F0ZWdvcnk6cmVhZCJ9LHsiYXV0aG9yaXR5IjoicHJvZHVjdDpyZWFkIn1dLC"
				+ "JpYXQiOjE2Mjk2OTYwNDgsImV4cCI6MTYyOTY5Njk0OH0.6EhglTyrgw8wssv2A4ZHLQZANhjhUt5r8a48dvWmX3HNJ"
				+ "rqR96MBoSpcRpYBIeb3";
		
		String subExpected = "customer";
		Long iatExpected = 1629696048000L;
		Long expExpected = 1629696948000L;
		
		// When
		when(jwtConfig.getSecretKeyForSigning()).thenReturn(secretKey);
		
		Claims tokenPayload;
		try{
			tokenPayload = underTest.getTokenPayload(token);
		}catch(ExpiredJwtException e){
		   tokenPayload = e.getClaims();
	    }
		
		String username = tokenPayload.getSubject();
		Long iat = tokenPayload.getIssuedAt().getTime();
		Long exp = tokenPayload.getExpiration().getTime();
		// Get Authorities from the body
		List<Map<String, String>> authorities = (List<Map<String, String>>) tokenPayload.get("authorities");
		// Convert Authorities to SimpleGrantedAuthorities
		Set<SimpleGrantedAuthority> simpleGrantedAuthorities = 
				authorities.stream()
				.map(authMap -> new SimpleGrantedAuthority(authMap.get("authority")))
				.collect(Collectors.toSet());
		
		// Then
		assertEquals(subExpected, username);
		assertEquals(iatExpected, iat);
		assertEquals(expExpected, exp);
		Set<GrantedAuthority> customerAuthorities = ApplicationUserRole.CUSTOMER.getGrantedAuthorities();
		assertTrue(simpleGrantedAuthorities.containsAll(customerAuthorities));
		assertTrue(customerAuthorities.containsAll(simpleGrantedAuthorities));
	}
	
	@Test
	void itShouldThrowAnExceptionWhenTokenIsExpired() {
		// Given
		String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJjdXN0b21lciIsImF1dGhvcml0aWVzIjpbeyJhdXRob3Jp"
				+ "dHkiOiJST0xFX0NVU1RPTUVSIn0seyJhdXRob3JpdHkiOiJvcmRlcjp3cml0ZSJ9LHsiYXV0aG9yaXR5Ijoib3JkZXI"
				+ "6cmVhZCJ9LHsiYXV0aG9yaXR5IjoiY2F0ZWdvcnk6cmVhZCJ9LHsiYXV0aG9yaXR5IjoicHJvZHVjdDpyZWFkIn1dLC"
				+ "JpYXQiOjE2Mjk2OTYwNDgsImV4cCI6MTYyOTY5Njk0OH0.6EhglTyrgw8wssv2A4ZHLQZANhjhUt5r8a48dvWmX3HNJ"
				+ "rqR96MBoSpcRpYBIeb3";
		
		// When
		when(jwtConfig.getSecretKeyForSigning()).thenReturn(secretKey);
		
		// Then
		assertThrows(ExpiredJwtException.class, () -> underTest.getTokenPayload(token));
	}
}
