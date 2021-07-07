package com.bernardguiang.SnackOverflow.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.OrderDTO;
import com.bernardguiang.SnackOverflow.dto.OrderItemDTO;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.OrderRepository;
import com.bernardguiang.SnackOverflow.repository.ProductRepository;
import com.bernardguiang.SnackOverflow.repository.UserRepository;

@Service
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	
	public OrderService(
			OrderRepository orderRepository, 
			ProductRepository productRepository, 
			UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}
	
	public OrderDTO save(OrderDTO orderDTO) {
		
		Order order = dtoToOrder(orderDTO);
		Order saved = orderRepository.save(order);
		
		return orderToDTO(saved);
	}
	
	private OrderDTO orderToDTO(Order order) {

		OrderDTO dto = new OrderDTO();
		List<OrderItemDTO> itemDTOs = new ArrayList<>();
		for(OrderItem item : order.getItems()) {
			OrderItemDTO itemDTO = new OrderItemDTO(
					item.getId(), 
					item.getProduct().getId(), 
					item.getPrice(), 
					item.getQuantity());
			itemDTOs.add(itemDTO);
		}
		
		dto.setId(order.getId());
		dto.setItems(itemDTOs);
		dto.setCreatedDate(order.getCreatedDate());
		dto.setNotes(order.getNotes());
		dto.setBillingName(order.getBillingName());
		dto.setBillingAddress(order.getBillingAddress());
		dto.setShippingName(order.getShippingName());
		dto.setShippingAddress(order.getShippingAddress());
		dto.setShippingSameAsBilling(order.isShippingSameAsBilling());
		dto.setUserId(order.getUser().getId());
		
		return dto;
	}
	
	private Order dtoToOrder(OrderDTO orderDTO) {
		
		Order order = new Order();
		User user = userRepository.findById(orderDTO.getUserId())
				.orElseThrow(() -> new IllegalStateException("Could not find user with id: " + orderDTO.getUserId()));
		
		List<OrderItem> items = new ArrayList<>();
		for(OrderItemDTO dto : orderDTO.getItems()) {
			Product product = productRepository.findById(dto.getProductId())
				.orElseThrow(() -> new IllegalStateException("Could not find product with id: " + dto.getProductId()));
			OrderItem item = new OrderItem();
			item.setId(dto.getId());
			item.setOrder(order); // set order
			item.setProduct(product); // set product
			item.setPrice(dto.getPrice());
			item.setQuantity(dto.getQuantity());
			
			items.add(item);
		}
		
		order.setId(orderDTO.getId());
		order.setItems(items);
		order.setCreatedDate(orderDTO.getCreatedDate());
		order.setBillingName(orderDTO.getBillingName());
		order.setBillingAddress(orderDTO.getBillingAddress());
		order.setShippingName(orderDTO.getShippingName());
		order.setShippingAddress(orderDTO.getShippingAddress());
		order.setShippingSameAsBilling(orderDTO.isShippingSameAsBilling());
		order.setNotes(orderDTO.getNotes());
		order.setUser(user);
		
		return order;
	}

}
