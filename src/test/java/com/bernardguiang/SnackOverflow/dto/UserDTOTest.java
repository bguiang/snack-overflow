package com.bernardguiang.SnackOverflow.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
		
		User user = new User(id, email, username, password, fullName, role,
				orders, address, refreshToken);
		
		// When
		UserDTO dto = new UserDTO(user);
		
		// Then
		assertEquals(id, dto.getId());
		assertEquals(username, dto.getUsername());
		assertEquals(fullName, dto.getFullName());
		assertEquals(email, dto.getEmail());
		assertEquals(role, dto.getRole());
	}

}
