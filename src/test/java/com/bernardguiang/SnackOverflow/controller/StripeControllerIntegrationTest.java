package com.bernardguiang.SnackOverflow.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.bernardguiang.SnackOverflow.repository.OrderRepository;
import com.bernardguiang.SnackOverflow.service.ApplicationUserDetailsService;
import com.bernardguiang.SnackOverflow.service.AuthService;
import com.bernardguiang.SnackOverflow.service.JwtService;
import com.bernardguiang.SnackOverflow.service.StripeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;

@WebMvcTest(StripeController.class)
class StripeControllerIntegrationTest {
	
	// StripeController Dependencies
	@MockBean
	private StripeService stripeService;
	//@MockBean
	//private Environment env;
	@MockBean
	private OrderRepository orderRepository;
	
	// JWT Auth Filter Dependencies
	@MockBean
	private AuthService authService;
	@MockBean
	private PasswordEncoder passwordEncoder;
	@MockBean
	private ApplicationUserDetailsService applicationUserDetailsService;
	@MockBean
	private JwtService jwtService;

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void itShouldSucceed() throws Exception {
		// Given
		String payload = "payload";
		String signatureHeader = "signature header";
		
		// When
		doNothing().when(stripeService).handleStripeWebhookEvent(payload, signatureHeader);
		
		// Then
		mockMvc.perform(
			post("/api/v1/stripe")
				.header("Stripe-Signature", signatureHeader)
				.content(payload)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value("Received"))
		;
	}

	@Test
	void itShouldFailIfInvalidPayload() throws Exception {
		// Given
		String payload = "payload";
		String signatureHeader = "signature header";
		
		// When
		doThrow(new JsonSyntaxException(signatureHeader)).when(stripeService).handleStripeWebhookEvent(payload, signatureHeader);
		
		// Then
		mockMvc.perform(
			post("/api/v1/stripe")
				.header("Stripe-Signature", signatureHeader)
				.content(payload)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$").value("Invalid Payload"))
		;
		verify(stripeService).handleStripeWebhookEvent(payload, signatureHeader);
	}
	
	@Test
	void itShouldFailIfInvalidHeader() throws Exception {
		// Given
		String payload = "payload";
		String signatureHeader = "signature header";
		
		// When
		doThrow(new SignatureVerificationException("", signatureHeader)).when(stripeService).handleStripeWebhookEvent(payload, signatureHeader);
		
		// Then
		mockMvc.perform(
			post("/api/v1/stripe")
				.header("Stripe-Signature", signatureHeader)
				.content(payload)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$").value("Invalid Payload"))
		;
		verify(stripeService).handleStripeWebhookEvent(payload, signatureHeader);
	}

	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
