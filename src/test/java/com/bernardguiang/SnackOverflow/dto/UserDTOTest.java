package com.bernardguiang.SnackOverflow.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.RefreshToken;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

class UserDTOTest {

	@Test
	void itShouldConstructUserDTOFromUser() {
		// Given
		Long id = 1L;
		String email = "user@user.com";
		String username = "user123";
		String password = "Password123!";
		String fullName = "User FullName";
		String role = ApplicationUserRole.CUSTOMER.name();
		List<Order> orders = null;
		Address address = null;
		RefreshToken refreshToken = null;
		Instant joinDate = Instant.now();
		
		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(password);
		user.setFullName(fullName);
		user.setRole(role);
		user.setOrders(orders);
		user.setAddress(address);
		user.setRefreshToken(refreshToken);
		user.setJoinDate(joinDate);
		
		// When
		UserDTO dto = new UserDTO(user);
		
		// Then
		assertEquals(id, dto.getId());
		assertEquals(username, dto.getUsername());
		assertEquals(fullName, dto.getFullName());
		assertEquals(email, dto.getEmail());
		assertEquals(role, dto.getRole());
		assertEquals(joinDate, dto.getJoinDate());
	}

}
