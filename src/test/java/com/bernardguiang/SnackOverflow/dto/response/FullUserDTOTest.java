package com.bernardguiang.SnackOverflow.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.RefreshToken;
import com.bernardguiang.SnackOverflow.model.ShippingDetails;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

class FullUserDTOTest {

	@Test
	void itShouldConstructFullUserDTOFromUser() {
		// Given
		Long id = 1L;
		String email = "user@user.com";
		String username = "user123";
		String password = "Password123!";
		String fullName = "User FullName";
		String role = ApplicationUserRole.CUSTOMER.name();
		RefreshToken refreshToken = null;
		Instant joinDate = Instant.now();

		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);

		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(password);
		user.setFullName(fullName);
		user.setRole(role);
		user.setAddress(address);
		user.setRefreshToken(refreshToken);
		user.setJoinDate(joinDate);
		user.setAddress(address);

		Order order = new Order();
		order.setId(1L);
		order.setTotal(new BigDecimal(20));
		order.setCreatedDate(Instant.now());
		order.setShippingSameAsBilling(false);
		order.setStatus(OrderStatus.COMPLETED);

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
		order.setUser(user);

		BillingDetails billingDetails = new BillingDetails();
		billingDetails.setOrder(order);
		order.setBillingDetails(billingDetails);

		ShippingDetails shippingDetails = new ShippingDetails();
		shippingDetails.setOrder(order);
		order.setShippingDetails(shippingDetails);

		List<Order> orders = new ArrayList<>();
		orders.add(order);
		user.setOrders(orders);
		// When
		FullUserDTO underTest = new FullUserDTO(user);

		// Then
		assertEquals(id, underTest.getId());
		assertEquals(username, underTest.getUsername());
		assertEquals(fullName, underTest.getFullName());
		assertEquals(email, underTest.getEmail());
		assertEquals(role, underTest.getRole());
		assertEquals(joinDate, underTest.getJoinDate());
		assertEquals(1, underTest.getOrders().size());
		assertEquals(address, underTest.getAddress());
		assertEquals(1, underTest.getOrders().size());
	}

}
