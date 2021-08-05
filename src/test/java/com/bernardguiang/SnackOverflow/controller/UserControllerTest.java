package com.bernardguiang.SnackOverflow.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bernardguiang.SnackOverflow.service.UserService;

class UserControllerTest {

	private UserController underTest;
	
	private UserService userService;

	@BeforeEach
	void setUp() throws Exception {
		
		userService = Mockito.mock(UserService.class);
		underTest = new UserController(userService);
	}
	
//	@Test 
//	void itshouldReturnUsers() {
//		
//	}
}
