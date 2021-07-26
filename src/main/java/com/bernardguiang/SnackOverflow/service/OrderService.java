package com.bernardguiang.SnackOverflow.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.BillingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.ShippingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.UserDTO;
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
	
	public Long createOrderWithCartItemsAndClientSecret(List<CartInfoRequestItem> cartItems, String clientSecret, Long userId) {
		
		Order order = new Order();
		
		BigDecimal total = getCartTotal(cartItems);
		
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
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalStateException("Invalid user ID: " + userId));
		order.setItems(items);
		order.setTotal(total);
		order.setUser(user);
		order.setClientSecret(clientSecret);
		order.setStatus(OrderStatus.CREATED);
		
		
		Order saved = orderRepository.save(order);
		
		return saved.getId();
	}
	
	public OrderResponse updateBillingAndShipping(UpdateBillingAndShippingRequest update, UserDTO user) {
		
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
		
		return new OrderResponse(saved);
	}
	
//	public OrderDTO save(OrderDTO orderDTO) {
//		
//		Order order = dtoToOrder(orderDTO);
//		Order saved = orderRepository.save(order);
//		
//		return new OrderDTO(saved);
//	}
	
	public OrderResponse updateStatusByClientSecret(String clientSecret, OrderStatus status) {
		Order order = orderRepository.findByClientSecret(clientSecret)
			.orElseThrow(() -> new IllegalStateException("Could not find Order with Client Secret: " + clientSecret));
		
		order.setStatus(OrderStatus.PROCESSING);
		
		Order saved = orderRepository.save(order);
		
		
		System.out.println("ORDER STATUS UPDATED!!!");
		
		return new OrderResponse(saved);
	}
	
	private BigDecimal getCartTotal(List<CartInfoRequestItem> cartItems) {
		BigDecimal total = new BigDecimal("0");
		for(CartInfoRequestItem item : cartItems) {
			
			Product product = productRepository.findById(item.getProductId())
					.orElseThrow(() -> new IllegalStateException("Could not find product with id: " + item.getProductId()));
			
			total = total.add(product.getPrice().multiply(new BigDecimal(item.getQuantity())));
		}
		
		return total;
	}
	
//		
//		Order order = new Order();
//		User user = userRepository.findById(orderDTO.getUserId())
//				.orElseThrow(() -> new IllegalStateException("Could not find user with id: " + orderDTO.getUserId()));
//		
//		List<OrderItem> items = new ArrayList<>();
//		for(OrderItemDTO dto : orderDTO.getItems()) {
//			ProductDTO productDTO = dto.getProduct();
//			//Product product = productService.dtoToProduct(productDTO); // Dont use product from frontend as it might update the product
//			Product product = productRepository.findById(productDTO.getId()) // Load product from repository
//				.orElseThrow(() -> new IllegalStateException("Could not find product with id: " + productDTO.getId()));
//			OrderItem item = new OrderItem();
//			item.setId(dto.getId());
//			item.setOrder(order); // set order
//			item.setProduct(product); // set product
//			item.setPrice(dto.getPrice());
//			item.setQuantity(dto.getQuantity());
//			
//			items.add(item);
//		}
//		
//		order.setId(orderDTO.getId());
//		order.setItems(items);
//		order.setTotal(orderDTO.getTotal());
//		order.setCreatedDate(orderDTO.getCreatedDate());
//		order.setBillingDetails(dtoToBillingDetails(orderDTO.getBillingDetails()));
//		order.setShippingDetails(dtoToShippingDetails(orderDTO.getShippingDetails()));
//		order.setShippingSameAsBilling(orderDTO.getIsShippingSameAsBilling());
//		order.setUser(user);
//		if(orderDTO.getStatus() != null)
//			order.setStatus(orderDTO.getStatus());
//		
//		return order;
//	}
	
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
