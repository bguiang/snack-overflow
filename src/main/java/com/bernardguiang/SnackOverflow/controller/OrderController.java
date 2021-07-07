package com.bernardguiang.SnackOverflow.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.OrderDTO;
import com.bernardguiang.SnackOverflow.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
	
	private final OrderService orderService;
	
	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('order:read')")
	public List<OrderDTO> getOrders() {
		return null;
	}

	@PostMapping
	@PreAuthorize("hasAuthority('order:write')")
	public OrderDTO createAnOrder(@Valid @RequestBody OrderDTO orderDTO) {
		System.out.println("Creating Order: " + orderDTO.toString());
		return orderService.save(orderDTO);
	}
}
