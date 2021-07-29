package com.bernardguiang.SnackOverflow.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.RefreshToken;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

@DataJpaTest(properties = { "spring.jpa.properties.javax.persistence.validation.mode=none" })
class UserRepositoryTest {
	
	@Autowired
	private UserRepository underTest;

	private User user1, user2;
	
	@BeforeEach
	void setUp() throws Exception {
		String email1 = "email1@email.com";
		String username1 = "username1";
		String password1 = "Password1!";
		String fullName1 = "Full Name 1";
		String role1 = ApplicationUserRole.ADMIN.name();
		List<Order> orders1 = null;
		Address address1 = null;
		RefreshToken refreshToken1 = null;
	
		user1 = underTest.save(new User(
				email1, 
				username1, 
				password1, 
				fullName1, 
				role1,
				orders1, 
				address1, 
				refreshToken1
		));
		
		String email2 = "email2@email.com";
		String username2 = "username2";
		String password2 = "Password2!";
		String fullName2 = "Full Name 2";
		String role2 = ApplicationUserRole.CUSTOMER.name();
		List<Order> orders2 = null;
		Address address2 = null;
		RefreshToken refreshToken2 = null;
	
		user2 = underTest.save(new User(
				email2, 
				username2, 
				password2, 
				fullName2, 
				role2,
				orders2, 
				address2, 
				refreshToken2
		));
	}

	@Test
	void itShouldFindByUsername() {
		// Given
		// When
		Optional<User> resultOptional = underTest.findByUsername(user1.getUsername());
		
		// Then
		User result = resultOptional.get();
		assertEquals(user1, result);
	}
	
	@Test
	void itShouldFindByEmail() {
		// Given
		// When
		Optional<User> resultOptional = underTest.findByEmail(user2.getEmail());
				
		// Then
		User result = resultOptional.get();
		assertEquals(user2, result);
	}
	
	
	

}
