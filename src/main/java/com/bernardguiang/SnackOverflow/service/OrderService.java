package com.bernardguiang.SnackOverflow.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.BillingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.CheckoutRequest;
import com.bernardguiang.SnackOverflow.dto.OrderDTO;
import com.bernardguiang.SnackOverflow.dto.OrderItemDTO;
import com.bernardguiang.SnackOverflow.dto.ShippingDetailsDTO;
import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.ShippingDetails;
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
	
	public OrderDTO saveOrderFromCheckoutForUser(CheckoutRequest checkoutRequest,  Long userId) {
		
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalStateException("Could not find user with id: " + userId));
		
		Order order = checkoutRequestToOrder(checkoutRequest,user);
		Order saved = orderRepository.save(order);
		
		return orderToDTO(saved);
	}
	
	public OrderDTO save(OrderDTO orderDTO) {
		
		Order order = dtoToOrder(orderDTO);
		Order saved = orderRepository.save(order);
		
		return orderToDTO(saved);
	}
	
	private Order checkoutRequestToOrder(CheckoutRequest checkoutRequest, User  user) {
		Order order = new Order();
		
		List<OrderItem> items = new ArrayList<>();
		for(CartInfoRequestItem requestItem : checkoutRequest.getItems()) {
			Product product = productRepository.findById(requestItem.getProductId())
				.orElseThrow(() -> new IllegalStateException("Could not find product with id: " + requestItem.getProductId()));
			OrderItem item = new OrderItem();
			item.setOrder(order); // set order
			item.setProduct(product); // set product
			item.setPrice(product.getPrice());
			item.setQuantity(requestItem.getQuantity());
			
			items.add(item);
		}
		
		order.setItems(items);
		order.setCreatedDate(Instant.now());
		order.setBillingDetails(dtoToBillingDetails(checkoutRequest.getBillingDetails()));
		order.setShippingDetails(dtoToShippingDetails(checkoutRequest.getShippingDetails()));
		order.setShippingSameAsBilling(checkoutRequest.isShippingSameAsBilling());
		order.setNotes(checkoutRequest.getNotes());
		order.setUser(user);
		order.setStatus(OrderStatus.CREATED);
		
		return order;
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
		BillingDetailsDTO billing = entityToBillingDetailsDTO(order.getBillingDetails());
		dto.setBillingDetails(billing);
		ShippingDetailsDTO shipping = entityToShippingDetailsDTO(order.getShippingDetails());
		dto.setShippingDetails(shipping);
		dto.setShippingSameAsBilling(order.isShippingSameAsBilling());
		dto.setUserId(order.getUser().getId());
		dto.setStatus(order.getStatus());
		
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
		order.setBillingDetails(dtoToBillingDetails(orderDTO.getBillingDetails()));
		order.setShippingDetails(dtoToShippingDetails(orderDTO.getShippingDetails()));
		order.setShippingSameAsBilling(orderDTO.isShippingSameAsBilling());
		order.setNotes(orderDTO.getNotes());
		order.setUser(user);
		if(orderDTO.getStatus() != null)
			order.setStatus(orderDTO.getStatus());
		
		return order;
	}
	
	
	private BillingDetailsDTO entityToBillingDetailsDTO(BillingDetails billingDetails) {
		if(billingDetails == null)
			return null;
		
		BillingDetailsDTO dto = new BillingDetailsDTO();
		dto.setId(billingDetails.getId());
		dto.setAddress(billingDetails.getAddress());
		dto.setName(billingDetails.getName());
		dto.setPhone(billingDetails.getPhone());
		dto.setEmail(billingDetails.getEmail());
		dto.setOrderId(billingDetails.getOrder().getId());
		
		return dto;
	}
	
	private BillingDetails dtoToBillingDetails(BillingDetailsDTO dto) {
		
		Order order = orderRepository.findById(dto.getOrderId())
				.orElseThrow(() -> new IllegalStateException("Could not find order with id: " + dto.getOrderId()));
		
		BillingDetails entity = new BillingDetails();
		entity.setId(dto.getId());
		entity.setAddress(dto.getAddress());
		entity.setOrder(order);
		entity.setEmail(dto.getEmail());
		entity.setPhone(dto.getPhone());
		entity.setName(dto.getName());
		
		return entity;
	}

	
	private ShippingDetailsDTO entityToShippingDetailsDTO(ShippingDetails shippingDetails) {
		
		if(shippingDetails == null)
			return null;
		ShippingDetailsDTO dto = new ShippingDetailsDTO();
		dto.setId(shippingDetails.getId());
		dto.setAddress(shippingDetails.getAddress());
		dto.setName(shippingDetails.getName());
		dto.setPhone(shippingDetails.getPhone());
		dto.setOrderId(shippingDetails.getOrder().getId());
		
		return dto;
	}
	
	private ShippingDetails dtoToShippingDetails(ShippingDetailsDTO dto) {
		
		Order order = orderRepository.findById(dto.getOrderId())
				.orElseThrow(() -> new IllegalStateException("Could not find order with id: " + dto.getOrderId()));
		
		ShippingDetails entity = new ShippingDetails();
		entity.setId(dto.getId());
		entity.setAddress(dto.getAddress());
		entity.setOrder(order);
		entity.setPhone(dto.getPhone());
		entity.setName(dto.getName());
		
		return entity;
	}


}
