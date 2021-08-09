package com.bernardguiang.SnackOverflow.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bernardguiang.SnackOverflow.service.StripeService;
import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;

class StripeControllerTest {

	private StripeController underTest;
	private StripeService stripeService;


	@BeforeEach
	void setUp() throws Exception {
		stripeService = Mockito.mock(StripeService.class);
		underTest = new StripeController(stripeService);
	}

	@Test
	void StripeWebhookShouldReturnOkStatus() throws Exception {
		// Given
		String payload = "any";
		String signatureHeader = "any";
		
		// When
		doNothing().when(stripeService).handleStripeWebhookEvent(Mockito.any(), Mockito.any());
		
		// Then
		ResponseEntity<String> response = underTest.stripeWebhook(payload, signatureHeader);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Received", response.getBody());
	}
	
	@Test
	void StripeWebhookShouldReturnBadRequestIfInvalidJsonPayload() throws Exception {
		// Given
		String payload = "any";
		String signatureHeader = "any";
		
		// When
		doThrow(new JsonSyntaxException("Invalid json")).when(stripeService).handleStripeWebhookEvent(Mockito.any(), Mockito.any());
		
		// Then
		ResponseEntity<String> response = underTest.stripeWebhook(payload, signatureHeader);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Invalid Payload", response.getBody());
	}
	
	@Test
	void StripeWebhookShouldReturnBadRequestIfInvalidStripeSignature() throws Exception {
		// Given
		String payload = "any";
		String signatureHeader = "any";
		
		// When
		doThrow(new SignatureVerificationException("Invalid", "Signature")).when(stripeService).handleStripeWebhookEvent(Mockito.any(), Mockito.any());
		
		// Then
		ResponseEntity<String> response = underTest.stripeWebhook(payload, signatureHeader);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Invalid Payload", response.getBody());
	}

}
