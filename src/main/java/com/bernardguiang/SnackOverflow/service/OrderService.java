package com.bernardguiang.SnackOverflow.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartRequest;
import com.bernardguiang.SnackOverflow.dto.request.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.request.UpdateBillingAndShippingRequest;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponse;
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
	private final ProductRepository	productRepository;
	private final UserRepository userRepository;
	
	@Autowired
	public OrderService(
			OrderRepository orderRepository, 
			ProductRepository productRepository,
			UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}
	
	public OrderResponse findByIdAndUserId(Long id, Long userId) {
		Order order = orderRepository.findByIdAndUserId(id, userId)
			.orElseThrow(() -> new IllegalStateException("Could not find Order with id: " + id + " and userId: " + userId));
		return new OrderResponse(order);
	}
	
	public List<OrderResponse> findAllByUserAndStatusNot(UserDTO user, OrderStatus status) {
		Iterable<Order> ordersIterator = orderRepository.findAllByUserIdAndStatusNot(user.getId(), status);
		List<OrderResponse> orderDTOs = new ArrayList<>();
		for(Order order : ordersIterator)
		{
			OrderResponse orderDTO = new OrderResponse(order);
			orderDTOs.add(orderDTO);
		}
		return orderDTOs;
	}
	
	public OrderResponse findByIdAndUserIdAndStatusNot(Long id, Long userId, OrderStatus status) {
		Order order = orderRepository.findByIdAndUserIdAndStatusNot(id, userId, status)
			.orElseThrow(() -> new IllegalStateException("Could not find Order with id: " + id + " and userId: " + userId));
		return new OrderResponse(order);
	}
	
	public Long createOrderWithCartItemsAndClientSecret(CartRequest cartRequest, String clientSecret, Long userId) {
		
		Order order = new Order();
		
		BigDecimal total = new BigDecimal("0");	
		List<OrderItem> items = new ArrayList<>();
		for(CartInfoRequestItem requestItem : cartRequest.getItems()) {
			Product product = productRepository.findById(requestItem.getProductId())
				.orElseThrow(() -> new IllegalStateException("Could not find product with id: " + requestItem.getProductId()));
			OrderItem item = new OrderItem();
			item.setOrder(order); // set order
			item.setProduct(product); // set product
			item.setPrice(product.getPrice());
			item.setQuantity(requestItem.getQuantity());
			items.add(item);
			
			total = total.add(product.getPrice().multiply(new BigDecimal(requestItem.getQuantity())));
		}
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalStateException("Could not find user with id: " + userId));
		order.setItems(items);
		order.setTotal(total);
		order.setUser(user);
		order.setClientSecret(clientSecret);
		order.setStatus(OrderStatus.CREATED);
		
		Order saved = orderRepository.save(order);
		
		return saved.getId();
	}
	
	// TODO: this doesn't "update" billing and shipping. It always saves a new one. 
	// TODO: Don't let the user dictate which billing or shipping id to update either
	public OrderResponse updateBillingAndShipping(UpdateBillingAndShippingRequest update, UserDTO user) {
		
		Order order = orderRepository.findByIdAndUserId(update.getId(), user.getId())
				.orElseThrow(() -> new IllegalStateException("Order " + update.getId() + " does not exist for user " + user.getId()));	
		
		order.setCreatedDate(Instant.now());
		
		// Update Billing
		BillingDetails billing = null;
		if(order.getBillingDetails() == null) {
			billing = new BillingDetails();
		} else {
			billing = order.getBillingDetails();
		}
		
//		if(update.getBillingDetails().getId() != null)
//			billing.setId(update.getBillingDetails().getId());
		billing.setAddress(update.getBillingDetails().getAddress());
		billing.setEmail(update.getBillingDetails().getEmail());
		billing.setPhone(update.getBillingDetails().getPhone());
		billing.setName(update.getBillingDetails().getName());
		billing.setOrder(order);
		order.setBillingDetails(billing);
		
		// Update Shipping
		order.setShippingSameAsBilling(update.isShippingSameAsBilling());
		
		if(update.isShippingSameAsBilling()) {
			order.setShippingDetails(null);
		} else {
			ShippingDetails shipping = null;
			if(order.getShippingDetails() == null) {
				shipping = new ShippingDetails();
			} else {
				shipping = order.getShippingDetails();
			}
//			if(update.getShippingDetails().getId() != null)
//				shipping.setId(update.getShippingDetails().getId());
			shipping.setAddress(update.getShippingDetails().getAddress());
			shipping.setPhone(update.getShippingDetails().getPhone());
			shipping.setName(update.getShippingDetails().getName());
			shipping.setOrder(order);
			order.setShippingDetails(shipping);
		}
		
		Order saved = orderRepository.save(order);	
		
		return new OrderResponse(saved);
	}
}
