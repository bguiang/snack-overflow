package com.bernardguiang.SnackOverflow.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.OrderPage;
import com.bernardguiang.SnackOverflow.dto.request.StatsRequest;
import com.bernardguiang.SnackOverflow.dto.request.OrderStatusUpdateRequest;
import com.bernardguiang.SnackOverflow.dto.response.OrderDTO;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponse;
import com.bernardguiang.SnackOverflow.dto.response.OrderStatsResponse;
import com.bernardguiang.SnackOverflow.service.OrderService;
import com.bernardguiang.SnackOverflow.service.UserService;

@RestController
public class OrderController {
	
	private final OrderService orderService;
	private final UserService userService;
	
	@Autowired
	public OrderController(OrderService orderService, UserService userService) {
		this.orderService = orderService;
		this.userService = userService;
	}
	
	@GetMapping("/api/v1/orders")
	@PreAuthorize("hasAuthority('order:read')")
	public List<OrderResponse> getOrdersByCurrentUser(Authentication authentication) {
		String username = authentication.getName();
		UserDTO user = userService.findByUsername(username);
		
		return orderService.findAllByUserId(user.getId());
	}
	
	@GetMapping("/api/v1/orders/{orderId}")
	@PreAuthorize("hasAuthority('order:read')")
	public OrderResponse getOrderByCurrentUser(@PathVariable long orderId, Authentication authentication) {
		String username = authentication.getName();
		UserDTO user = userService.findByUsername(username);
		
		return orderService.findByIdAndUserId(orderId, user.getId());
	}
	
	@GetMapping("/api/v1/admin/orders")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Page<OrderDTO> getOrders(OrderPage orderPage) {
		return orderService.findOrdersPaginated(orderPage);
	}
	
	@GetMapping("/api/v1/admin/orders/{orderId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public OrderDTO getOrder(@PathVariable long orderId) {
		return orderService.findByIdIncludUserInfo(orderId);
	}
	
	@PutMapping("/api/v1/admin/orders")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public OrderResponse updateOrderStatus(@RequestBody @Valid OrderStatusUpdateRequest orderStatusUpdate) {
		return orderService.updateOrderStatus(orderStatusUpdate);
	}
	
	// Month, Year, All
	@GetMapping("/api/v1/admin/orders/stats")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public OrderStatsResponse getOrderStats(@Valid StatsRequest request) {
		return orderService.getOrderStats(request);
	}
}
