package com.bernardguiang.SnackOverflow.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.dto.BillingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.request.CartRequest;
import com.bernardguiang.SnackOverflow.dto.request.UpdateBillingAndShippingRequest;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponseItem;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponse;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponseItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;
import com.bernardguiang.SnackOverflow.service.ApplicationUserDetailsService;
import com.bernardguiang.SnackOverflow.service.AuthService;
import com.bernardguiang.SnackOverflow.service.CartService;
import com.bernardguiang.SnackOverflow.service.JwtService;
import com.bernardguiang.SnackOverflow.service.OrderService;
import com.bernardguiang.SnackOverflow.service.StripeService;
import com.bernardguiang.SnackOverflow.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.PaymentIntent;

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

	private final Long customerId = 5L;
	private final String customerUsername = "customerUsername";
	private final String customerEmail = "customer@email.com";
	private final String customerRole = ApplicationUserRole.CUSTOMER.name();
	private final String customerAuthToken = "customerAuthToken";

	@BeforeAll
	static void setupBeforeAll() {
		customerRoleAuthorities = new ArrayList<>();
		
		Map<String, String> auth1 = new HashMap<>();
		auth1.put("authority", "ROLE_CUSTOMER");
		customerRoleAuthorities.add(auth1);
		Map<String, String> auth2 = new HashMap<>();
		auth2.put("authority", "order:write");
		customerRoleAuthorities.add(auth2);
		Map<String, String> auth3 = new HashMap<>();
		auth3.put("authority", "category:read");
		customerRoleAuthorities.add(auth3);
		Map<String, String> auth4 = new HashMap<>();
		auth4.put("authority", "product:read");
		customerRoleAuthorities.add(auth4);
		Map<String, String> auth5 = new HashMap<>();
		auth5.put("authority", "order:read");
		customerRoleAuthorities.add(auth5);

//		adminRoleAuthorities = new ArrayList<>();
//		Map<String, String> auth1 = new HashMap<>();
//		auth1.put("authority", "order:write");
//		adminRoleAuthorities.add(auth1);
//		Map<String, String> auth2 = new HashMap<>();
//		auth2.put("authority", "product:write");
//		adminRoleAuthorities.add(auth2);
//		Map<String, String> auth3 = new HashMap<>();
//		auth3.put("authority", "order:read");
//		adminRoleAuthorities.add(auth3);
//		Map<String, String> auth4 = new HashMap<>();
//		auth4.put("authority", "category:read");
//		adminRoleAuthorities.add(auth4);
//		Map<String, String> auth5 = new HashMap<>();
//		auth5.put("authority", "ROLE_ADMIN");
//		adminRoleAuthorities.add(auth5);
//		Map<String, String> auth6 = new HashMap<>();
//		auth6.put("authority", "category:write");
//		adminRoleAuthorities.add(auth6);
//		Map<String, String> auth7 = new HashMap<>();
//		auth7.put("authority", "product:read");
//		adminRoleAuthorities.add(auth7);
	}

	void setCustomerAuthorization() {
		// ... Handle token authorization via filter
		Claims tokenPayload = Mockito.mock(Claims.class);
		when(tokenPayload.getSubject()).thenReturn(customerUsername);
		when(tokenPayload.get("authorities")).thenReturn(customerRoleAuthorities);
		when(jwtService.getTokenPayload(customerAuthToken)).thenReturn(tokenPayload);
	}

	@Test
	void itShouldStartOrder() throws Exception {
		// Given
		Long productId = 1L;
		int quantity = 10;

		BigDecimal total = new BigDecimal(20);

		List<CartInfoRequestItem> requestItems = new ArrayList<>();
		CartInfoRequestItem cartItem = new CartInfoRequestItem();
		cartItem.setProductId(productId);
		cartItem.setQuantity(quantity);
		requestItems.add(cartItem);

		CartRequest request = new CartRequest();
		request.setItems(requestItems);

		CartInfoResponse cartInfoResponse = new CartInfoResponse();
		List<CartInfoResponseItem> responseItems = new ArrayList<>();
		CartInfoResponseItem responseItem = new CartInfoResponseItem();
		responseItem.setProduct(new ProductDTO());
		responseItem.setQuantity(10);
		responseItems.add(responseItem);
		cartInfoResponse.setItems(responseItems);
		cartInfoResponse.setTotal(total);

		UserDTO user = new UserDTO();
		user.setEmail(customerEmail);
		user.setFullName("Full Name");
		user.setId(customerId);
		user.setRole(customerRole);
		user.setUsername(customerUsername);

		Long amount = 2000L;
		PaymentIntent intent = new PaymentIntent();
		String clientSecret = "client_secret";
		intent.setClientSecret(clientSecret);

		Long savedOrderId = 3L;

		// When
		// ... Handle token authorization via filter
		setCustomerAuthorization();

		// ... Controller's mocked dependency methods
		when(userService.findByUsername(customerUsername)).thenReturn(user);

		when(cartService.getCartInfo(Mockito.argThat(new ArgumentMatcher<CartRequest>() {

			@Override
			public boolean matches(CartRequest argument) {
				return argument.getItems().get(0).getProductId() == request.getItems().get(0).getProductId()
						&& argument.getItems().get(0).getQuantity() == request.getItems().get(0).getQuantity();
			}
		}))).thenReturn(cartInfoResponse);

		when(stripeService.createPaymentIntent(amount, customerEmail)).thenReturn(intent);

		when(orderService.createOrderWithCartItemsAndClientSecret(Mockito.argThat(new ArgumentMatcher<CartRequest>() {

			@Override
			public boolean matches(CartRequest argument) {
				return argument.getItems().get(0).getProductId() == request.getItems().get(0).getProductId()
						&& argument.getItems().get(0).getQuantity() == request.getItems().get(0).getQuantity();
			}
		}), Mockito.eq(clientSecret), Mockito.eq(customerId))).thenReturn(savedOrderId);

		// Then
		mockMvc.perform(post("/api/v1/orders/start").header("Authorization", "Bearer " + customerAuthToken)
				.content(asJsonString(request)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.client_secret").value(clientSecret))
				.andExpect(jsonPath("$.cart.total").value(total))
				.andExpect(jsonPath("$.cart.items").isNotEmpty())
				.andExpect(jsonPath("$.orderId").value(savedOrderId));

		verify(cartService).getCartInfo(Mockito.any());
	}

	@Test
	void startOrderShouldFailIfRequestIsInvalid() throws Exception {
		// Given
		List<CartInfoRequestItem> requestItems = new ArrayList<>();

		CartRequest request = new CartRequest();
		request.setItems(requestItems);// empty cart is invalid

		// When
		// ... Handle token authorization via filter
		setCustomerAuthorization();
		// Then
		mockMvc.perform(post("/api/v1/orders/start").header("Authorization", "Bearer " + customerAuthToken)
				.content(asJsonString(request)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}

	@Test
	void itShouldUpdateOrderBillingAndShipping() throws Exception {
		// Given
		UpdateBillingAndShippingRequest updateBillingAndShippingRequest = new UpdateBillingAndShippingRequest();
		updateBillingAndShippingRequest.setId(1L);
		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
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
		billingDetails.setOrderId(1L);
		billingDetails.setPhone("1234567890");
		updateBillingAndShippingRequest.setBillingDetails(billingDetails);
		updateBillingAndShippingRequest.setShippingDetails(null);
		updateBillingAndShippingRequest.setShippingSameAsBilling(true);

		UserDTO user = new UserDTO();
		user.setEmail(customerEmail);
		user.setFullName("Full Name");
		user.setId(customerId);
		user.setRole(customerRole);
		user.setUsername(customerUsername);

		// When
		// ... Handle token authorization via filter
		setCustomerAuthorization();

		// ... Controller's mocked dependency methods
		when(userService.findByUsername(customerUsername)).thenReturn(user);

		when(orderService
				.updateBillingAndShipping(Mockito.argThat(new ArgumentMatcher<UpdateBillingAndShippingRequest>() {

					@Override
					public boolean matches(UpdateBillingAndShippingRequest argument) {
						return argument.getId() == updateBillingAndShippingRequest.getId();
					}
				}), Mockito.argThat(new ArgumentMatcher<UserDTO>() {

					@Override
					public boolean matches(UserDTO argument) {
						return argument.getId() == user.getId();
					}
				}))).thenReturn(Mockito.any());

		// Then
		mockMvc.perform(
				put("/api/v1/orders/updateBillingAndShipping").header("Authorization", "Bearer " + customerAuthToken)
						.content(asJsonString(updateBillingAndShippingRequest)).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value("Order Updated"));
	}

	@Test
	void updateOrderBillingAndShippingShouldFailIfRequestInvalid() throws Exception {
		// Given
		// ... invalid empty request
		UpdateBillingAndShippingRequest updateBillingAndShippingRequest = new UpdateBillingAndShippingRequest();

		// When
		// ... Handle token authorization via filter
		setCustomerAuthorization();

		// Then
		mockMvc.perform(
				put("/api/v1/orders/updateBillingAndShipping").header("Authorization", "Bearer " + customerAuthToken)
						.content(asJsonString(updateBillingAndShippingRequest)).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void itShouldGetOrdersByCurrentUser() throws Exception {
		// Given
		UserDTO user = new UserDTO();
		user.setEmail(customerEmail);
		user.setFullName("Full Name");
		user.setId(customerId);
		user.setRole(customerRole);
		user.setUsername(customerUsername);

		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
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
		billingDetails.setOrderId(1L);
		billingDetails.setPhone("1234567890");

		Long orderId = 1L;
		Instant createdDate = Instant.now();
		OrderStatus status = OrderStatus.PROCESSING;
		BigDecimal total = new BigDecimal(20);
		List<OrderResponseItem> items = new ArrayList<>();
		OrderResponseItem item = new OrderResponseItem();
		items.add(item);

		List<OrderResponse> orders = new ArrayList<>();
		OrderResponse order1 = new OrderResponse();
		order1.setId(orderId);
		order1.setCreatedDate(createdDate);
		order1.setStatus(status);
		order1.setBillingDetails(billingDetails);
		order1.setIsShippingSameAsBilling(true);
		order1.setShippingDetails(null);
		order1.setItems(items);
		order1.setTotal(total);
		order1.setUserId(customerId);
		orders.add(order1);

		// When
		setCustomerAuthorization();

		when(userService.findByUsername(customerUsername)).thenReturn(user);

		when(orderService.findAllByUserAndStatusNot(Mockito.any(), Mockito.any())).thenReturn(orders);

		// Then
		mockMvc.perform(get("/api/v1/orders").header("Authorization", "Bearer " + customerAuthToken)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$[0].id").value(orderId));
	}

	@Test
	void itShouldGetOrderByCurrentUser() throws Exception {
		// Given
		UserDTO user = new UserDTO();
		user.setEmail(customerEmail);
		user.setFullName("Full Name");
		user.setId(customerId);
		user.setRole(customerRole);
		user.setUsername(customerUsername);

		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
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
		billingDetails.setOrderId(1L);
		billingDetails.setPhone("1234567890");

		Long orderId = 1L;
		Instant createdDate = Instant.now();
		OrderStatus status = OrderStatus.PROCESSING;
		BigDecimal total = new BigDecimal(20);
		List<OrderResponseItem> items = new ArrayList<>();
		OrderResponseItem item = new OrderResponseItem();
		Long orderItemId = 20L;
		item.setId(orderItemId);
		items.add(item);

		OrderResponse order = new OrderResponse();
		order.setId(orderId);
		order.setCreatedDate(createdDate);
		order.setStatus(status);
		order.setBillingDetails(billingDetails);
		order.setIsShippingSameAsBilling(true);
		order.setShippingDetails(null);
		order.setItems(items);
		order.setTotal(total);
		order.setUserId(customerId);

		// When
		setCustomerAuthorization();

		when(userService.findByUsername(customerUsername)).thenReturn(user);

		when(orderService.findByIdAndUserIdAndStatusNot(orderId, customerId, OrderStatus.CREATED)).thenReturn(order);

		// Then
		mockMvc.perform(get("/api/v1/orders/1").header("Authorization", "Bearer " + customerAuthToken)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(orderId))
				.andExpect(jsonPath("$.items").isArray())
				.andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$.items[0].id").value(orderItemId));
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
