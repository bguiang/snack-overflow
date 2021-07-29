package com.bernardguiang.SnackOverflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponse;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.service.OrderService;
import com.bernardguiang.SnackOverflow.service.UserService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
	
	private final OrderService orderService;
	private final UserService userService;
	
	@Autowired
	public OrderController(OrderService orderService, UserService userService) {
		this.orderService = orderService;
		this.userService = userService;
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
		
		return orderService.findByIdAndUserId(orderId, user.getId());
	}
}
