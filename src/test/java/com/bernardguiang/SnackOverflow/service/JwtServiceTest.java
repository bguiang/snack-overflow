package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
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
	
	private final String secretKeyString = "ssshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";
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
		String username = "felix123";
		Instant iat = Instant.ofEpochMilli(1627285608000L);
		Collection<? extends GrantedAuthority> authorities = ApplicationUserRole.CUSTOMER.getGrantedAuthorities();
		
		String tokenExpected = 
				"eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJmZWxpeDEyMyIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJST"
				+ "0xFX0NVU1RPTUVSIn0seyJhdXRob3JpdHkiOiJvcmRlcjp3cml0ZSJ9LHsiYXV0aG9yaXR5IjoiY2F0ZWdvcn"
				+ "k6cmVhZCJ9LHsiYXV0aG9yaXR5IjoicHJvZHVjdDpyZWFkIn1dLCJpYXQiOjE2MjcyODU2MDgsImV4cCI6MTYy"
				+ "NzI4NjUwOH0.zwOkhm_U7xaauxiY_cSY2mDWJDkf9XdKuG7DPmFYVhcBRhX-vnCI7k4Q2MG8Oy5C";
		
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
		String token = 
			"eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJmZWxpeDEyMyIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJST"
			+ "0xFX0NVU1RPTUVSIn0seyJhdXRob3JpdHkiOiJvcmRlcjp3cml0ZSJ9LHsiYXV0aG9yaXR5IjoiY2F0ZWdvcn"
			+ "k6cmVhZCJ9LHsiYXV0aG9yaXR5IjoicHJvZHVjdDpyZWFkIn1dLCJpYXQiOjE2MjcyODU2MDgsImV4cCI6MTYy"
			+ "NzI4NjUwOH0.zwOkhm_U7xaauxiY_cSY2mDWJDkf9XdKuG7DPmFYVhcBRhX-vnCI7k4Q2MG8Oy5C";
		
		String subExpected = "felix123";
		Long iatExpected = 1627285608000L;
		Long expExpected = 1627286508000L;
		
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
		String token = 
			"eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJmZWxpeDEyMyIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJST"
			+ "0xFX0NVU1RPTUVSIn0seyJhdXRob3JpdHkiOiJvcmRlcjp3cml0ZSJ9LHsiYXV0aG9yaXR5IjoiY2F0ZWdvcn"
			+ "k6cmVhZCJ9LHsiYXV0aG9yaXR5IjoicHJvZHVjdDpyZWFkIn1dLCJpYXQiOjE2MjcyODU2MDgsImV4cCI6MTYy"
			+ "NzI4NjUwOH0.zwOkhm_U7xaauxiY_cSY2mDWJDkf9XdKuG7DPmFYVhcBRhX-vnCI7k4Q2MG8Oy5C";
		
		// When
		when(jwtConfig.getSecretKeyForSigning()).thenReturn(secretKey);
		
		// Then
		assertThrows(ExpiredJwtException.class, () -> underTest.getTokenPayload(token));
	}
}
