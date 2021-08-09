package com.bernardguiang.SnackOverflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartRequest;
import com.bernardguiang.SnackOverflow.dto.request.UpdateBillingAndShippingRequest;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponse;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.service.CartService;
import com.bernardguiang.SnackOverflow.service.OrderService;
import com.bernardguiang.SnackOverflow.service.StripeService;
import com.bernardguiang.SnackOverflow.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
	
	private final OrderService orderService;
	private final UserService userService;
	private final StripeService stripeService;
	private final CartService cartService;
	
	@Autowired
	public OrderController(OrderService orderService, UserService userService, StripeService stripeService, CartService cartService) {
		this.orderService = orderService;
		this.userService = userService;
		this.stripeService = stripeService;
		this.cartService = cartService;
	}
	
	@PostMapping("/start")
	@PreAuthorize("hasAuthority('order:write')")
	public ResponseEntity<Map<String, Object>> startOrder(@RequestBody @Valid CartRequest cartRequest,
			Authentication authentication) throws StripeException {
		
		// Get Current User's Email
		String username = authentication.getName();
		UserDTO user = userService.findByUsername(username);
		String userEmail = user.getEmail();

		// Get Cart Product Info and Total
		CartInfoResponse cart = cartService.getCartInfo(cartRequest);

		// Create Payment Intent and Client Secret
		Long amount = cart.getTotal().longValue() * 100;
		PaymentIntent intent = stripeService.createPaymentIntent(amount, userEmail);
		String clientSecret = intent.getClientSecret();

		// Create Order
		Long savedOrderId = orderService.createOrderWithCartItemsAndClientSecret(cartRequest, clientSecret, user.getId());

		Map<String, Object> map = new HashMap<>();
		map.put("client_secret", clientSecret);
		map.put("cart", cart);
		map.put("orderId", savedOrderId);

		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}

	@PutMapping("/updateBillingAndShipping")
	public ResponseEntity<String> updateOrderBillingAndShipping(
			@RequestBody @Valid UpdateBillingAndShippingRequest updateBillingAndShippingRequest,
			Authentication authentication) throws StripeException {
		String username = authentication.getName();
		UserDTO user = userService.findByUsername(username);

		orderService.updateBillingAndShipping(updateBillingAndShippingRequest, user);

		return new ResponseEntity<>("Order Updated", HttpStatus.OK);
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('order:read')")
	public List<OrderResponse> getOrdersByCurrentUser(Authentication authentication) {
		String username = authentication.getName();
		UserDTO user = userService.findByUsername(username);
		
		return orderService.findAllByUserAndStatusNot(user, OrderStatus.CREATED);
	}
	
	@GetMapping("/{orderId}")
	@PreAuthorize("hasAuthority('order:read')")
	public OrderResponse getOrderByCurrentUser(@PathVariable long orderId, Authentication authentication) {
		String username = authentication.getName();
		UserDTO user = userService.findByUsername(username);
		
		return orderService.findByIdAndUserIdAndStatusNot(orderId, user.getId(), OrderStatus.CREATED);
	}
}
