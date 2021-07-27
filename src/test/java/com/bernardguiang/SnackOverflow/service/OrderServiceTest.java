package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.dto.BillingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.ShippingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.request.UpdateBillingAndShippingRequest;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponse;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponseItem;
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

class OrderServiceTest {

	private OrderService underTest;

	private OrderRepository orderRepository;
	private ProductRepository productRepository;
	private UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		orderRepository = Mockito.mock(OrderRepository.class);
		productRepository = Mockito.mock(ProductRepository.class);
		userRepository = Mockito.mock(UserRepository.class);

		underTest = new OrderService(orderRepository, productRepository, userRepository);
	}

	// TODO: This is way too many assertions? And its mainly testing the DTO
	// conversion. Transfer over the assertions to DTO conversion test and just use
	// minimal input here
	@Test
	void itShouldFindByIdAndUserId() {
		// Given
		Long id = 1L;
		Long userId = 1L;
		BigDecimal total = new BigDecimal(20);
		OrderStatus status = OrderStatus.PROCESSING;
		Instant createDate = Instant.now();
		boolean isShippingSameAsBilling = false;

		Order order = new Order();
		order.setId(id);
		order.setTotal(total);
		order.setCreatedDate(createDate);
		order.setShippingSameAsBilling(isShippingSameAsBilling);
		order.setStatus(status);

		List<OrderItem> items = new ArrayList<>();
		OrderItem item1 = new OrderItem();
		item1.setId(1L);
		item1.setOrder(order);
		item1.setPrice(new BigDecimal(2));
		Product product1 = new Product();
		product1.setId(1L);
		item1.setProduct(product1);
		item1.setQuantity(5);
		OrderItem item2 = new OrderItem();
		item2.setId(2L);
		item2.setOrder(order);
		item2.setPrice(new BigDecimal(5));
		Product product2 = new Product();
		product2.setId(2L);
		item2.setProduct(product2);
		item2.setQuantity(2);
		items.add(item1);
		items.add(item2);
		order.setItems(items);

		User user = new User();
		user.setId(userId);
		order.setUser(user);

		String name = "First Last";
		String email = "my@email.com";
		String phone = "1234567890";
		String addressLineOne = "address line 1";
		String addressLineTwo = "address line 2";
		String city = "city";
		String state = "state";
		String postalCode = "postal";
		String country = "country";

		Address address = new Address();
		address.setAddressLineOne(addressLineOne);
		address.setAddressLineTwo(addressLineTwo);
		address.setCity(city);
		address.setState(state);
		address.setPostalCode(postalCode);
		address.setCountry(country);

		BillingDetails billingDetails = new BillingDetails();
		billingDetails.setId(1L);
		billingDetails.setName(name);
		billingDetails.setEmail(email);
		billingDetails.setPhone(phone);
		billingDetails.setAddress(address);
		billingDetails.setOrder(order);
		order.setBillingDetails(billingDetails);

		ShippingDetails shippingDetails = new ShippingDetails();
		shippingDetails.setId(1L);
		shippingDetails.setName(name);
		shippingDetails.setPhone(phone);
		shippingDetails.setAddress(address);
		shippingDetails.setOrder(order);
		order.setShippingDetails(shippingDetails);

		Optional<Order> orderOptional = Optional.ofNullable(order);

		// When
		when(orderRepository.findByIdAndUserId(id, userId)).thenReturn(orderOptional);
		OrderResponse orderResponse = underTest.findByIdAndUserId(id, userId);

		// Then
		assertEquals(id, orderResponse.getId());
		assertEquals(userId, orderResponse.getUserId());
		assertEquals(total, orderResponse.getTotal());
		assertEquals(status, orderResponse.getStatus());
		assertEquals(userId, orderResponse.getUserId());
		assertEquals(isShippingSameAsBilling, orderResponse.getIsShippingSameAsBilling());

		// ... compare billing
		assertEquals(1L, orderResponse.getBillingDetails().getId());
		assertEquals(name, orderResponse.getBillingDetails().getName());
		assertEquals(email, orderResponse.getBillingDetails().getEmail());
		assertEquals(id, orderResponse.getBillingDetails().getOrderId());
		assertEquals(phone, orderResponse.getBillingDetails().getPhone());
		assertEquals(addressLineOne, orderResponse.getBillingDetails().getAddress().getAddressLineOne());
		assertEquals(addressLineTwo, orderResponse.getBillingDetails().getAddress().getAddressLineTwo());
		assertEquals(city, orderResponse.getBillingDetails().getAddress().getCity());
		assertEquals(state, orderResponse.getBillingDetails().getAddress().getState());
		assertEquals(postalCode, orderResponse.getBillingDetails().getAddress().getPostalCode());
		assertEquals(country, orderResponse.getBillingDetails().getAddress().getCountry());

		// ... compare shipping
		assertEquals(1L, orderResponse.getShippingDetails().getId());
		assertEquals(name, orderResponse.getShippingDetails().getName());
		assertEquals(id, orderResponse.getShippingDetails().getOrderId());
		assertEquals(phone, orderResponse.getShippingDetails().getPhone());
		assertEquals(addressLineOne, orderResponse.getShippingDetails().getAddress().getAddressLineOne());
		assertEquals(addressLineTwo, orderResponse.getShippingDetails().getAddress().getAddressLineTwo());
		assertEquals(city, orderResponse.getShippingDetails().getAddress().getCity());
		assertEquals(state, orderResponse.getShippingDetails().getAddress().getState());
		assertEquals(postalCode, orderResponse.getShippingDetails().getAddress().getPostalCode());
		assertEquals(country, orderResponse.getShippingDetails().getAddress().getCountry());

		// .. compare order items
		OrderResponseItem responseItem1 = orderResponse.getItems().get(0);
		OrderResponseItem responseItem2 = orderResponse.getItems().get(1);
		assertEquals(item1.getId(), responseItem1.getId());
		assertEquals(item1.getPrice(), responseItem1.getPrice());
		assertEquals(item1.getQuantity(), responseItem1.getQuantity());
		assertEquals(item1.getProduct().getId(), responseItem1.getProduct().getId());

		assertEquals(item2.getId(), responseItem2.getId());
		assertEquals(item2.getPrice(), responseItem2.getPrice());
		assertEquals(item2.getQuantity(), responseItem2.getQuantity());
		assertEquals(item2.getProduct().getId(), responseItem2.getProduct().getId());
	}

	@Test
	void itShouldThrowAnExceptionWhenNotFound() {
		// Given
		Long id = 1L;
		Long userId = 1L;
		Optional<Order> orderOptional = Optional.ofNullable(null);
		// When
		when(orderRepository.findByIdAndUserId(Mockito.any(), Mockito.any())).thenReturn(orderOptional);

		// Then
		assertThrows(IllegalStateException.class, () -> underTest.findByIdAndUserId(id, userId),
				"Could not find Order with id: " + id + " and userId: " + userId);
	}

	@Test
	void itShouldFindAllByUserAndStatusNot() {
		// Given
		Long id = 1L;
		OrderStatus status = OrderStatus.CREATED;

		User user = new User();
		user.setId(id);
		List<Order> orders = new ArrayList<>();
		Order order1 = new Order();
		List<OrderItem> items1 = new ArrayList<>();
		Product p1 = new Product();
		p1.setId(1L);
		OrderItem orderItem1 = new OrderItem();
		orderItem1.setId(1L);
		orderItem1.setProduct(p1);
		items1.add(orderItem1);
		order1.setItems(items1);
		order1.setUser(user);

		Order order2 = new Order();
		List<OrderItem> items2 = new ArrayList<>();
		Product p2 = new Product();
		p2.setId(2L);
		OrderItem orderItem2 = new OrderItem();
		orderItem2.setId(2L);
		orderItem2.setProduct(p2);
		items2.add(orderItem2);
		order2.setItems(items2);
		order2.setUser(user);

		orders.add(order1);
		orders.add(order2);

		UserDTO userDTO = new UserDTO();
		userDTO.setId(id);

		// When
		when(orderRepository.findAllByUserIdAndStatusNot(id, status)).thenReturn(orders);

		// Then
		List<OrderResponse> orderList = underTest.findAllByUserAndStatusNot(userDTO, status);

		assertEquals(2, orderList.size());
	}

	@Test
	void itShouldCreateOrderWithCartItemsAndClientSecret() {
		// Given
		Long productId = 1L;
		Product product = new Product();
		product.setId(productId);
		product.setPrice(new BigDecimal(2));
		Optional<Product> productOptional = Optional.of(product);

		Long userId = 20L;
		User user = new User();
		user.setId(userId);
		Optional<User> userOptional = Optional.of(user);

		Long orderId = 8L;
		
		Order order = new Order();
		order.setUser(user);
		order.setStatus(OrderStatus.CREATED);

		List<CartInfoRequestItem> cartItems = new ArrayList<>();
		CartInfoRequestItem item = new CartInfoRequestItem();
		item.setProductId(productId);
		item.setQuantity(5);
		cartItems.add(item);
		String clientSecret = "client secret";

		// When
		when(productRepository.findById(productId)).thenReturn(productOptional);
		when(userRepository.findById(userId)).thenReturn(userOptional);
		when(orderRepository.save(
				Mockito.argThat((Order o) -> (o.getStatus() == OrderStatus.CREATED && o.getUser().getId() == userId
						&& o.getTotal().equals(new BigDecimal(10)) && o.getClientSecret() == clientSecret
						&& o.getItems().get(0).getProduct().getPrice().equals(new BigDecimal(2))
						&& o.getItems().get(0).getProduct().getId() == productId)))).thenAnswer(
								new Answer() {
									@Override
									public Object answer(InvocationOnMock invocation) throws Throwable {
										Object[] args = invocation.getArguments();
										Order savedOrder = (Order) args[0];
										savedOrder.setId(orderId);
										return savedOrder;
									}
						});

		// Then
		Long savedOrderId = underTest.createOrderWithCartItemsAndClientSecret(cartItems, clientSecret, userId);

		assertEquals(orderId, savedOrderId);
	}

	@Test
	void itShouldThrowAnExceptionWhenUserDoesNotExist() {
		// Given
		Long productId = 1L;
		Product product = new Product();
		product.setId(productId);
		product.setPrice(new BigDecimal(2));
		Optional<Product> productOptional = Optional.ofNullable(product);

		Long userId = 20L;

		List<CartInfoRequestItem> cartItems = new ArrayList<>();
		CartInfoRequestItem item = new CartInfoRequestItem();
		item.setProductId(productId);
		item.setQuantity(5);
		cartItems.add(item);

		Optional<User> userOptional = Optional.ofNullable(null);
		;

		String clientSecret = "client secret";

		// When
		when(productRepository.findById(productId)).thenReturn(productOptional);
		when(userRepository.findById(userId)).thenReturn(userOptional);

		// Then
		assertThrows(IllegalStateException.class,
				() -> underTest.createOrderWithCartItemsAndClientSecret(cartItems, clientSecret, userId),
				"Could not find user with id: " + userId);
	}

	@Test
	void itShouldThrowAnExceptionWhenProductDoesNotExist() {
		// Given
		Long productId = 1L;
		Product product = null;
		Optional<Product> productOptional = Optional.ofNullable(product);

		Long userId = 20L;

		List<CartInfoRequestItem> cartItems = new ArrayList<>();
		CartInfoRequestItem item = new CartInfoRequestItem();
		item.setProductId(productId);
		item.setQuantity(5);
		cartItems.add(item);

		String clientSecret = "client secret";

		// When
		when(productRepository.findById(productId)).thenReturn(productOptional);

		// Then
		assertThrows(IllegalStateException.class,
				() -> underTest.createOrderWithCartItemsAndClientSecret(cartItems, clientSecret, userId),
				"Could not find product with id: " + productId);
	}
	
	@Test
	void itShouldUpdateBillingAndShipping() {
		// Given		
		Long orderId = 5L;
		UpdateBillingAndShippingRequest update = new UpdateBillingAndShippingRequest();
		update.setId(orderId);

		String name = "First Last";
		String email = "my@email.com";
		String phone = "1234567890";
		String addressLineOne = "address line 1";
		String addressLineTwo = "address line 2";
		String city = "city";
		String state = "state";
		String postalCode = "postal";
		String country = "country";

		Address address = new Address();
		address.setAddressLineOne(addressLineOne);
		address.setAddressLineTwo(addressLineTwo);
		address.setCity(city);
		address.setState(state);
		address.setPostalCode(postalCode);
		address.setCountry(country);

		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
		billingDetails.setId(1L);
		billingDetails.setName(name);
		billingDetails.setEmail(email);
		billingDetails.setPhone(phone);
		billingDetails.setAddress(address);
		billingDetails.setOrderId(orderId);

		ShippingDetailsDTO shippingDetails = new ShippingDetailsDTO();
		shippingDetails.setId(1L);
		shippingDetails.setName(name);
		shippingDetails.setPhone(phone);
		shippingDetails.setAddress(address);
		shippingDetails.setOrderId(orderId);

		update.setBillingDetails(billingDetails);
		boolean isShippingSameAsBilling = false;
		update.setShippingSameAsBilling(isShippingSameAsBilling);
		update.setShippingDetails(shippingDetails);

		Long userId = 2L;
		UserDTO userDTO = new UserDTO();
		userDTO.setId(userId);

		User user = new User();
		user.setId(userId);
		List<OrderItem> items = new ArrayList<>();
		
		Order order = new Order();
		order.setItems(items);
		order.setId(orderId);
		order.setUser(user);
		Optional<Order> orderOptional = Optional.of(order);

		// When
		when(orderRepository.findByIdAndUserId(orderId, userId)).thenReturn(orderOptional);
		when(orderRepository.save(order)).thenAnswer(
				new Answer() {
					@Override
					public Object answer(InvocationOnMock invocation) throws Throwable {
						Object[] args = invocation.getArguments();
						Order savedOrder = (Order) args[0];
						savedOrder.setId(orderId);
						return savedOrder;
					}
		});

		// Then
		OrderResponse orderResponse = underTest.updateBillingAndShipping(update, userDTO);

		assertEquals(orderId, orderResponse.getId());
		assertEquals(userId, orderResponse.getUserId());
		assertTrue(orderResponse.getCreatedDate() != null);
		
		// Billing And Shipping Details
		assertEquals(isShippingSameAsBilling, orderResponse.getIsShippingSameAsBilling());

		// ... compare billing
		assertEquals(1L, orderResponse.getBillingDetails().getId());
		assertEquals(name, orderResponse.getBillingDetails().getName());
		assertEquals(email, orderResponse.getBillingDetails().getEmail());
		assertEquals(orderId, orderResponse.getBillingDetails().getOrderId());
		assertEquals(phone, orderResponse.getBillingDetails().getPhone());
		assertEquals(addressLineOne, orderResponse.getBillingDetails().getAddress().getAddressLineOne());
		assertEquals(addressLineTwo, orderResponse.getBillingDetails().getAddress().getAddressLineTwo());
		assertEquals(city, orderResponse.getBillingDetails().getAddress().getCity());
		assertEquals(state, orderResponse.getBillingDetails().getAddress().getState());
		assertEquals(postalCode, orderResponse.getBillingDetails().getAddress().getPostalCode());
		assertEquals(country, orderResponse.getBillingDetails().getAddress().getCountry());

		// ... compare shipping
		assertEquals(1L, orderResponse.getShippingDetails().getId());
		assertEquals(name, orderResponse.getShippingDetails().getName());
		assertEquals(orderId, orderResponse.getShippingDetails().getOrderId());
		assertEquals(phone, orderResponse.getShippingDetails().getPhone());
		assertEquals(addressLineOne, orderResponse.getShippingDetails().getAddress().getAddressLineOne());
		assertEquals(addressLineTwo, orderResponse.getShippingDetails().getAddress().getAddressLineTwo());
		assertEquals(city, orderResponse.getShippingDetails().getAddress().getCity());
		assertEquals(state, orderResponse.getShippingDetails().getAddress().getState());
		assertEquals(postalCode, orderResponse.getShippingDetails().getAddress().getPostalCode());
		assertEquals(country, orderResponse.getShippingDetails().getAddress().getCountry());
	}
	
	@Test
	void shippingShouldBeNullWhenUpdatingBillingAndShippingWithBillingSameAsShipping() {
		// Given
		Long orderId = 5L;
		UpdateBillingAndShippingRequest update = new UpdateBillingAndShippingRequest();
		update.setId(orderId);

//		String name = "First Last";
//		String email = "my@email.com";
//		String phone = "1234567890";
//		String addressLineOne = "address line 1";
//		String addressLineTwo = "address line 2";
//		String city = "city";
//		String state = "state";
//		String postalCode = "postal";
//		String country = "country";

//		Address address = new Address();
//		address.setAddressLineOne(addressLineOne);
//		address.setAddressLineTwo(addressLineTwo);
//		address.setCity(city);
//		address.setState(state);
//		address.setPostalCode(postalCode);
//		address.setCountry(country);

		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
//		billingDetails.setId(1L);
//		billingDetails.setName(name);
//		billingDetails.setEmail(email);
//		billingDetails.setPhone(phone);
//		billingDetails.setAddress(address);
//		billingDetails.setOrderId(orderId);

		ShippingDetailsDTO shippingDetails = new ShippingDetailsDTO();
//		shippingDetails.setId(1L);
//		shippingDetails.setName(name);
//		shippingDetails.setPhone(phone);
//		shippingDetails.setAddress(address);
//		shippingDetails.setOrderId(orderId);

		update.setBillingDetails(billingDetails);
		boolean isShippingSameAsBilling = true;
		update.setShippingSameAsBilling(isShippingSameAsBilling);
		update.setShippingDetails(shippingDetails);

		Long userId = 2L;
		UserDTO userDTO = new UserDTO();
		userDTO.setId(userId);

		User user = new User();
		user.setId(userId);
		List<OrderItem> items = new ArrayList<>();
		
		Order order = new Order();
		order.setItems(items);
		order.setId(orderId);
		order.setUser(user);
		Optional<Order> orderOptional = Optional.of(order);

		// When
		when(orderRepository.findByIdAndUserId(orderId, userId)).thenReturn(orderOptional);
		when(orderRepository.save(order)).thenAnswer(
				new Answer() {
					@Override
					public Object answer(InvocationOnMock invocation) throws Throwable {
						Object[] args = invocation.getArguments();
						Order savedOrder = (Order) args[0];
						savedOrder.setId(orderId);
						return savedOrder;
					}
		});

		// Then
		OrderResponse orderResponse = underTest.updateBillingAndShipping(update, userDTO);

		assertEquals(orderId, orderResponse.getId());
		assertEquals(userId, orderResponse.getUserId());
		assertTrue(orderResponse.getCreatedDate() != null);
		
		// Shipping Info
		assertEquals(isShippingSameAsBilling, orderResponse.getIsShippingSameAsBilling());
		assertEquals(null, orderResponse.getShippingDetails());
	}
	
	@Test
	void updateBillingAndShippingShouldThrowAnErrorWhenOrderDoesNotExist() {
		// Given
		Long orderId = 5L;
		UpdateBillingAndShippingRequest update = new UpdateBillingAndShippingRequest();
		update.setId(orderId);

		Long userId = 2L;
		UserDTO userDTO = new UserDTO();
		userDTO.setId(userId);

		Optional<Order> orderOptional = Optional.ofNullable(null);

		// When
		when(orderRepository.findByIdAndUserId(orderId, userId)).thenReturn(orderOptional);

		// Then
		assertThrows(IllegalStateException.class, ()->underTest.updateBillingAndShipping(update, userDTO),
				"Order " + update.getId() + " does not exist for user " + userDTO.getId());
	}
	
	@Test
	void itShouldUpdateStatusByClientSecret() {
		// Given
		String clientSecret = "stripe client secret";
		OrderStatus initialStatus = OrderStatus.CREATED;
		OrderStatus statusUpdate = OrderStatus.PROCESSING;
		Long orderId = 5L;
		Long userId = 2L;
		
		UserDTO userDTO = new UserDTO();
		userDTO.setId(userId);

		User user = new User();
		user.setId(userId);
		List<OrderItem> items = new ArrayList<>();
		
		Order order = new Order();
		order.setStatus(initialStatus);
		order.setItems(items);
		order.setUser(user);
		
		Optional<Order> orderOptional = Optional.of(order);
		
		// When
		when(orderRepository.findByClientSecret(clientSecret)).thenReturn(orderOptional);
		when(orderRepository.save(order)).thenAnswer(
				new Answer() {
					@Override
					public Object answer(InvocationOnMock invocation) throws Throwable {
						Object[] args = invocation.getArguments();
						Order savedOrder = (Order) args[0];
						savedOrder.setId(orderId);
						return savedOrder;
					}
		});

		// Then
		OrderResponse orderResponse = underTest.updateStatusByClientSecret(clientSecret, statusUpdate);
		assertEquals(orderId, orderResponse.getId());
		assertEquals(statusUpdate, orderResponse.getStatus());
	}
	
	@Test
	void itShouldThrowAnExceptionWhenClientSecretDoesNotExist() {
		// Given
		String clientSecret = "stripe client secret";
		OrderStatus statusUpdate = OrderStatus.PROCESSING;
		
		Order order = null;
		Optional<Order> orderOptional = Optional.ofNullable(order);
		// When
		when(orderRepository.findByClientSecret(clientSecret)).thenReturn(orderOptional);

		// Then
		assertThrows(IllegalStateException.class, ()->underTest.updateStatusByClientSecret(clientSecret, statusUpdate),
				"Could not find Order with Client Secret: " + clientSecret);
	}
}
