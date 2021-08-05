package com.bernardguiang.SnackOverflow.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

@WebMvcTest(AuthController.class) // Must specify which controller to test or Spring will scan ALL controllers and
									// we'll ahve to mock all dependencies
class AuthControllerIntTest {

	// Main Dependency of AuthController
	@MockBean
	private AuthService authService;

	// Dependencies of AuthService
	// - ApparentlyAutowired Dependencies of @MockBean dependencies are still
	// autowired...
	// - so we must cascade the mocking of those dependencies as well
	@MockBean
	private PasswordEncoder passwordEncoder;
	@MockBean
	private ApplicationUserDetailsService applicationUserDetailsService;
	@MockBean
	private JwtService jwtService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void hello() throws Exception {
		// Given

		// Then
		mockMvc.perform(get("/api/v1/auth").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(equalTo("Hello World")));
	}

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
				.andExpect(status().isCreated()).andExpect(content().string(equalTo("User Registration Successful")));

		ArgumentCaptor<RegisterRequest> registerRequestCaptor = ArgumentCaptor.forClass(RegisterRequest.class);
		verify(authService).customerSignup(registerRequestCaptor.capture());

		assertEquals(registerRequest.getEmail(), registerRequestCaptor.getValue().getEmail());
		assertEquals(registerRequest.getFullName(), registerRequestCaptor.getValue().getFullName());
		assertEquals(registerRequest.getPassword(), registerRequestCaptor.getValue().getPassword());
		assertEquals(registerRequest.getUsername(), registerRequestCaptor.getValue().getUsername());
	}

	// test that the validation is actually happening but do not test all the properties
	// that should be done in a unit test with a validator
//	@Test
//	void signupShouldFailIfInvalidRequest() throws Exception {
//
//		// Given
//		String email = "test@email.com";
//		String fullName = "test";
//		String password = "Test123!";
//		String username = "";
//		RegisterRequest registerRequest = new RegisterRequest();
//		registerRequest.setEmail(email);
//		registerRequest.setFullName(fullName);
//		registerRequest.setPassword(password);
//		registerRequest.setUsername(username);
//
//		// When
//		when(authService.customerSignup(Mockito.eq(registerRequest))).thenReturn(new UserDTO());
//		// This confirms:
//		// 1.) The proper request mapping was called
//		// 2.) The json string sent was deserialized into the pojo properly
//
//		// Then
//		mockMvc.perform(post("/api/v1/auth/signup").content(asJsonString(registerRequest))
//				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isCreated()).andExpect(content().string(equalTo("User Registration Successful")));
//
//		ArgumentCaptor<RegisterRequest> registerRequestCaptor = ArgumentCaptor.forClass(RegisterRequest.class);
//		verify(authService).customerSignup(registerRequestCaptor.capture());
//
//		assertEquals(registerRequest.getEmail(), registerRequestCaptor.getValue().getEmail());
//		assertEquals(registerRequest.getFullName(), registerRequestCaptor.getValue().getFullName());
//		assertEquals(registerRequest.getPassword(), registerRequestCaptor.getValue().getPassword());
//		assertEquals(registerRequest.getUsername(), registerRequestCaptor.getValue().getUsername());
//	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
