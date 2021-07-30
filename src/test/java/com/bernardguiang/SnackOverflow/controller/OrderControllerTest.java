package com.bernardguiang.SnackOverflow.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import com.bernardguiang.SnackOverflow.service.CartService;
import com.bernardguiang.SnackOverflow.service.OrderService;
import com.bernardguiang.SnackOverflow.service.StripeService;
import com.bernardguiang.SnackOverflow.service.UserService;

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
	void itShouldStartCheckout() {
		// Given
		
		// When
//		 userService.findByUsername(username);
//		 cartService.getCartInfo(cartItems);
//		 env.getProperty("stripe_access_key");
//		 orderService.createOrderWithCartItemsAndClientSecret(cartItems, clientSecret, user.getId());
		fail("todo");
		
		// Then
	}

}
