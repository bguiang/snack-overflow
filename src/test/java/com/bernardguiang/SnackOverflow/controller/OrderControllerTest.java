package com.bernardguiang.SnackOverflow.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.OrderPage;
import com.bernardguiang.SnackOverflow.dto.request.OrderStatusUpdateRequest;
import com.bernardguiang.SnackOverflow.dto.request.StatsRequest;
import com.bernardguiang.SnackOverflow.dto.response.OrderDTO;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponse;
import com.bernardguiang.SnackOverflow.dto.response.OrderStatsResponse;
import com.bernardguiang.SnackOverflow.service.OrderService;
import com.bernardguiang.SnackOverflow.service.UserService;

class OrderControllerTest {

	private OrderController underTest;
	
	private OrderService orderService;
	private UserService userService;
	
	@BeforeEach
	void setUp() throws Exception {
		orderService = Mockito.mock(OrderService.class);
		userService = Mockito.mock(UserService.class);
		
		underTest = new OrderController(orderService, userService);
	}
	
	@Test
	void getOrdersByCurrentUser() {
		// Given
		Authentication authenticationMock = Mockito.mock(Authentication.class);
		UserDTO user = new UserDTO();
		user.setId(1L);
		OrderResponse orderResponseMock = Mockito.mock(OrderResponse.class);
		List<OrderResponse> orderResponseList = Arrays.asList(orderResponseMock);
		
		// When
		when(orderResponseMock.getId()).thenReturn(2L);
		
		when(authenticationMock.getName()).thenReturn("username");
		when(userService.findByUsername("username")).thenReturn(user);
		when(orderService.findAllByUserId(1L)).thenReturn(orderResponseList);
		List<OrderResponse> response = underTest.getOrdersByCurrentUser(authenticationMock);
		
		// Then
		assertEquals(1, response.size());
		assertEquals(2L, response.get(0).getId());
	}
	
	@Test
	void getOrderByCurrentUser() {
		// Given
		Authentication authenticationMock = Mockito.mock(Authentication.class);
		OrderResponse orderResponseMock = Mockito.mock(OrderResponse.class);
		UserDTO user = new UserDTO();
		user.setId(1L);
		
		// When
		when(orderResponseMock.getId()).thenReturn(2L);
		
		when(authenticationMock.getName()).thenReturn("username");
		when(userService.findByUsername("username")).thenReturn(user);
		when(orderService.findByIdAndUserId(2L, 1L)).thenReturn(orderResponseMock);
		OrderResponse orderResponse = underTest.getOrderByCurrentUser(2L, authenticationMock);
		
		// Then
		assertEquals(2L, orderResponse.getId());
	}
	
	@Test
	void getOrders() {
		// Given
		Page<OrderDTO> pageMock = Mockito.mock(Page.class);
		OrderPage orderPageMock = Mockito.mock(OrderPage.class);
		
		// When
		when(orderService.findOrdersPaginated(orderPageMock)).thenReturn(pageMock);
		Page<OrderDTO> result = underTest.getOrders(orderPageMock);
		
		// Then
		assertEquals(pageMock, result);
		
	}
	
	@Test
	void getOrder() {
		// Given
		OrderDTO orderDTOMock = Mockito.mock(OrderDTO.class);
		
		// When
		when(orderService.findByIdIncludUserInfo(1L)).thenReturn(orderDTOMock);
		OrderDTO result = underTest.getOrder(1L);
		
		// Then
		assertEquals(orderDTOMock, result);
	}
	
	@Test
	void updateOrderStatus() {
		// Given
		OrderStatusUpdateRequest orderStatusUpdate = Mockito.mock(OrderStatusUpdateRequest.class);
		OrderResponse orderResponseMock = Mockito.mock(OrderResponse.class);
		
		// When
		when(orderService.updateOrderStatus(orderStatusUpdate)).thenReturn(orderResponseMock);
		OrderResponse response = underTest.updateOrderStatus(orderStatusUpdate);
				
		// Then
		assertEquals(orderResponseMock, response);
	}
	
	@Test
	void getOrderStats() {
		// Given
		OrderStatsResponse orderStatsResponseMock = Mockito.mock(OrderStatsResponse.class);
		StatsRequest statsRequestMock = Mockito.mock(StatsRequest.class);
		
		// When
		when(orderService.getOrderStats(statsRequestMock)).thenReturn(orderStatsResponseMock);
		OrderStatsResponse response =  underTest.getOrderStats(statsRequestMock);
		
		// Then
		assertEquals(orderStatsResponseMock, response);
	}

}
