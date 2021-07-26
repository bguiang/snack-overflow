package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.UserRepository;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

class ApplicationUserDetailsServiceTest {

	private ApplicationUserDetailsService underTest;
	
	@Mock
	private UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		userRepository = Mockito.mock(UserRepository.class);
		underTest = new ApplicationUserDetailsService(userRepository);
	}
	
	@Test
	void itShouldLoadUserByUsername() {
		// Given
		String username = "testUsername123";
		String password = "testPassword123!";
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setRole(ApplicationUserRole.ADMIN.name());
		
		Optional<User> userOptional = Optional.of(user);
		
		// When
		when(userRepository.findByUsername(username)).thenReturn(userOptional);
		
		// Then
		UserDetails response = underTest.loadUserByUsername(username);
		assertEquals(username, response.getUsername());
		assertEquals(password, response.getPassword());
		Set<GrantedAuthority> expectedAuthorities = ApplicationUserRole.ADMIN.getGrantedAuthorities();
		Set<GrantedAuthority> authorities =  new HashSet<>();
		authorities.addAll(response.getAuthorities());
		assertTrue(authorities.containsAll(expectedAuthorities));
		assertTrue(expectedAuthorities.containsAll(authorities));
		assertTrue(!authorities.isEmpty());
	}
	
	@Test
	void itThrowAnErrorIfUserWithUsernameDoesNotExist() {
		// Given
		String username = "testUsername123";
		User user = null;
		Optional<User> userOptional = Optional.ofNullable(user);
		
		// When
		when(userRepository.findByUsername(username)).thenReturn(userOptional);
		
		// Then
		assertThrows(
			UsernameNotFoundException.class, 
			() -> underTest.loadUserByUsername(username),
			"User " + username + " not found.");
	}

}
