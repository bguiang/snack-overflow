package com.bernardguiang.SnackOverflow.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.ShippingDetails;
import com.bernardguiang.SnackOverflow.model.User;

class OrderResponseTest {

	@Test
	void itShouldConstructOrderResponseFromOrder() {
		// Given
		Instant createDate = Instant.now();

		Order order = new Order();
		order.setId(7L);
		order.setTotal(new BigDecimal(10));
		order.setCreatedDate(createDate);
		order.setShippingSameAsBilling(false);
		order.setStatus(OrderStatus.COMPLETED);

		BillingDetails billingDetails = new BillingDetails();
		billingDetails.setOrder(order);
		order.setBillingDetails(billingDetails);

		ShippingDetails shippingDetails = new ShippingDetails();
		shippingDetails.setOrder(order);
		order.setShippingDetails(shippingDetails);

		List<OrderItem> items = new ArrayList<>();
		OrderItem item1 = new OrderItem();
		Product product1 = new Product();
		product1.setId(1L);
		item1.setProduct(product1);
		OrderItem item2 = new OrderItem();
		Product product2 = new Product();
		product2.setId(2L);
		item2.setProduct(product2);
		items.add(item1);
		items.add(item2);
		order.setItems(items);

		User user = new User();
		user.setId(2L);
		order.setUser(user);

		// When
		OrderResponse underTest = new OrderResponse(order);

		// Then
		assertEquals(7L, underTest.getId());
		assertEquals(2L, underTest.getUserId());
		assertEquals(new BigDecimal(10), underTest.getTotal());
		assertEquals(OrderStatus.COMPLETED, underTest.getStatus());
		assertEquals(false, underTest.getIsShippingSameAsBilling());
		assertEquals(createDate, underTest.getCreatedDate());

		assertNotNull(underTest.getBillingDetails());
		assertNotNull(underTest.getShippingDetails());
		assertEquals(2, underTest.getItems().size());
	}

	@Test
	void shippingShouldBeNullWhenShippingIsSameAsBilling() {
		// Given
		Instant createDate = Instant.now();

		Order order = new Order();
		order.setId(7L);
		order.setTotal(new BigDecimal(10));
		order.setCreatedDate(createDate);
		order.setShippingSameAsBilling(true);
		order.setStatus(OrderStatus.COMPLETED);

		BillingDetails billingDetails = new BillingDetails();
		billingDetails.setOrder(order);
		order.setBillingDetails(billingDetails);

		ShippingDetails shippingDetails = new ShippingDetails();
		shippingDetails.setOrder(order);
		order.setShippingDetails(shippingDetails);

		List<OrderItem> items = new ArrayList<>();
		OrderItem item1 = new OrderItem();
		Product product1 = new Product();
		product1.setId(1L);
		item1.setProduct(product1);
		OrderItem item2 = new OrderItem();
		Product product2 = new Product();
		product2.setId(2L);
		item2.setProduct(product2);
		items.add(item1);
		items.add(item2);
		order.setItems(items);

		User user = new User();
		user.setId(2L);
		order.setUser(user);

		// When
		OrderResponse underTest = new OrderResponse(order);

		// Then
		assertEquals(7L, underTest.getId());
		assertEquals(2L, underTest.getUserId());
		assertEquals(new BigDecimal(10), underTest.getTotal());
		assertEquals(OrderStatus.COMPLETED, underTest.getStatus());
		assertEquals(true, underTest.getIsShippingSameAsBilling());
		assertEquals(createDate, underTest.getCreatedDate());
		assertNotNull(underTest.getBillingDetails());
		assertNull(underTest.getShippingDetails());
		assertEquals(2, underTest.getItems().size());
	}

}
