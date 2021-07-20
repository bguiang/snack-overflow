package com.bernardguiang.SnackOverflow.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.BillingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.UpdateBillingAndShippingRequest;
import com.bernardguiang.SnackOverflow.dto.OrderDTO;
import com.bernardguiang.SnackOverflow.dto.OrderItemDTO;
import com.bernardguiang.SnackOverflow.dto.ProductDTO;
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
	
	public List<OrderDTO> findAllByUser(User user){
		Iterable<Order> ordersIterator = orderRepository.findAllByUser(user);
		List<OrderDTO> orderDTOs = new ArrayList<>();
		for(Order order : ordersIterator)
		{
			OrderDTO orderDTO = orderToDTO(order);
			orderDTOs.add(orderDTO);
		}
		return orderDTOs;
	}
	
	public OrderDTO createOrderWithCartItemsAndClientSecret(List<CartInfoRequestItem> cartItems, String clientSecret, User user) {
		
		Order order = cartItemsToOrder(cartItems, clientSecret, user);
		Order saved = orderRepository.save(order);
		
		return orderToDTO(saved);
	}
	
	public OrderDTO updateBillingAndShipping(UpdateBillingAndShippingRequest update,  User user) {
		
		Order order = orderRepository.findById(update.getId())
				.orElseThrow(() -> new IllegalStateException("Invalid order ID: " + update.getId()));	
		
		order.setCreatedDate(Instant.now());
		
		BillingDetails billing = dtoToBillingDetails(update.getBillingDetails());
		billing.setOrder(order);
		order.setBillingDetails(billing);
		ShippingDetails shipping = dtoToShippingDetails(update.getShippingDetails());
		if(shipping != null)
			shipping.setOrder(order);
		order.setShippingDetails(shipping);
		order.setShippingSameAsBilling(update.isShippingSameAsBilling());
		Order saved = orderRepository.save(order);
		
		return orderToDTO(saved);
	}
	
	public OrderDTO save(OrderDTO orderDTO) {
		
		Order order = dtoToOrder(orderDTO);
		Order saved = orderRepository.save(order);
		
		return orderToDTO(saved);
	}
	
	public OrderDTO updateStatusByClientSecret(String clientSecret, OrderStatus status) {
		Order order = orderRepository.findByClientSecret(clientSecret)
			.orElseThrow(() -> new IllegalStateException("Could not find Order with Client Secret: " + clientSecret));
		
		order.setStatus(OrderStatus.PROCESSING);
		
		Order saved = orderRepository.save(order);
		
		
		System.out.println("ORDER STATUS UPDATED!!!");
		
		return orderToDTO(saved);
	}
	
	private Order cartItemsToOrder(List<CartInfoRequestItem> cartItems, String clientSecret, User user) {
		Order order = new Order();
		order.setClientSecret(clientSecret);
		
		List<OrderItem> items = new ArrayList<>();
		for(CartInfoRequestItem requestItem : cartItems) {
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
		
		
		BillingDetails entity = new BillingDetails();
		if(dto.getId() != null)
			entity.setId(dto.getId());
		entity.setAddress(dto.getAddress());
		entity.setEmail(dto.getEmail());
		entity.setPhone(dto.getPhone());
		entity.setName(dto.getName());
		if(dto.getOrderId() != null) {
			Order order = orderRepository.findById(dto.getOrderId()).orElse(null);
			entity.setOrder(order);
			}
		
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
		
		if(dto == null)
			return null;
		
		ShippingDetails entity = new ShippingDetails();
		if(dto.getId() != null)
			entity.setId(dto.getId());
		entity.setAddress(dto.getAddress());
		entity.setPhone(dto.getPhone());
		entity.setName(dto.getName());
		if(dto.getOrderId() != null) {
			Order order = orderRepository.findById(dto.getOrderId()).orElse(null);
			entity.setOrder(order);
		}
		return entity;
	}


}
