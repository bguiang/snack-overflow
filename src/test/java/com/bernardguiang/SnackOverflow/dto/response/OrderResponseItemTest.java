package com.bernardguiang.SnackOverflow.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.Product;

class OrderResponseItemTest {

	@Test
	void itShouldConstructOrderResponseItemFromOrderItem() {
		// Given
		Order order = null;
		Product product = new Product();
		
		OrderItem orderItem = new OrderItem();
		orderItem.setId(1L);
		orderItem.setOrder(order);
		orderItem.setPrice(new BigDecimal(2));
		orderItem.setProduct(product);
		orderItem.setQuantity(10);
		
		// When
		OrderResponseItem underTest = new OrderResponseItem(orderItem);
		
		// Then		
		assertEquals(1L, underTest.getId());
		assertEquals(new BigDecimal(2), underTest.getPrice());
		assertEquals(10, underTest.getQuantity());
		assertNotNull(underTest.getProduct());
	}

}
