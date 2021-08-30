package com.bernardguiang.SnackOverflow.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.RegisterRequest;
import com.bernardguiang.SnackOverflow.dto.response.AuthenticationResponse;
import com.bernardguiang.SnackOverflow.service.ApplicationUserDetailsService;
import com.bernardguiang.SnackOverflow.service.AuthService;
import com.bernardguiang.SnackOverflow.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Spring provides several annotations for testing different parts (layers) of the application
 * They each scan a different set of classes that only provide what is needed to test that part of the application
 * Web Layer - we don't want to involve database calls when testing this
 * The rest of the dependencies are mocked
 * @WebMvcTest only scans @Controller, @ControllerAdvice, @JsonComponent, Converter/GenericConverter, 
 * Filter, WebMvcConfigurerand HandlerMethodArgumentResolver beans but NOT @Component, @Service or @Repository beans
 */

// Must specify which controller to test or Spring will scan 
// ALL controllers andwe'll have to mock all dependencies
@WebMvcTest(AuthController.class)
class AuthControllerIntegrationTest {

	// Auth Filter Dependencies
	@MockBean 
	private AuthService authService; // Also the main Dependency of AuthController
	@MockBean
	private PasswordEncoder passwordEncoder;
	@MockBean
	private ApplicationUserDetailsService applicationUserDetailsService;
	@MockBean
	private JwtService jwtService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void itShouldSignUp() throws Exception {
		// Given
		String email = "test@email.com";
		String fullName = "test";
		String password = "Test123!";
		String username = "tester";
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setEmail(email);
		registerRequest.setFullName(fullName);
		registerRequest.setPassword(password);
		registerRequest.setUsername(username);

		// When
		when(authService.customerSignup(Mockito.eq(registerRequest))).thenReturn(new UserDTO());
		// This confirms:
		// 1.) The proper request mapping was called
		// 2.) The json string sent was deserialized into the pojo properly

		// Then
		mockMvc.perform(post("/api/v1/auth/signup").content(asJsonString(registerRequest))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().string(equalTo("User Registration Successful")))
				.andExpect(jsonPath("$").value("User Registration Successful"));

		ArgumentCaptor<RegisterRequest> registerRequestCaptor = ArgumentCaptor.forClass(RegisterRequest.class);
		verify(authService).customerSignup(registerRequestCaptor.capture());

		assertEquals(registerRequest.getEmail(), registerRequestCaptor.getValue().getEmail());
		assertEquals(registerRequest.getFullName(), registerRequestCaptor.getValue().getFullName());
		assertEquals(registerRequest.getPassword(), registerRequestCaptor.getValue().getPassword());
		assertEquals(registerRequest.getUsername(), registerRequestCaptor.getValue().getUsername());
	}

	// test that the validation is actually happening but do not test all the properties
	// that should be done in a unit test with a validator
	@Test
	void signupShouldFailIfInvalidRequest() throws Exception {

		// Given
		RegisterRequest registerRequest = new RegisterRequest();

		// When
		// Then
		mockMvc.perform(post("/api/v1/auth/signup").content(asJsonString(registerRequest))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()
		);
	}
	
	@Test
	void logout() throws Exception {
		// Given
	
		String cookieValue = null;
		
		Cookie emptyRefreshCookie = new Cookie("refresh-token", cookieValue);
		emptyRefreshCookie.setSecure(false);
		emptyRefreshCookie.setHttpOnly(true);
		emptyRefreshCookie.setPath("/api/v1/auth");

		// When
		when(authService.generateEmptyRefreshTokenCookie()).thenReturn(emptyRefreshCookie);
		// This confirms:
		// 1.) The proper request mapping was called
		// 2.) The json string sent was deserialized into the pojo properly

		// Then
		mockMvc.perform(get("/api/v1/auth/logout"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value("Logout Successful"))
				.andExpect(cookie().exists("refresh-token"))
				.andExpect(cookie().value("refresh-token", cookieValue))
				.andExpect(cookie().path("refresh-token", "/api/v1/auth"))
				.andExpect(cookie().httpOnly("refresh-token", true));
	}
	
	@Test
	void refreshTokens() throws Exception {
		// Given
		String username = "tokenOwner";
		String refreshTokenUsed = "refreshTokenUsed";
		String newRefreshTokenGenerated = "newRefreshToken";
		String authenticationTokenGenerated = "new jwt";
		
		Cookie requestCookie = new Cookie("refresh-token", refreshTokenUsed);
		requestCookie.setSecure(false);
		requestCookie.setHttpOnly(true);
		requestCookie.setPath("/api/v1/auth");
		
		Cookie responseCookie = new Cookie("refresh-token", newRefreshTokenGenerated);
		responseCookie.setSecure(false);
		responseCookie.setHttpOnly(true);
		responseCookie.setPath("/api/v1/auth");
		
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		authenticationResponse.setAuthenticationToken(authenticationTokenGenerated);
		authenticationResponse.setUsername(username);

		// When
		when(authService.refreshToken(refreshTokenUsed)).thenReturn(authenticationResponse);
		
		when(authService.generateRefreshTokenCookie(username)).thenReturn(responseCookie);

		// Then
		mockMvc.perform(get("/api/v1/auth/refresh").cookie(requestCookie))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.authenticationToken").value(authenticationTokenGenerated))
				.andExpect(jsonPath("$.username").value(username))
				.andExpect(cookie().exists("refresh-token"))
				.andExpect(cookie().value("refresh-token", newRefreshTokenGenerated))
				.andExpect(cookie().path("refresh-token", "/api/v1/auth"))
				.andExpect(cookie().httpOnly("refresh-token", true));
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
