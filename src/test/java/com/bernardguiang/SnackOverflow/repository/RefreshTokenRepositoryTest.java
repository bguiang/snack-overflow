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

@DataJpaTest(properties = { "spring.jpa.properties.javax.persistence.validation.mode=none" })
class RefreshTokenRepositoryTest {

	@Autowired
	private RefreshTokenRepository underTest;
	
	@Autowired
	private UserRepository userRepository;
	
	private User user1, user2;
	private RefreshToken refreshToken1, refreshToken2, refreshToken3;
	
	@BeforeEach
	void setUp() throws Exception {
		
		user1 = userRepository.save(new User());
		user2 = userRepository.save(new User());
		
		String token1 = "token 1";
		String token2 = "token 2";
		String token3 = "token 3";
		
		refreshToken1 = underTest.save(new RefreshToken(token1, Instant.now(), user1));
		refreshToken2 = underTest.save(new RefreshToken(token2, Instant.now(), user2));
		refreshToken3 = underTest.save(new RefreshToken(token3, Instant.now(), user2));
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
