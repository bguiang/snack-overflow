package com.bernardguiang.SnackOverflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.service.StripeService;
import com.stripe.exception.StripeException;

@RestController
@RequestMapping("/api/v1/stripe")
public class StripeController {

	private final StripeService stripeService;

	@Autowired
	public StripeController(StripeService stripeService) {
		this.stripeService = stripeService;
	}

	@PostMapping
	public ResponseEntity<String> stripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signatureHeader)
			throws Exception {
		try {
			stripeService.handleStripeWebhookEvent(payload, signatureHeader);
			return new ResponseEntity<>("Received", HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>("Invalid Payload", HttpStatus.BAD_REQUEST);
		}
	}
}
