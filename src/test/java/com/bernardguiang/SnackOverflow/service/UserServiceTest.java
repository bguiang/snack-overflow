package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.UserRepository;

class UserServiceTest {

	private UserService underTest;
	
	private UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		userRepository = Mockito.mock(UserRepository.class);
		underTest = new UserService(userRepository);
	}
	
	@Test
	void itShouldFindFindByUsername() {
		// Given
		String username = "myUsername";
		User user = new User();
		user.setUsername(username);
		
		Optional<User> userOptional = Optional.of(user);
		
		// When
		when(userRepository.findByUsername(username)).thenReturn(userOptional);
		UserDTO userDTO = underTest.findByUsername(username);
		
		// Then
		assertEquals(username, userDTO.getUsername());	
	}
	
	@Test
	void itShouldThrowAndExceptionWhenUsernameDoesNotExist() {
		String username = "myUsername";
		User user = null;
		Optional<User> userOptional = Optional.ofNullable(user);
		
		// When
		when(userRepository.findByUsername(username)).thenReturn(userOptional);
		
		// Then
		assertThrows(IllegalStateException.class, ()->underTest.findByUsername(username),
				"Could not find user: " + username);
	}

}
