package com.bernardguiang.SnackOverflow.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.request.CartRequest;
import com.bernardguiang.SnackOverflow.dto.request.UpdateBillingAndShippingRequest;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponse;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.service.CartService;
import com.bernardguiang.SnackOverflow.service.OrderService;
import com.bernardguiang.SnackOverflow.service.StripeService;
import com.bernardguiang.SnackOverflow.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

class OrderControllerTest {

	private OrderController underTest;
	
	private OrderService orderService;
	private UserService userService;
	private StripeService stripeService;
	private CartService cartService;
	
	@BeforeEach
	void setUp() throws Exception {
		orderService = Mockito.mock(OrderService.class);
		userService = Mockito.mock(UserService.class);
		stripeService = Mockito.mock(StripeService.class);
		cartService = Mockito.mock(CartService.class);
		
		underTest = new OrderController(orderService, userService, stripeService, cartService);
	}

	@Test
	void itShouldStartCheckout() throws StripeException {
		// Given
		String authUsername = "authUsername";
		Authentication authentication = Mockito.mock(Authentication.class);
		
		UserDTO user = new UserDTO();
		String userEmail = "user@user.com";
		user.setEmail(userEmail);
		Long userId = 3L;
		user.setId(userId);
		
		List<CartInfoRequestItem> cartItems = new ArrayList<>();
		Long productId = 2L;
		int quantity = 5;
		CartInfoRequestItem cartItem = new CartInfoRequestItem();
		cartItem.setProductId(productId);
		cartItem.setQuantity(quantity);
		cartItems.add(cartItem);
		CartRequest request = new CartRequest();
		request.setItems(cartItems);
		
		CartInfoResponse cartInfoResponse = new CartInfoResponse();
		BigDecimal cartTotal = new BigDecimal(10);
		cartInfoResponse.setTotal(cartTotal);
		
		PaymentIntent intent = Mockito.mock(PaymentIntent.class);
		String clientSecret = "clientSecret";
		
		Long savedOrderId = 10L;
		Long amount = 1000L;
		
		// When
		when(authentication.getName()).thenReturn(authUsername);
		when(userService.findByUsername(authUsername)).thenReturn(user);
		when(cartService.getCartInfo(request)).thenReturn(cartInfoResponse);
		when(intent.getClientSecret()).thenReturn(clientSecret);
		when(stripeService.createPaymentIntent(amount, userEmail)).thenReturn(intent);
		when(orderService.createOrderWithCartItemsAndPaymentIntentId(request, clientSecret, user.getId())).thenReturn(savedOrderId);
		
		ResponseEntity<Map<String, Object>> response = underTest.startOrder(request, authentication);
		
		// Then
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		Map<String, Object> responseBody = response.getBody();
		assertEquals(clientSecret, (String) responseBody.get("client_secret"));
		CartInfoResponse responseCart = (CartInfoResponse) responseBody.get("cart");
		assertEquals(cartInfoResponse.getTotal(), responseCart.getTotal());
		assertEquals(savedOrderId, (Long) responseBody.get("orderId"));
	}
	
	@Test
	void updateOrderBillingAndShippingShouldReturnStatusOk( ) throws StripeException {
		// Given
		UpdateBillingAndShippingRequest updateBillingAndShippingRequest = null;
		Authentication authentication = Mockito.mock(Authentication.class);
		String authUsername = "authUsername";
		
		UserDTO user = new UserDTO();
		String userEmail = "user@user.com";
		user.setEmail(userEmail);
		Long userId = 3L;
		user.setId(userId);
		
		// When
		when(userService.findByUsername(authUsername)).thenReturn(user);

		when(orderService.updateBillingAndShipping(updateBillingAndShippingRequest, user)).thenReturn(null);
		
		ResponseEntity<String> response = underTest.updateOrderBillingAndShipping(
				updateBillingAndShippingRequest, authentication);
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void itShouldGetOrdersByCurrentUser() {
		// Pointless to unit test
	}
	
	@Test
	void itShouldGetOrderByCurrentUser() {
		// Pointless to unit test
	}

}
