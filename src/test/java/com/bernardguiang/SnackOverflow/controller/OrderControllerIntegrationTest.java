package com.bernardguiang.SnackOverflow.controller;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.dto.BillingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartRequest;
import com.bernardguiang.SnackOverflow.dto.request.OrderPage;
import com.bernardguiang.SnackOverflow.dto.request.OrderStatusUpdateRequest;
import com.bernardguiang.SnackOverflow.dto.request.StatsRequest;
import com.bernardguiang.SnackOverflow.dto.response.OrderDTO;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponse;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponseItem;
import com.bernardguiang.SnackOverflow.dto.response.OrderStatsResponse;
import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;
import com.bernardguiang.SnackOverflow.service.ApplicationUserDetailsService;
import com.bernardguiang.SnackOverflow.service.AuthService;
import com.bernardguiang.SnackOverflow.service.CartService;
import com.bernardguiang.SnackOverflow.service.JwtService;
import com.bernardguiang.SnackOverflow.service.OrderService;
import com.bernardguiang.SnackOverflow.service.StripeService;
import com.bernardguiang.SnackOverflow.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;

@WebMvcTest(OrderController.class)
class OrderControllerIntegrationTest {

	// Order Controller Dependencies
	@MockBean
	private OrderService orderService;
	@MockBean
	private UserService userService;
	@MockBean
	private StripeService stripeService;
	@MockBean
	private CartService cartService;

	// JWT Auth Filter Dependencies
	@MockBean
	private AuthService authService;
	@MockBean
	private PasswordEncoder passwordEncoder;
	@MockBean
	private ApplicationUserDetailsService applicationUserDetailsService;
	@MockBean
	private JwtService jwtService;

	@Autowired
	private MockMvc mockMvc;

	private static List<Map<String, String>> customerRoleAuthorities;
	private static List<Map<String, String>> adminRoleAuthorities;

	private final Long customerId = 5L;
	private final String customerUsername = "customerUsername";
	private final String customerEmail = "customer@email.com";
	private final String customerRole = ApplicationUserRole.CUSTOMER.name();
	private final String customerAuthToken = "customerAuthToken";
	
	private final Long adminId = 1L;
	private final String adminUsername = "adminUsername";
	private final String adminEmail = "admin@email.com";
	private final String adminRole = ApplicationUserRole.ADMIN.name();
	private final String adminAuthToken = "adminAuthToken";

	@BeforeAll
	static void setupBeforeAll() {
		customerRoleAuthorities = new ArrayList<>();
		
		Map<String, String> customerAuth1 = new HashMap<>();
		customerAuth1.put("authority", "ROLE_CUSTOMER");
		customerRoleAuthorities.add(customerAuth1);
		Map<String, String> customerAuth2 = new HashMap<>();
		customerAuth2.put("authority", "order:write");
		customerRoleAuthorities.add(customerAuth2);
		Map<String, String> customerAuth3 = new HashMap<>();
		customerAuth3.put("authority", "category:read");
		customerRoleAuthorities.add(customerAuth3);
		Map<String, String> customerAuth4 = new HashMap<>();
		customerAuth4.put("authority", "product:read");
		customerRoleAuthorities.add(customerAuth4);
		Map<String, String> customerAuth5 = new HashMap<>();
		customerAuth5.put("authority", "order:read");
		customerRoleAuthorities.add(customerAuth5);
		
		
		adminRoleAuthorities = new ArrayList<>();
		
		Map<String, String> adminAuth1 = new HashMap<>();
		adminAuth1.put("authority", "ROLE_ADMIN");
		adminRoleAuthorities.add(adminAuth1);
		Map<String, String> adminAuth2 = new HashMap<>();
		adminAuth2.put("authority", "order:write");
		adminRoleAuthorities.add(adminAuth2);
		Map<String, String> adminAuth3 = new HashMap<>();
		adminAuth3.put("authority", "category:read");
		adminRoleAuthorities.add(adminAuth3);
		Map<String, String> adminAuth4 = new HashMap<>();
		adminAuth4.put("authority", "product:read");
		adminRoleAuthorities.add(adminAuth4);
		Map<String, String> adminAuth5 = new HashMap<>();
		adminAuth5.put("authority", "order:read");
		adminRoleAuthorities.add(adminAuth5);
		
		Map<String, String> adminAuth6 = new HashMap<>();
		adminAuth6.put("authority", "product:write");
		adminRoleAuthorities.add(adminAuth6);
		Map<String, String> adminAuth7 = new HashMap<>();
		adminAuth7.put("authority", "category:write");
		adminRoleAuthorities.add(adminAuth7);
	}

