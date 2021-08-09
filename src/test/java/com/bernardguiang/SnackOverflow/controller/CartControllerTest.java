package com.bernardguiang.SnackOverflow.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bernardguiang.SnackOverflow.dto.request.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.request.CartRequest;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponseItem;
import com.bernardguiang.SnackOverflow.service.CartService;

class CartControllerTest {

	private CartController underTest;
	
	private CartService cartService;

	@BeforeEach
	void setUp() throws Exception {
		cartService = Mockito.mock(CartService.class);
		underTest = new CartController(cartService);
	}

	// Pretty useless test
	@Test
	void itShouldReturnCartInfoResponse() {
		// Given
		// ... Java bean validation does not work in unit tests so this will always be valid here
		List<CartInfoRequestItem> cartItems = null;
		
		List<CartInfoResponseItem> items = null;
		CartInfoResponse cart = new CartInfoResponse(new BigDecimal(2), items);
		
		CartRequest request = new CartRequest();
		request.setItems(cartItems);
		
		// When
		when(cartService.getCartInfo(Mockito.any())).thenReturn(cart);
		CartInfoResponse response =  underTest.getCartInfo(request);
		
		// Then
		assertEquals(cart, response);
	}

}
