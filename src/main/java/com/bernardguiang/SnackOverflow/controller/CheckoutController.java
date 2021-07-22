package com.bernardguiang.SnackOverflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.request.UpdateBillingAndShippingRequest;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.service.CartService;
import com.bernardguiang.SnackOverflow.service.OrderService;
import com.bernardguiang.SnackOverflow.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

	private final Environment env;
	private final CartService cartService;
	private final OrderService orderService;
	private final UserService userService;

	@Autowired
	public CheckoutController(Environment env, CartService cartService, OrderService orderService,
			UserService userService) {
		this.env = env;
		this.cartService = cartService;
		this.orderService = orderService;
		this.userService = userService;
	}

	@PostMapping("/startCheckout")
	@PreAuthorize("hasAuthority('order:write')")
	public ResponseEntity<Map<String, Object>> startCheckout(@RequestBody List<@Valid CartInfoRequestItem> cartItems,
			Authentication authentication) throws StripeException {

		String username = authentication.getName();
		UserDTO user = userService.findByUsername(username);

		// Get Cart Product Info and Total
		CartInfoResponse cart = cartService.getCartInfo(cartItems);

		// Create Payment Intent
		String stripeAccessKey = env.getProperty("stripe_access_key");
		Stripe.apiKey = stripeAccessKey;

		Long amount = cart.getTotal().longValue() * 100;

		System.out.println(cart.toString());
		System.out.println("Cart Total: " + cart.getTotal());
		PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setCurrency("usd").setAmount(amount)
				.setReceiptEmail(user.getEmail()) // stripe will automatically send receipts but only when using live
													// keys
				.putMetadata("integration_check", "accept_a_payment")
				.setDescription("Test payment to SnackOverflow. This is not a real transaction").build();

		PaymentIntent intent = PaymentIntent.create(params);
		String clientSecret = intent.getClientSecret();

		Long savedOrderId = orderService.createOrderWithCartItemsAndClientSecret(cartItems, clientSecret, user.getId());

		Map<String, Object> map = new HashMap<>();
		map.put("client_secret", clientSecret);
		map.put("cart", cart);
		map.put("orderId", savedOrderId);

		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}

	@PutMapping("/updateBillingAndShipping")
	public ResponseEntity<String> createOrder(
			@RequestBody @Valid UpdateBillingAndShippingRequest updateBillingAndShippingRequest,
			Authentication authentication) throws StripeException {
		String username = authentication.getName();
		UserDTO user = userService.findByUsername(username);

		orderService.updateBillingAndShipping(updateBillingAndShippingRequest, user);

		return new ResponseEntity<>("Order Updated", HttpStatus.OK);
	}
}
