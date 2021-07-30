package com.bernardguiang.SnackOverflow.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;

import com.bernardguiang.SnackOverflow.dto.request.RegisterRequest;
import com.bernardguiang.SnackOverflow.dto.response.AuthenticationResponse;
import com.bernardguiang.SnackOverflow.service.AuthService;

class AuthControllerTest {
	
	private AuthController underTest;
	
	private AuthService authService;

	@BeforeEach
	void setUp() throws Exception {
		authService = Mockito.mock(AuthService.class);
		underTest = new AuthController(authService);
	}
	
	@Test
	void itShouldReturnSuccessMessageAndCreatedStatusWithValidRegisterRequest() {
		// Given
		// ... Java bean validation does not work in unit tests so this will always be valid here
		RegisterRequest registerRequest = null;
		
		// When
		ResponseEntity<String> result = underTest.signup(registerRequest);
		
		// Then
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		assertEquals("User Registration Successful", result.getBody());
	}
	
	@Test
	void logoutShouldReturnSuccessMessageAndOkStatusAndSetEmptyRefreshCookie() {
		// Given	
		MockHttpServletResponse response = new MockHttpServletResponse();
		Cookie emptyRefreshCookie = new Cookie("refresh-token", null);
		
		// When
		when(authService.generateEmptyRefreshTokenCookie()).thenReturn(emptyRefreshCookie);
		ResponseEntity<String> result = underTest.Logout(response);
		
		// Then
		assertEquals(1, response.getCookies().length);
		assertEquals("refresh-token", response.getCookies()[0].getName());
		assertNull(response.getCookie("refresh-token").getValue());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("Logout Successful", result.getBody());
	}
	
	@Test
	void refreshTokensShouldReturnAuthenticationResponseAndSetNewRefreshCookie() {
		// Given
		MockHttpServletResponse response = new MockHttpServletResponse();
		String refreshTokenUsed = "old refresh token";
		
		String authenticatedUsername = "username123";
		String newAuthenticationToken = "new jwt";
		AuthenticationResponse authenticationResponse = 
				new AuthenticationResponse(newAuthenticationToken, authenticatedUsername);
		
		String newRefreshToken = "new refresh token";
		Cookie newRefreshCookie = new Cookie("refresh-token", newRefreshToken);
		
		// When
		when(authService.refreshToken(refreshTokenUsed)).thenReturn(authenticationResponse);
		when(authService.generateRefreshTokenCookie(authenticatedUsername)).thenReturn(newRefreshCookie);
		AuthenticationResponse result =  underTest.refreshTokens(response, refreshTokenUsed);
		
		// Then
		assertEquals(1, response.getCookies().length);
		assertEquals("refresh-token", response.getCookies()[0].getName());
		assertEquals(newRefreshToken, response.getCookie("refresh-token").getValue());
		
		assertEquals(newAuthenticationToken, result.getAuthenticationToken());
		assertEquals(authenticatedUsername, result.getUsername());
	}

}
