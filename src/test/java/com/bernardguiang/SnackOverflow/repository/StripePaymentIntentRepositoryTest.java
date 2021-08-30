package com.bernardguiang.SnackOverflow.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bernardguiang.SnackOverflow.model.Cart;
import com.bernardguiang.SnackOverflow.model.StripePaymentIntent;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

@DataJpaTest(properties = { "spring.jpa.properties.javax.persistence.validation.mode=none" })
class StripePaymentIntentRepositoryTest {
	
	@Autowired
	private StripePaymentIntentRepository underTest;
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		
		Cart cart1 = new Cart();
		User u1 = new User();
		u1.setEmail("u1@email.com");
		u1.setFullName("user one");
		u1.setUsername("user1");
		u1.setPassword("asdf");
		u1.setRole(ApplicationUserRole.CUSTOMER.name());
		User user1 = userRepository.save(u1);
		
		StripePaymentIntent intent1 = new StripePaymentIntent();
		intent1.setUser(user1);
		intent1.setCart(cart1);
		intent1.setPaymentIntentId("paymentIntent1");
		
		underTest.save(intent1);
		
	}
	
	@Test
	void itShouldFindByPaymentIntentId() {
		// Given
		// When
		Optional<StripePaymentIntent> result = underTest.findByPaymentIntentId("paymentIntent1");
		assertNotNull(result.get());
		assertEquals("user1", result.get().getUser().getUsername());
	}

}
