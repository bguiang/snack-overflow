package com.bernardguiang.SnackOverflow.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.dto.request.OrderPage;
import com.bernardguiang.SnackOverflow.dto.request.OrderStatusUpdateRequest;
import com.bernardguiang.SnackOverflow.dto.request.StatsRequest;
import com.bernardguiang.SnackOverflow.dto.response.OrderDTO;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponse;
import com.bernardguiang.SnackOverflow.dto.response.OrderResponseItem;
import com.bernardguiang.SnackOverflow.dto.response.OrderStatsResponse;
import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.ShippingDetails;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.OrderRepository;

class OrderServiceTest {

	private OrderService underTest;

	private OrderRepository orderRepository;

	private static List<Order> ordersList;

	private Order completeOrder;

	static SimpleDateFormat dateFormat;

	@BeforeAll
	static void beforeAll() throws ParseException {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		Order order1 = new Order();
		order1.setStatus(OrderStatus.PAYMENT_PENDING);
		order1.setTotal(new BigDecimal(20));
		Date date1 = dateFormat.parse("15/02/1999");
		order1.setCreatedDate(date1.toInstant());

		Order order2 = new Order();
		order2.setStatus(OrderStatus.PROCESSING);
		order2.setTotal(new BigDecimal(10));
		Date date2 = dateFormat.parse("15/02/2000");
		order2.setCreatedDate(date2.toInstant());

		Order order3 = new Order();
		order3.setStatus(OrderStatus.COMPLETED);
		order3.setTotal(new BigDecimal(30));
		Date date3 = dateFormat.parse("15/02/2000");
		order3.setCreatedDate(date3.toInstant());

		Order order4 = new Order();
		order4.setStatus(OrderStatus.FAILED);
		order4.setTotal(new BigDecimal(5));
		Date date4 = dateFormat.parse("15/03/2000");
		order4.setCreatedDate(date4.toInstant());

		Order order5 = new Order();
		order5.setStatus(OrderStatus.CANCELLED);
		order5.setTotal(new BigDecimal(15));
		Date date5 = dateFormat.parse("15/03/2000");
		order5.setCreatedDate(date5.toInstant());

		Order order6 = new Order();
		order6.setStatus(OrderStatus.REFUNDED);
		order6.setTotal(new BigDecimal(10));
		Date date6 = dateFormat.parse("15/03/2000");
		order6.setCreatedDate(date6.toInstant());

		ordersList = Arrays.asList(order1, order2, order3, order4, order5, order6);
	}

	@BeforeEach
	void setUp() throws Exception {
		orderRepository = Mockito.mock(OrderRepository.class);

		underTest = new OrderService(orderRepository);

		completeOrder = new Order();
		completeOrder.setId(1L);
		completeOrder.setTotal(new BigDecimal(20));
		completeOrder.setCreatedDate(Instant.now());
		completeOrder.setShippingSameAsBilling(false);
		completeOrder.setStatus(OrderStatus.PROCESSING);

		List<OrderItem> items = new ArrayList<>();
		OrderItem item1 = new OrderItem();
		item1.setId(1L);
		item1.setOrder(completeOrder);
		item1.setPrice(new BigDecimal(2));
		Product product1 = new Product();
		product1.setId(1L);
		item1.setProduct(product1);
		item1.setQuantity(5);
		OrderItem item2 = new OrderItem();
		item2.setId(2L);
		item2.setOrder(completeOrder);
		item2.setPrice(new BigDecimal(5));
		Product product2 = new Product();
		product2.setId(2L);
		item2.setProduct(product2);
		item2.setQuantity(2);
		items.add(item1);
		items.add(item2);
		completeOrder.setItems(items);

		User user = new User();
		user.setId(2L);
		completeOrder.setUser(user);

		String name = "First Last";
		String email = "my@email.com";
		String phone = "1234567890";
		String addressLineOne = "address line 1";
		String addressLineTwo = "address line 2";
		String city = "city";
		String state = "state";
		String postalCode = "postal";
		String country = "country";

		Address address = new Address();
		address.setAddressLineOne(addressLineOne);
		address.setAddressLineTwo(addressLineTwo);
		address.setCity(city);
		address.setState(state);
		address.setPostalCode(postalCode);
		address.setCountry(country);

		BillingDetails billingDetails = new BillingDetails();
		billingDetails.setId(3L);
		billingDetails.setName(name);
		billingDetails.setEmail(email);
		billingDetails.setPhone(phone);
		billingDetails.setAddress(address);
		billingDetails.setOrder(completeOrder);
		completeOrder.setBillingDetails(billingDetails);

		ShippingDetails shippingDetails = new ShippingDetails();
		shippingDetails.setId(4L);
		shippingDetails.setName(name);
		shippingDetails.setPhone(phone);
		shippingDetails.setAddress(address);
		shippingDetails.setOrder(completeOrder);
		completeOrder.setShippingDetails(shippingDetails);
	}

