package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;

import com.bernardguiang.SnackOverflow.dto.response.AuthenticationResponse;
import com.bernardguiang.SnackOverflow.model.RefreshToken;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

import io.jsonwebtoken.security.Keys;

class JwtServiceTest {

	@BeforeEach
	void setUp() throws Exception {
	}

//	@Test
//	void itShouldRefreshToken() {
//		// Given
//		String refreshTokenString = "refresh-token";
//		RefreshToken refreshToken = new RefreshToken();
//		refreshToken.setToken(refreshTokenString);
//		User user = new User();
//		user.setRole(ApplicationUserRole.CUSTOMER.name());
//		user.setUsername("username");
//		refreshToken.setUser(user);
//		
//		// When
//		Optional<RefreshToken> refreshTokenOptional = Optional.ofNullable(refreshToken);
//		when(refreshTokenRepository.findByToken(refreshTokenString)).thenReturn(refreshTokenOptional);
//		when(jwtConfig.getTokenExpirationMilliSeconds()).thenReturn(1000L);
//		String secret = "shhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";
//		SecretKey sk = Keys.hmacShaKeyFor(secret.getBytes());
//		when(jwtConfig.getSecretKeyForSigning()).thenReturn(sk);
//		
//		Mockito.verify(underTest).generateJwt(usernameCaptor.capture(), authoritiesCaptor.capture());
//		String usernameUsed =  usernameCaptor.getValue();
//		Collection<? extends GrantedAuthority> authoritiesUsed = authoritiesCaptor.capture();
//		AuthenticationResponse response = underTest.refreshToken(refreshTokenString);
//		
//		// Then
//		boolean collectionsAreEqual = authoritiesUsed.containsAll(B) && B.containsAll(authoritiesUsed);
//		//TODO:set up argumentcaptor and assess the properties used in the token builder
//		assertEquals(user.getUsername(), response.getUsername());
//	}

}
