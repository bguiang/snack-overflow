package com.bernardguiang.SnackOverflow.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.request.OrderStatusUpdateRequest;
import com.bernardguiang.SnackOverflow.dto.response.OrderDTO;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponse;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.repository.OrderRepository;

@Service
public class OrderService {
	
	private final OrderRepository orderRepository;
	
	@Autowired
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	
	// TODO: test
	public List<OrderDTO> findAllIncludeUserInfo() {
		Iterable<Order> ordersIterator = orderRepository.findAll();
		List<OrderDTO> orderDTOs = new ArrayList<>();
		for(Order order : ordersIterator)
		{
			OrderDTO orderDTO = new OrderDTO(order);
			orderDTOs.add(orderDTO);
		}
		return orderDTOs;
	}
	
	// TODO: test
	public OrderDTO findByIdIncludUserInfo(Long id) {
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException("Could not find Order with id: " + id));
		return new OrderDTO(order);
	}
	
	public OrderResponse findByIdAndUserId(Long id, Long userId) {
		Order order = orderRepository.findByIdAndUserId(id, userId)
			.orElseThrow(() -> new IllegalStateException("Could not find Order with id: " + id + " and userId: " + userId));
		return new OrderResponse(order);
	}
	
	public List<OrderResponse> findAllByUserId(Long userId) {
		Iterable<Order> ordersIterator = orderRepository.findAllByUserId(userId);
		List<OrderResponse> orderDTOs = new ArrayList<>();
		for(Order order : ordersIterator)
		{
			OrderResponse orderDTO = new OrderResponse(order);
			orderDTOs.add(orderDTO);
		}
		return orderDTOs;
	}
	
	//TODO: test
	public OrderResponse updateOrderStatus(OrderStatusUpdateRequest request) {
		// Find order first
		Long id = request.getId();
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException("Could not find Order with id: " + id));
		
		// Update values minus PaymentIntentId and Cart
		order.setStatus(request.getStatus());
		
		Order saved = orderRepository.save(order);	
		
		return new OrderResponse(saved);
	}
}
