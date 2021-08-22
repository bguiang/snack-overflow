package com.bernardguiang.SnackOverflow.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.RefreshToken;
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
		
		List<Order> orders = new ArrayList<>();
		Order order = new Order();
		order.setId(1L);
		order.setUser(user);
		List<OrderItem> orderItems = new ArrayList<>();
		order.setItems(orderItems);
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
	}

}
