package com.bernardguiang.SnackOverflow.dto.response;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.Product;

class OrderItemDTOTest {

	@Test
	void itShouldConstructOrderItemDTOFromOrderItem() {
		
		// Given
		Instant orderCreatedDate = Instant.now();
		Order order = new Order();
		order.setId(1L);
		order.setCreatedDate(orderCreatedDate);
		order.setStatus(OrderStatus.COMPLETED);
		
		Product product = new Product();
		product.setId(2L);
		
		OrderItem orderItem = new OrderItem();
		orderItem.setId(3L);
		orderItem.setOrder(order);
		orderItem.setPrice(new BigDecimal(2));
		orderItem.setProduct(product);
		orderItem.setQuantity(5);
		
		// When
		OrderItemDTO underTest = new OrderItemDTO(orderItem);
		
		// Then
		assertEquals((Long) 3L, underTest.getId());
		assertEquals(5, underTest.getQuantity());
		assertEquals(new BigDecimal(2), underTest.getPrice());
		assertEquals((Long) 1L, underTest.getOrderId());
		assertEquals(orderCreatedDate, underTest.getOrderCreatedDate());
		assertEquals(OrderStatus.COMPLETED, underTest.getOrderStatus());
		assertEquals((Long) 2L, underTest.getProductId());
		
	}
}
