package com.bernardguiang.SnackOverflow.dto.response;

import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.model.Order;

class OrderResponseTest {

	@Test
	void itShouldConstructOrderResponseFromOrder() {
		// Given
		Order order = new Order();
		
		// When
		OrderResponse orderResponse = new OrderResponse(order);
		
		// Then
	}

}
