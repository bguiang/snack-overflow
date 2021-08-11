package com.bernardguiang.SnackOverflow.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartRequest;
import com.bernardguiang.SnackOverflow.dto.request.UpdateBillingAndShippingRequest;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.model.StripePaymentIntent;
import com.bernardguiang.SnackOverflow.service.CartService;
import com.bernardguiang.SnackOverflow.service.OrderService;
import com.bernardguiang.SnackOverflow.service.StripeService;
import com.bernardguiang.SnackOverflow.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@RestController
@RequestMapping("/api/v1/stripe")
public class StripeController {

	private final StripeService stripeService;
	private final CartService cartService;

	@Autowired
	public StripeController(StripeService stripeService, CartService cartService) {
		this.cartService = cartService;
		this.stripeService = stripeService;
	}
	
	@PostMapping("/createPaymentIntent")
	@PreAuthorize("hasAuthority('order:write')")
	public ResponseEntity<Map<String, Object>> createPaymentIntent(@RequestBody @Valid CartRequest cartRequest,
			Authentication authentication) throws StripeException {
		
		// Get Current User's Email
		String username = authentication.getName();

		// Get Cart Product Info and Total
		CartInfoResponse cart = cartService.getCartInfo(cartRequest);

		// Create Payment Intent and Client Secret
		String clientSecret = stripeService.createPaymentIntent(username, cart);

		Map<String, Object> map = new HashMap<>();
		map.put("client_secret", clientSecret);
		map.put("cart", cart);

		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}

	@PostMapping
	public ResponseEntity<String> stripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signatureHeader)
			throws Exception {
		try {
			stripeService.handleStripeWebhookEvent(payload, signatureHeader);
			return new ResponseEntity<>("Received", HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Payload", HttpStatus.BAD_REQUEST);
		}
	}
}