	void setCustomerAuthorization() {
		// ... Handle token authorization via filter
		Claims tokenPayload = Mockito.mock(Claims.class);
		when(tokenPayload.getSubject()).thenReturn(customerUsername);
		when(tokenPayload.get("authorities")).thenReturn(customerRoleAuthorities);
		when(jwtService.getTokenPayload(customerAuthToken)).thenReturn(tokenPayload);
	}
	
	void setAdminAuthorization() {
		// ... Handle token authorization via filter
		Claims tokenPayload = Mockito.mock(Claims.class);
		when(tokenPayload.getSubject()).thenReturn(adminUsername);
		when(tokenPayload.get("authorities")).thenReturn(adminRoleAuthorities);
		when(jwtService.getTokenPayload(adminAuthToken)).thenReturn(tokenPayload);
	}

	@Test
	void getOrdersByCurrentUser() throws Exception {
		// Given
		UserDTO user = new UserDTO();
		user.setEmail(customerEmail);
		user.setFullName("Full Name");
		user.setId(customerId);
		user.setRole(customerRole);
		user.setUsername(customerUsername);

		OrderResponse orderResponseMock = Mockito.mock(OrderResponse.class);
		List<OrderResponse> orderResponseList = Arrays.asList(orderResponseMock);

		// When
		setCustomerAuthorization();
		
		when(orderResponseMock.getId()).thenReturn(1L);

		when(userService.findByUsername(customerUsername)).thenReturn(user);

		when(orderService.findAllByUserId(customerId)).thenReturn(orderResponseList);

		// Then
		mockMvc.perform(get("/api/v1/orders").header("Authorization", "Bearer " + customerAuthToken)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$[0].id").value(1L));
	}
	
	@Test
	void getOrderByCurrentUser() throws Exception {
		// Given
		UserDTO userDTO = new UserDTO();
		userDTO.setId(customerId);
		
		User user = new User();
		user.setId(customerId);

		BillingDetails billingDetails = new BillingDetails();
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		billingDetails.setAddress(address);
		billingDetails.setEmail(customerEmail);
		billingDetails.setId(1L);
		billingDetails.setName("Billing name");
		billingDetails.setPhone("1234567890");

		Product product = new Product();
		product.setId(3L);
		product.setName("Chips");
		product.setDescription("A Bag of Chips");
		product.setPrice(new BigDecimal(3));
		product.setCreatedDate(Instant.now());
		List<String> productImages = Arrays.asList("Image 1", "Image 2", "Image 3");
		product.setImages(productImages);
		Category category = new Category();
		category.setId(1L);
		category.setName("Junk Food");
		Set<Category> productCategories = new HashSet<>(
				Arrays.asList(category));
		product.setCategories(productCategories);
		
		Long orderId = 1L;
		Instant createdDate = Instant.now();
		OrderStatus status = OrderStatus.PROCESSING;
		BigDecimal total = new BigDecimal(20);
		List<OrderItem> items = new ArrayList<>();
		OrderItem item = new OrderItem();
		Long orderItemId = 20L;
		item.setId(orderItemId);
		item.setProduct(product);
		item.setPrice(new BigDecimal(5));
		items.add(item);

		Order order = new Order();
		order.setId(orderId);
		order.setCreatedDate(createdDate);
		order.setStatus(status);
		order.setBillingDetails(billingDetails);
		billingDetails.setOrder(order);
		order.setShippingSameAsBilling(true);
		order.setShippingDetails(null);
		order.setItems(items);
		order.setTotal(total);
		order.setUser(user);
		item.setOrder(order);
		
		OrderResponse orderResponse = new OrderResponse(order);

		// When
		setCustomerAuthorization();

		when(userService.findByUsername(customerUsername)).thenReturn(userDTO);

		when(orderService.findByIdAndUserId(orderId, customerId)).thenReturn(orderResponse);

		// Then
		mockMvc.perform(get("/api/v1/orders/1").header("Authorization", "Bearer " + customerAuthToken)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(orderId))
				.andExpect(jsonPath("$.items").isArray())
				.andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$.items[0].id").value(orderItemId));// TODO: create a separate Serialization unit test
	}
	
	@Test
	void getOrders() throws Exception {
		// Given
		OrderPage page = new OrderPage();
		page.setSearch("chip");
		
		User user = new User();
		user.setId(customerId);

		BillingDetails billingDetails = new BillingDetails();
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		billingDetails.setAddress(address);
		billingDetails.setEmail(customerEmail);
		billingDetails.setId(1L);
		billingDetails.setName("Billing name");
		billingDetails.setPhone("1234567890");

		Product product = new Product();
		product.setId(3L);
		product.setName("Chips");
		product.setDescription("A Bag of Chips");
		product.setPrice(new BigDecimal(3));
		product.setCreatedDate(Instant.now());
		List<String> productImages = Arrays.asList("Image 1", "Image 2", "Image 3");
		product.setImages(productImages);
		Category category = new Category();
		category.setId(1L);
		category.setName("Junk Food");
		Set<Category> productCategories = new HashSet<>(
				Arrays.asList(category));
		product.setCategories(productCategories);
		
		Long orderId = 1L;
		Instant createdDate = Instant.now();
		OrderStatus status = OrderStatus.PROCESSING;
		BigDecimal total = new BigDecimal(20);
		List<OrderItem> items = new ArrayList<>();
		OrderItem item = new OrderItem();
		Long orderItemId = 20L;
		item.setId(orderItemId);
		item.setProduct(product);
		item.setPrice(new BigDecimal(5));
		items.add(item);

		Order order = new Order();
		order.setId(orderId);
		order.setCreatedDate(createdDate);
		order.setStatus(status);
		order.setBillingDetails(billingDetails);
		billingDetails.setOrder(order);
		order.setShippingSameAsBilling(true);
		order.setShippingDetails(null);
		order.setItems(items);
		order.setTotal(total);
		order.setUser(user);
		item.setOrder(order);
		
		OrderDTO orderResponse = new OrderDTO(order);
		
		List<OrderDTO> content = Arrays.asList(orderResponse);
		Page<OrderDTO> pageResult = new PageImpl<OrderDTO>(content);

		// When
		setAdminAuthorization();

		when(orderService.findOrdersPaginated(Mockito.argThat(new ArgumentMatcher<OrderPage>() {

			@Override
			public boolean matches(OrderPage argument) {
				return argument.getSearch().equalsIgnoreCase("chip");
			}
		}))).thenReturn(pageResult);

		// Then
		mockMvc.perform(get("/api/v1/admin/orders")
				.param("search", "chip")
				.header("Authorization", "Bearer " + adminAuthToken)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").exists()) // TODO: create a separate Serialization unit test
				.andExpect(jsonPath("$.content").isNotEmpty())
				.andExpect(jsonPath("$.content[0].id").value(orderId))
				.andExpect(jsonPath("$.content[0].status").value(status.name()))
				;
	}
	
	@Test
	void getOrder() throws Exception {
		// Given
		User user = new User();
		user.setId(customerId);

		BillingDetails billingDetails = new BillingDetails();
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		billingDetails.setAddress(address);
		billingDetails.setEmail(customerEmail);
		billingDetails.setId(1L);
		billingDetails.setName("Billing name");
		billingDetails.setPhone("1234567890");

		Product product = new Product();
		product.setId(3L);
		product.setName("Chips");
		product.setDescription("A Bag of Chips");
		product.setPrice(new BigDecimal(3));
		product.setCreatedDate(Instant.now());
		List<String> productImages = Arrays.asList("Image 1", "Image 2", "Image 3");
		product.setImages(productImages);
		Category category = new Category();
		category.setId(1L);
		category.setName("Junk Food");
		Set<Category> productCategories = new HashSet<>(
				Arrays.asList(category));
		product.setCategories(productCategories);
		
		Long orderId = 1L;
		Instant createdDate = Instant.now();
		OrderStatus status = OrderStatus.PROCESSING;
		BigDecimal total = new BigDecimal(20);
		List<OrderItem> items = new ArrayList<>();
		OrderItem item = new OrderItem();
		Long orderItemId = 20L;
		item.setId(orderItemId);
		item.setProduct(product);
		item.setPrice(new BigDecimal(5));
		items.add(item);

		Order order = new Order();
		order.setId(orderId);
		order.setCreatedDate(createdDate);
		order.setStatus(status);
		order.setBillingDetails(billingDetails);
		billingDetails.setOrder(order);
		order.setShippingSameAsBilling(true);
		order.setShippingDetails(null);
		order.setItems(items);
		order.setTotal(total);
		order.setUser(user);
		item.setOrder(order);
		
		OrderDTO orderResponse = new OrderDTO(order);

		// When
		setAdminAuthorization();

		when(orderService.findByIdIncludUserInfo(1L)).thenReturn(orderResponse);

		// Then
		mockMvc.perform(get("/api/v1/admin/orders/1")
				.header("Authorization", "Bearer " + adminAuthToken)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").exists()) // TODO: create a separate Serialization unit test
				.andExpect(jsonPath("$.id").value(orderId))
				.andExpect(jsonPath("$.status").value(status.name()))
				;
	}
	
	@Test
	void updateOrderStatus() throws Exception {
		// Given
		OrderStatusUpdateRequest statusUpdateRequest = new OrderStatusUpdateRequest();
		statusUpdateRequest.setId(1L);
		statusUpdateRequest.setStatus(OrderStatus.CANCELLED);
		
		User user = new User();
		user.setId(customerId);

		BillingDetails billingDetails = new BillingDetails();
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		billingDetails.setAddress(address);
		billingDetails.setEmail(customerEmail);
		billingDetails.setId(1L);
		billingDetails.setName("Billing name");
		billingDetails.setPhone("1234567890");

		Product product = new Product();
		product.setId(3L);
		product.setName("Chips");
		product.setDescription("A Bag of Chips");
		product.setPrice(new BigDecimal(3));
		product.setCreatedDate(Instant.now());
		List<String> productImages = Arrays.asList("Image 1", "Image 2", "Image 3");
		product.setImages(productImages);
		Category category = new Category();
		category.setId(1L);
		category.setName("Junk Food");
		Set<Category> productCategories = new HashSet<>(
				Arrays.asList(category));
		product.setCategories(productCategories);
		
		Long orderId = 1L;
		Instant createdDate = Instant.now();
		OrderStatus status = OrderStatus.PROCESSING;
		BigDecimal total = new BigDecimal(20);
		List<OrderItem> items = new ArrayList<>();
		OrderItem item = new OrderItem();
		Long orderItemId = 20L;
		item.setId(orderItemId);
		item.setProduct(product);
		item.setPrice(new BigDecimal(5));
		items.add(item);

		Order order = new Order();
		order.setId(orderId);
		order.setCreatedDate(createdDate);
		order.setStatus(status);
		order.setBillingDetails(billingDetails);
		billingDetails.setOrder(order);
		order.setShippingSameAsBilling(true);
		order.setShippingDetails(null);
		order.setItems(items);
		order.setTotal(total);
		order.setUser(user);
		item.setOrder(order);
		
		OrderResponse orderResponse = new OrderResponse(order);

		// When
		setAdminAuthorization();

		when(orderService.updateOrderStatus(Mockito.argThat(new ArgumentMatcher<OrderStatusUpdateRequest>() {

			@Override
			public boolean matches(OrderStatusUpdateRequest argument) {
				return argument.getId() == 1L && argument.getStatus() == OrderStatus.CANCELLED;
			}
		}))).thenReturn(orderResponse);

		// Then
		mockMvc.perform(put("/api/v1/admin/orders").content(asJsonString(statusUpdateRequest))
				.header("Authorization", "Bearer " + adminAuthToken)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").exists()) // TODO: create a separate Serialization unit test
				.andExpect(jsonPath("$.id").value(orderId))
				.andExpect(jsonPath("$.status").value(status.name()))
				;
	}
	
	@Test
	void getOrderStats() throws Exception {
		// Given
		OrderPage page = new OrderPage();
		page.setSearch("chip");
		
		User user = new User();
		user.setId(customerId);

		BillingDetails billingDetails = new BillingDetails();
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		billingDetails.setAddress(address);
		billingDetails.setEmail(customerEmail);
		billingDetails.setId(1L);
		billingDetails.setName("Billing name");
		billingDetails.setPhone("1234567890");

		Product product = new Product();
		product.setId(3L);
		product.setName("Chips");
		product.setDescription("A Bag of Chips");
		product.setPrice(new BigDecimal(3));
		product.setCreatedDate(Instant.now());
		List<String> productImages = Arrays.asList("Image 1", "Image 2", "Image 3");
		product.setImages(productImages);
		Category category = new Category();
		category.setId(1L);
		category.setName("Junk Food");
		Set<Category> productCategories = new HashSet<>(
				Arrays.asList(category));
		product.setCategories(productCategories);
		
		Long orderId = 1L;
		Instant createdDate = Instant.now();
		OrderStatus status = OrderStatus.PROCESSING;
		BigDecimal total = new BigDecimal(20);
		List<OrderItem> items = new ArrayList<>();
		OrderItem item = new OrderItem();
		Long orderItemId = 20L;
		item.setId(orderItemId);
		item.setProduct(product);
		item.setPrice(new BigDecimal(5));
		items.add(item);

		Order order = new Order();
		order.setId(orderId);
		order.setCreatedDate(createdDate);
		order.setStatus(status);
		order.setBillingDetails(billingDetails);
		billingDetails.setOrder(order);
		order.setShippingSameAsBilling(true);
		order.setShippingDetails(null);
		order.setItems(items);
		order.setTotal(total);
		order.setUser(user);
		item.setOrder(order);
		
		OrderDTO orderResponse = new OrderDTO(order);
		
		OrderStatsResponse response = new OrderStatsResponse(2, 3, new BigDecimal(100), new BigDecimal(30));

		// When
		setAdminAuthorization();

		when(orderService.getOrderStats(Mockito.argThat(new ArgumentMatcher<StatsRequest>() {

			@Override
			public boolean matches(StatsRequest argument) {
				return argument.getRange().equals("all");
			}
		}))).thenReturn(response);

		// Then
		mockMvc.perform(get("/api/v1/admin/orders/stats")
				.param("range", "all")
				.header("Authorization", "Bearer " + adminAuthToken)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").exists()) 
				.andExpect(jsonPath("$.successfulOrders").value(2)) // TODO: create a separate Serialization unit test
				.andExpect(jsonPath("$.unsuccessfulOrders").value(3))
				.andExpect(jsonPath("$.totalIncome").value(new BigDecimal(100)))
				.andExpect(jsonPath("$.unsuccessfulPayments").value(new BigDecimal(30)))
				;
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