	@Test
	void getOrderStatsAllTime() {
		// Given
		StatsRequest statsRequest = new StatsRequest();
		statsRequest.setRange("all");

		// When
		when(orderRepository.findAll()).thenReturn(ordersList);
		OrderStatsResponse orderStatsResponse = underTest.getOrderStats(statsRequest);

		// Then
		assertEquals(3, orderStatsResponse.getSuccessfulOrders());
		assertEquals(new BigDecimal(60), orderStatsResponse.getTotalIncome());
		assertEquals(3, orderStatsResponse.getUnsuccessfulOrders());
		assertEquals(new BigDecimal(30), orderStatsResponse.getUnsuccessfulPayments());
	}

	// TODO: need to figure out how to properly test this (time dependent)
	@Test
	void getOrderStatsForThisMonth() {
		// Given
		ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);

		StatsRequest statsRequest = new StatsRequest();
		statsRequest.setRange("month");
		LocalDate firstDayOfMonth = LocalDate.parse("2000-05-01");
		LocalDate localDateMock = Mockito.mock(LocalDate.class);

		MockedStatic<LocalDate> localDateMockedStatic = Mockito.mockStatic(LocalDate.class);

		// When
		localDateMockedStatic.when(() -> LocalDate.now()).thenReturn(localDateMock);
		when(localDateMock.withDayOfMonth(1)).thenReturn(firstDayOfMonth);
		underTest.getOrderStats(statsRequest);

		Mockito.verify(orderRepository).findAllByCreatedDateAfter(instantCaptor.capture());

