package com.bernardguiang.SnackOverflow.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.OrderDTO;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.User;
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
	public List<OrderDTO> getOrdersByCurrentUser(Authentication authentication) {
		String username = authentication.getName();
		User user = userService.findUserByUsername(username);
		
		return orderService.findAllByUserAndStatusNot(user, OrderStatus.CREATED);
	}
	
	@GetMapping("/{orderId}")
	@PreAuthorize("hasAuthority('order:read')")
	public OrderDTO getOrderByCurrentUser(@PathVariable long orderId, Authentication authentication) {
		String username = authentication.getName();
		User user = userService.findUserByUsername(username);
		
		return orderService.findByIdAndUser(orderId, user); // TODO: will throw if not found. create custom exception
	}

//	@PostMapping
//	@PreAuthorize("hasAuthority('order:write')")
//	public OrderDTO createAnOrder(@Valid @RequestBody OrderDTO orderDTO) {
//		System.out.println("Creating Order: " + orderDTO.toString());
//		return orderService.save(orderDTO);
//	}
}
