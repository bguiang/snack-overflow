package com.bernardguiang.SnackOverflow.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.RefreshToken;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

@DataJpaTest(properties = { "spring.jpa.properties.javax.persistence.validation.mode=none" })
class UserRepositoryIntegrationTest {
	
	@Autowired
	private UserRepository underTest;

	private User user1, user2, user3;
	private Instant joinDate1, joinDate2, joinDate3;
	
	@BeforeEach
	void setUp() throws Exception {
	
		User u1 = new User();
		u1.setEmail("email1@email.com");
		u1.setUsername("username1");
		u1.setPassword("Password1!");
		u1.setFullName("Full Name 1");
		u1.setRole(ApplicationUserRole.ADMIN.name());
		joinDate1 = Instant.now();
		u1.setJoinDate(joinDate1);
//		u1.setOrders(orders1);
//		u1.setAddress(address1);
//		u1.setRefreshToken(refreshToken1);
		
		user1 = underTest.save(u1);
	
		User u2 = new User();
		u2.setEmail("email2@email.com");
		u2.setUsername("username2");
		u2.setPassword("Password2!");
		u2.setFullName("Full Name 2");
		u2.setRole(ApplicationUserRole.CUSTOMER.name());
		joinDate2 = Instant.now();
		u2.setJoinDate(joinDate2);
//		u2.setOrders(orders2);
//		u2.setAddress(address2);
//		u2.setRefreshToken(refreshToken2);
		user2 = underTest.save(u2);
		
		User u3 = new User();
		u3.setEmail("email3@email.com");
		u3.setUsername("username3");
		u3.setPassword("Password3!");
		u3.setFullName("Full Name 3");
		u3.setRole(ApplicationUserRole.CUSTOMER.name());
		joinDate3 = Instant.now();
		u3.setJoinDate(joinDate3);
//		u3.setOrders(orders2);
//		u3.setAddress(address2);
//		u3.setRefreshToken(refreshToken2);
		user3 = underTest.save(u3);
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
	
	@Test
	void itShouldFindAllByUsernameContainingIgnoreCase() {
		// Given
		Sort sort = Sort.by(Sort.Direction.DESC, "joinDate");
		Pageable pageable = PageRequest.of(0, 10, sort);
		
		// When
		Page<User> userPage = underTest.findAllByUsernameContainingIgnoreCase("2", pageable);
		
		// Then
		assertEquals(1, userPage.getContent().size());
		assertEquals(user2, userPage.getContent().get(0));
	}
	
	@Test
	void itShouldFindAllByJoinDateAfter() {
		// Given
		
		// When
		List<User> userPage = underTest.findAllByJoinDateAfter(joinDate1);
		
		// Then
		assertEquals(2, userPage.size());
	}
	

}
