package com.bernardguiang.SnackOverflow.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.StatsRequest;
import com.bernardguiang.SnackOverflow.dto.request.UserPage;
import com.bernardguiang.SnackOverflow.dto.response.FullUserDTO;
import com.bernardguiang.SnackOverflow.dto.response.UserStatsResponse;
import com.bernardguiang.SnackOverflow.service.UserService;

class UserControllerTest {

	private UserController underTest;
	
	private UserService userService;

	@BeforeEach
	void setUp() throws Exception {
		
		userService = Mockito.mock(UserService.class);
		underTest = new UserController(userService);
	}
	
	@Test
	void getUsersPaginated() {
		// Given
		UserPage userPageMock = Mockito.mock(UserPage.class);
		Page<UserDTO> pageMock = Mockito.mock(Page.class);
		
		// When
		when(userService.findUsersPaginated(userPageMock)).thenReturn(pageMock);
		Page<UserDTO> resultPage = underTest.getUsersPaginated(userPageMock);
		
		// Then
		assertEquals(pageMock, resultPage);
		
	}
	
	@Test
	void getUser() {
		// Given
		FullUserDTO fullUserDTOMock = Mockito.mock(FullUserDTO.class);
		
		// When
		when(userService.findById(1L)).thenReturn(fullUserDTOMock);
		FullUserDTO result = underTest.getUser(1L);
		
		// Then
		assertEquals(fullUserDTOMock, result);
	}
	
	@Test
	void getNewUsers() {
		// Given
		StatsRequest statsRequestMock = Mockito.mock(StatsRequest.class);
		UserStatsResponse userStatsResponseMock = Mockito.mock(UserStatsResponse.class);
		
		// When
		when(userService.getUserStats(statsRequestMock)).thenReturn(userStatsResponseMock);
		UserStatsResponse result = underTest.getNewUsers(statsRequestMock);
		
		// Then
		assertEquals(userStatsResponseMock, result);
	}
}