		// Then
		Instant instantParam = instantCaptor.getValue();
		Date convert = Date.from(instantParam);
		assertNotNull(instantParam);
		assertEquals(1, convert.getDate());
		assertEquals(4, convert.getMonth());
		localDateMockedStatic.close();
	}

	// TODO: need to figure out how to properly test this (time dependent). Replace
	// argument captor with matcher
	@Test
	void getOrderStatsForThisYear() {
		// Given
		ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);

		StatsRequest statsRequest = new StatsRequest();
		statsRequest.setRange("year");
		LocalDate firstDayOfYear = LocalDate.parse("2000-01-01");
		LocalDate localDateMock = Mockito.mock(LocalDate.class);

		MockedStatic<LocalDate> localDateMockedStatic = Mockito.mockStatic(LocalDate.class);

		// When
		localDateMockedStatic.when(() -> LocalDate.now()).thenReturn(localDateMock);
		when(localDateMock.with(Mockito.any())).thenReturn(firstDayOfYear);
		underTest.getOrderStats(statsRequest);

		Mockito.verify(orderRepository).findAllByCreatedDateAfter(instantCaptor.capture());

		// Then
		Instant instantParam = instantCaptor.getValue();
		Date convert = Date.from(instantParam);
		assertNotNull(instantParam);
		assertEquals(1, convert.getDate());
		assertEquals(0, convert.getMonth());
		localDateMockedStatic.close();
	}

	@Test
	void findOrdersPaginated() {
		// Given
		OrderPage orderPage = new OrderPage();
		OrderDTO mockOrderDTO = Mockito.mock(OrderDTO.class);
		List<Object> orderDTOList = Arrays.asList(mockOrderDTO);
		Sort sort = Sort.by(orderPage.getSortDirection(), orderPage.getSortBy());
		Pageable pageable = PageRequest.of(orderPage.getPageNumber(), orderPage.getPageSize(), sort);

		Page<Object> dtoPage = new PageImpl<Object>(orderDTOList, pageable, orderDTOList.size());

		Page<Order> pageMock = Mockito.mock(Page.class);

		// When
		when(orderRepository.findAllByUserUsernameContainingIgnoreCase(Mockito.any(), Mockito.any()))
				.thenReturn(pageMock);
		when(pageMock.map(Mockito.any())).thenReturn(dtoPage);
		when(mockOrderDTO.getStatus()).thenReturn(OrderStatus.COMPLETED);

		Page<OrderDTO> result = underTest.findOrdersPaginated(orderPage);

		// Then
		assertEquals(dtoPage, result);
		assertEquals(1, result.getContent().size());
		assertEquals(OrderStatus.COMPLETED, result.getContent().get(0).getStatus());
	}

	@Test
	void findByIdIncludUserInfo() {
		// Given
		Optional<Order> orderOptional = Optional.ofNullable(completeOrder);

		// When
		when(orderRepository.findById(1L)).thenReturn(orderOptional);

		OrderDTO result = underTest.findByIdIncludUserInfo(1L);

		// Then
		assertEquals(1L, result.getId());
		assertEquals(2L, result.getUser().getId());
		assertEquals(new BigDecimal(20), result.getTotal());
		assertEquals(OrderStatus.PROCESSING, result.getStatus());
		assertEquals(false, result.getIsShippingSameAsBilling());
		assertEquals(3L, result.getBillingDetails().getId());
		assertEquals(4L, result.getShippingDetails().getId());
	}

	@Test
	void findByIdIncludUserInfoShouldThrowAnExceptionWhenNotFound() {
		// Given
		Optional<Order> orderOptional = Optional.ofNullable(null);
		// When
		when(orderRepository.findById(1L)).thenReturn(orderOptional);

		// Then
		assertThrows(IllegalStateException.class, () -> underTest.findByIdIncludUserInfo(1L),
				"Could not find Order with id: " + 1L);
	}

	@Test
	void findByIdAndUserId() {
		// Given
		Optional<Order> orderOptional = Optional.ofNullable(completeOrder);

		// When
		when(orderRepository.findByIdAndUserId(1L, 2L)).thenReturn(orderOptional);
		OrderResponse orderResponse = underTest.findByIdAndUserId(1L, 2L);

		// Then
		assertEquals(1L, orderResponse.getId());
		assertEquals(2L, orderResponse.getUserId());
		assertEquals(new BigDecimal(20), orderResponse.getTotal());
		assertEquals(OrderStatus.PROCESSING, orderResponse.getStatus());
		assertEquals(false, orderResponse.getIsShippingSameAsBilling());
		assertEquals(3L, orderResponse.getBillingDetails().getId());
		assertEquals(4L, orderResponse.getShippingDetails().getId());
	}

	@Test
	void findByIdAndUserIdShouldThrowAnExceptionWhenNotFound() {
		// Given
		Optional<Order> orderOptional = Optional.ofNullable(null);
		// When
		when(orderRepository.findByIdAndUserId(1L, 2L)).thenReturn(orderOptional);

		// Then
		assertThrows(IllegalStateException.class, () -> underTest.findByIdAndUserId(1L, 2L),
				"Could not find Order with id: " + 1L + " and userId: " + 2L);
	}

	@Test
	void findAllByUserId() {
		// Given
		List<Order> orders = Arrays.asList(completeOrder);

		// When
		when(orderRepository.findAllByUserId(1L)).thenReturn(orders);

		List<OrderResponse> result = underTest.findAllByUserId(1L);

		// Then
		assertEquals(1L, result.get(0).getId());
		assertEquals(2L, result.get(0).getUserId());
		assertEquals(new BigDecimal(20), result.get(0).getTotal());
		assertEquals(OrderStatus.PROCESSING, result.get(0).getStatus());
		assertEquals(false, result.get(0).getIsShippingSameAsBilling());
		assertEquals(3L, result.get(0).getBillingDetails().getId());
		assertEquals(4L, result.get(0).getShippingDetails().getId());
	}

	@Test
	void updateOrderStatus() {
		// Given
		OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
		request.setId(1L);
		request.setStatus(OrderStatus.CANCELLED);

		Optional<Order> orderOptional = Optional.ofNullable(completeOrder);

		// When
		when(orderRepository.findById(1L)).thenReturn(orderOptional);
		when(orderRepository.save(completeOrder)).thenReturn(completeOrder);
		OrderResponse orderResponse = underTest.updateOrderStatus(request);

		// Then
		assertEquals(1L, orderResponse.getId());
		assertEquals(2L, orderResponse.getUserId());
		assertEquals(new BigDecimal(20), orderResponse.getTotal());
		assertEquals(OrderStatus.CANCELLED, orderResponse.getStatus());
		assertEquals(false, orderResponse.getIsShippingSameAsBilling());
		assertEquals(3L, orderResponse.getBillingDetails().getId());
		assertEquals(4L, orderResponse.getShippingDetails().getId());
	}

	@Test
	void updateOrderStatusShouldThrowAnExceptionIfOrderNotfound() {
		// Given
		OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
		request.setId(1L);
		request.setStatus(OrderStatus.CANCELLED);
		
		Optional<Order> orderOptional = Optional.ofNullable(null);
		// When
		when(orderRepository.findById(1L)).thenReturn(orderOptional);

		// Then
		assertThrows(IllegalStateException.class, () -> underTest.updateOrderStatus(request),
			"Could not find Order with id: " + 1L);
	}
}
