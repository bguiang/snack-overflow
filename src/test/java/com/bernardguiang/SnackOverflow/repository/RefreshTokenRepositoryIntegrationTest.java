package com.bernardguiang.SnackOverflow.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bernardguiang.SnackOverflow.model.RefreshToken;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

@DataJpaTest(properties = { "spring.jpa.properties.javax.persistence.validation.mode=none" })
class RefreshTokenRepositoryIntegrationTest {

	@Autowired
	private RefreshTokenRepository underTest;
	
	@Autowired
	private UserRepository userRepository;
	
	private User user1, user2;
	private RefreshToken refreshToken1, refreshToken2, refreshToken3;
	
	@BeforeEach
	void setUp() throws Exception {
		
		User u1 = new User();
		u1.setEmail("u@email.com");
		u1.setFullName("user one");
		u1.setUsername("user1");
		u1.setPassword("asdf1");
		u1.setRole(ApplicationUserRole.CUSTOMER.name());
		user1 = userRepository.save(u1);
		User u2 = new User();
		u2.setEmail("u2@email.com");
		u2.setFullName("user two");
		u2.setUsername("user2");
		u2.setPassword("asdf2");
		u2.setRole(ApplicationUserRole.CUSTOMER.name());
		user2 = userRepository.save(u2);
		
		String token1 = "token 1";
		String token2 = "token 2";
		String token3 = "token 3";
		
		RefreshToken r1 = new RefreshToken();
		r1.setToken(token1);
		r1.setCreatedDate(Instant.now());
		r1.setUser(user1);
		
		RefreshToken r2 = new RefreshToken();
		r2.setToken(token2);
		r2.setCreatedDate(Instant.now());
		r2.setUser(user2);
		
		RefreshToken r3 = new RefreshToken();
		r3.setToken(token3);
		r3.setCreatedDate(Instant.now());
		r3.setUser(user2);
		
		refreshToken1 = underTest.save(r1);
		refreshToken2 = underTest.save(r2);
		refreshToken3 = underTest.save(r3);
	}

	@Test
	void itShouldFindByToken() {
		//Given
		//When
		Optional<RefreshToken> resultOptional = underTest.findByToken(refreshToken2.getToken());
		// Then
		RefreshToken result = resultOptional.get();
		assertEquals(refreshToken2, result);
	}

}
