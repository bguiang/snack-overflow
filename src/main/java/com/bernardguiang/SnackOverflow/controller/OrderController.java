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
import com.bernardguiang.SnackOverflow.dto.response.OrderDTO;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponse;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.service.CartService;
import com.bernardguiang.SnackOverflow.service.OrderService;
import com.bernardguiang.SnackOverflow.service.StripeService;
import com.bernardguiang.SnackOverflow.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@RestController
//@RequestMapping("/api/v1/orders")
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
	
	@GetMapping("/api/v1/orders")
	@PreAuthorize("hasAuthority('order:read')")
	public List<OrderResponse> getOrdersByCurrentUser(Authentication authentication) {
		String username = authentication.getName();
		UserDTO user = userService.findByUsername(username);
		
		return orderService.findAllByUserAndStatusNot(user, OrderStatus.CREATED);
	}
	
	@GetMapping("/api/v1/orders/{orderId}")
	@PreAuthorize("hasAuthority('order:read')")
	public OrderResponse getOrderByCurrentUser(@PathVariable long orderId, Authentication authentication) {
		String username = authentication.getName();
		UserDTO user = userService.findByUsername(username);
		
		return orderService.findByIdAndUserIdAndStatusNot(orderId, user.getId(), OrderStatus.CREATED);
	}
	
	//TODO: test
	@GetMapping("/api/v1/admin/orders")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<OrderDTO> getAllStartedOrders() {
		return orderService.findAllByStatusNot(OrderStatus.CREATED);
	}
	
	//TODO: test
	@GetMapping("/api/v1/admin/orders/{orderId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public OrderDTO getOrderByCurrentUser(@PathVariable long orderId) {
		return orderService.findByIdAndStatusNot(orderId, OrderStatus.CREATED);
	}
}
