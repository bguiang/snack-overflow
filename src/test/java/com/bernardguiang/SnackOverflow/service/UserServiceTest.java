package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.ProductPage;
import com.bernardguiang.SnackOverflow.dto.request.StatsRequest;
import com.bernardguiang.SnackOverflow.dto.request.UserPage;
import com.bernardguiang.SnackOverflow.dto.response.FullProductInfo;
import com.bernardguiang.SnackOverflow.dto.response.FullUserDTO;
import com.bernardguiang.SnackOverflow.dto.response.UserStatsResponse;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.UserRepository;

class UserServiceTest {

	private UserService underTest;

	private UserRepository userRepository;

	private List<User> usersList;
	private User user1, user2;

	@BeforeEach
	void setUp() throws Exception {
		userRepository = Mockito.mock(UserRepository.class);
		underTest = new UserService(userRepository);

		user1 = new User();
		user2 = new User();
		usersList = Arrays.asList(user1, user2);
	}

	@Test
	void getUserStatsAllTime() {
		// Given
		StatsRequest request = new StatsRequest();
		request.setRange("all");

		// When
		when(userRepository.findAll()).thenReturn(usersList);
		UserStatsResponse response = underTest.getUserStats(request);

		// Then
		assertEquals(2, response.getNewUsers());
	}

	@Test
	void getUserStatsThisMonth() {
		// Given
		StatsRequest request = new StatsRequest();
		request.setRange("month");

		LocalDate today = LocalDate.of(2000, 5, 15);
		ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);
		MockedStatic<LocalDate> localDateMockedStatic = Mockito.mockStatic(LocalDate.class);

		// When
		localDateMockedStatic.when(() -> LocalDate.now()).thenAnswer(new Answer<LocalDate>() {
			public LocalDate answer(InvocationOnMock invocation) throws Throwable {
				localDateMockedStatic.close();
				return today;
			}
		});

		// When
		when(userRepository.findAllByJoinDateAfter(instantCaptor.capture())).thenReturn(usersList);
		UserStatsResponse response = underTest.getUserStats(request);

		// Then
		Date convertedDate = Date.from(instantCaptor.getValue());
		assertEquals(1, convertedDate.getDate());
		assertEquals(4, convertedDate.getMonth());
		assertEquals(100, convertedDate.getYear()); // getYear returns calendar year - 1900
		assertEquals(2, response.getNewUsers());
	}

	@Test
	void getUserStatsThisYear() {
		// Given
		StatsRequest request = new StatsRequest();
		request.setRange("year");
		
		LocalDate today = LocalDate.of(2000, 5, 15);
		ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);
		MockedStatic<LocalDate> localDateMockedStatic = Mockito.mockStatic(LocalDate.class);

		// When
		localDateMockedStatic.when(() -> LocalDate.now()).thenAnswer(new Answer<LocalDate>() {
			public LocalDate answer(InvocationOnMock invocation) throws Throwable {
				localDateMockedStatic.close();
				return today;
			}
		});
		when(userRepository.findAllByJoinDateAfter(instantCaptor.capture())).thenReturn(usersList);
		UserStatsResponse response = underTest.getUserStats(request);

		// Then
		Date convertedDate = Date.from(instantCaptor.getValue());
		assertEquals(1, convertedDate.getDate());
		assertEquals(0, convertedDate.getMonth());
		assertEquals(100, convertedDate.getYear()); // getYear returns calendar year - 1900
		assertEquals(2, response.getNewUsers());
	}
	
	@Test
	void findUsersPaginated() {
		// Given
		UserPage page = new UserPage();
		page.setSearch("search string");
		List<Object> userDTOList = new ArrayList<>();
		userDTOList.add(new UserDTO(user1));
		userDTOList.add(new UserDTO(user2));
		Page<Object> dtoPage = new PageImpl<Object>(userDTOList, Pageable.unpaged(), userDTOList.size());
		
		// When
		Page<User> userPageMock = Mockito.mock(Page.class);
		when(userRepository.findAllByUsernameContainingIgnoreCase(Mockito.matches("search string"), Mockito.any())).thenReturn(userPageMock);
		when(userPageMock.map(Mockito.any())).thenReturn(dtoPage);
		
		Page<UserDTO> result = underTest.findUsersPaginated(page);
		
		// Then
		assertEquals(2, result.getContent().size());
	}
	
	@Test
	void findByUsername() {
		// Given
		User user = new User();
		user.setUsername("myUsername");

		Optional<User> userOptional = Optional.of(user);

		// When
		when(userRepository.findByUsername("myUsername")).thenReturn(userOptional);
		UserDTO userDTO = underTest.findByUsername("myUsername");

		// Then
		assertEquals("myUsername", userDTO.getUsername());
	}

	@Test
	void findByUsernameShouldThrowAndExceptionWhenUserDoesNotExist() {
		User user = null;
		Optional<User> userOptional = Optional.ofNullable(user);

		// When
		when(userRepository.findByUsername("myUsername")).thenReturn(userOptional);

		// Then
		assertThrows(IllegalStateException.class, () -> underTest.findByUsername("myUsername"),
				"Could not find user: " + "myUsername");
	}

	@Test
	void findById() {
		// Given
		User user = new User();
		user.setUsername("myUsername");
		List<Order> orders = new ArrayList<>();
		user.setOrders(orders);

		Optional<User> userOptional = Optional.of(user);

		// When
		when(userRepository.findById(1L)).thenReturn(userOptional);
		FullUserDTO userDTO = underTest.findById(1L);

		// Then
		assertEquals("myUsername", userDTO.getUsername());
	}

	@Test
	void findByIdShouldThrowAndExceptionWhenUserDoesNotExist() {
		User user = null;
		Optional<User> userOptional = Optional.ofNullable(user);

		// When
		when(userRepository.findById(1L)).thenReturn(userOptional);

		// Then
		assertThrows(IllegalStateException.class, () -> underTest.findById(1L), "Could not find user with id: " + 1L);
	}

}
