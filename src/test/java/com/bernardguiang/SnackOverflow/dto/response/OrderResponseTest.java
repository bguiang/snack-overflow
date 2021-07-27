package com.bernardguiang.SnackOverflow.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.dto.BillingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.ShippingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.UpdateBillingAndShippingRequest;
import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.ShippingDetails;
import com.bernardguiang.SnackOverflow.model.User;

class OrderResponseTest {

	@Test
	void itShouldConstructOrderResponseFromOrder() {
		// Given
		Long orderId = 7L;
		BigDecimal total = new BigDecimal(10);
		Instant createDate = Instant.now();
		boolean isShippingSameAsBilling = false;
		Long userId = 2L;
		User user = new User();
		user.setId(userId);
		String clientSecret = "client secret";
		OrderStatus status = OrderStatus.COMPLETED;
		
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
		billingDetails.setId(1L);
		billingDetails.setName(name);
		billingDetails.setEmail(email);
		billingDetails.setPhone(phone);
		billingDetails.setAddress(address);

		ShippingDetails shippingDetails = new ShippingDetails();
		shippingDetails.setId(1L);
		shippingDetails.setName(name);
		shippingDetails.setPhone(phone);
		shippingDetails.setAddress(address);
		
		
		Order order = new Order();
		order.setId(orderId);
		order.setTotal(total);
		order.setCreatedDate(createDate);
		order.setBillingDetails(billingDetails);
		billingDetails.setOrder(order);
		order.setShippingDetails(shippingDetails);
		shippingDetails.setOrder(order);
		order.setShippingSameAsBilling(isShippingSameAsBilling);
		order.setUser(user);
		order.setClientSecret(clientSecret);
		order.setStatus(status);
		
		List<OrderItem> items = new ArrayList<>();
		OrderItem item1 = new OrderItem();
		item1.setId(1L);
		item1.setOrder(order);
		item1.setPrice(new BigDecimal(2));
		Product product1 = new Product();
		product1.setId(1L);
		item1.setProduct(product1);
		item1.setQuantity(5);
		OrderItem item2 = new OrderItem();
		item2.setId(2L);
		item2.setOrder(order);
		item2.setPrice(new BigDecimal(5));
		Product product2 = new Product();
		product2.setId(2L);
		item2.setProduct(product2);
		item2.setQuantity(2);
		items.add(item1);
		items.add(item2);
		order.setItems(items);
		
		// When
		OrderResponse orderResponse = new OrderResponse(order);
		
		// Then
		
		assertEquals(orderId, orderResponse.getId());
		assertEquals(userId, orderResponse.getUserId());
		assertEquals(total, orderResponse.getTotal());
		assertEquals(status, orderResponse.getStatus());
		assertEquals(userId, orderResponse.getUserId());
		assertEquals(isShippingSameAsBilling, orderResponse.getIsShippingSameAsBilling());
		assertEquals(createDate, orderResponse.getCreatedDate());

		// ... compare billing
		assertEquals(1L, orderResponse.getBillingDetails().getId());
		assertEquals(name, orderResponse.getBillingDetails().getName());
		assertEquals(email, orderResponse.getBillingDetails().getEmail());
		assertEquals(orderId, orderResponse.getBillingDetails().getOrderId());
		assertEquals(phone, orderResponse.getBillingDetails().getPhone());
		assertEquals(addressLineOne, orderResponse.getBillingDetails().getAddress().getAddressLineOne());
		assertEquals(addressLineTwo, orderResponse.getBillingDetails().getAddress().getAddressLineTwo());
		assertEquals(city, orderResponse.getBillingDetails().getAddress().getCity());
		assertEquals(state, orderResponse.getBillingDetails().getAddress().getState());
		assertEquals(postalCode, orderResponse.getBillingDetails().getAddress().getPostalCode());
		assertEquals(country, orderResponse.getBillingDetails().getAddress().getCountry());

		// ... compare shipping
		assertEquals(1L, orderResponse.getShippingDetails().getId());
		assertEquals(name, orderResponse.getShippingDetails().getName());
		assertEquals(orderId, orderResponse.getShippingDetails().getOrderId());
		assertEquals(phone, orderResponse.getShippingDetails().getPhone());
		assertEquals(addressLineOne, orderResponse.getShippingDetails().getAddress().getAddressLineOne());
		assertEquals(addressLineTwo, orderResponse.getShippingDetails().getAddress().getAddressLineTwo());
		assertEquals(city, orderResponse.getShippingDetails().getAddress().getCity());
		assertEquals(state, orderResponse.getShippingDetails().getAddress().getState());
		assertEquals(postalCode, orderResponse.getShippingDetails().getAddress().getPostalCode());
		assertEquals(country, orderResponse.getShippingDetails().getAddress().getCountry());

		// .. compare order items
		OrderResponseItem responseItem1 = orderResponse.getItems().get(0);
		OrderResponseItem responseItem2 = orderResponse.getItems().get(1);
		assertEquals(item1.getId(), responseItem1.getId());
		assertEquals(item1.getPrice(), responseItem1.getPrice());
		assertEquals(item1.getQuantity(), responseItem1.getQuantity());
		assertEquals(item1.getProduct().getId(), responseItem1.getProduct().getId());

		assertEquals(item2.getId(), responseItem2.getId());
		assertEquals(item2.getPrice(), responseItem2.getPrice());
		assertEquals(item2.getQuantity(), responseItem2.getQuantity());
		assertEquals(item2.getProduct().getId(), responseItem2.getProduct().getId());
	}
	
	@Test
	void shippingShouldBeNullWhenShippingIsSameAsBilling() {
		// Given
		Long orderId = 7L;
		BigDecimal total = new BigDecimal(10);
		Instant createDate = Instant.now();
		boolean isShippingSameAsBilling = true;
		Long userId = 2L;
		User user = new User();
		user.setId(userId);
		String clientSecret = "client secret";
		OrderStatus status = OrderStatus.COMPLETED;
		
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
		billingDetails.setId(1L);
		billingDetails.setName(name);
		billingDetails.setEmail(email);
		billingDetails.setPhone(phone);
		billingDetails.setAddress(address);

		ShippingDetails shippingDetails = new ShippingDetails();
		shippingDetails.setId(1L);
		shippingDetails.setName(name);
		shippingDetails.setPhone(phone);
		shippingDetails.setAddress(address);
		
		
		Order order = new Order();
		order.setId(orderId);
		order.setTotal(total);
		order.setCreatedDate(createDate);
		order.setBillingDetails(billingDetails);
		billingDetails.setOrder(order);
		order.setShippingDetails(shippingDetails);
		shippingDetails.setOrder(order);
		order.setShippingSameAsBilling(isShippingSameAsBilling);
		order.setUser(user);
		order.setClientSecret(clientSecret);
		order.setStatus(status);
		
		List<OrderItem> items = new ArrayList<>();
		OrderItem item1 = new OrderItem();
		item1.setId(1L);
		item1.setOrder(order);
		item1.setPrice(new BigDecimal(2));
		Product product1 = new Product();
		product1.setId(1L);
		item1.setProduct(product1);
		item1.setQuantity(5);
		OrderItem item2 = new OrderItem();
		item2.setId(2L);
		item2.setOrder(order);
		item2.setPrice(new BigDecimal(5));
		Product product2 = new Product();
		product2.setId(2L);
		item2.setProduct(product2);
		item2.setQuantity(2);
		items.add(item1);
		items.add(item2);
		order.setItems(items);
		
		// When
		OrderResponse orderResponse = new OrderResponse(order);
		
		// Then
		
		// ...Shipping Info
		assertEquals(isShippingSameAsBilling, orderResponse.getIsShippingSameAsBilling());
		assertEquals(null, orderResponse.getShippingDetails());
		
		assertEquals(orderId, orderResponse.getId());
		assertEquals(userId, orderResponse.getUserId());
		assertEquals(total, orderResponse.getTotal());
		assertEquals(status, orderResponse.getStatus());
		assertEquals(userId, orderResponse.getUserId());
		assertEquals(createDate, orderResponse.getCreatedDate());

		// ... compare billing
		assertEquals(1L, orderResponse.getBillingDetails().getId());
		assertEquals(name, orderResponse.getBillingDetails().getName());
		assertEquals(email, orderResponse.getBillingDetails().getEmail());
		assertEquals(orderId, orderResponse.getBillingDetails().getOrderId());
		assertEquals(phone, orderResponse.getBillingDetails().getPhone());
		assertEquals(addressLineOne, orderResponse.getBillingDetails().getAddress().getAddressLineOne());
		assertEquals(addressLineTwo, orderResponse.getBillingDetails().getAddress().getAddressLineTwo());
		assertEquals(city, orderResponse.getBillingDetails().getAddress().getCity());
		assertEquals(state, orderResponse.getBillingDetails().getAddress().getState());
		assertEquals(postalCode, orderResponse.getBillingDetails().getAddress().getPostalCode());
		assertEquals(country, orderResponse.getBillingDetails().getAddress().getCountry());

		// .. compare order items
		OrderResponseItem responseItem1 = orderResponse.getItems().get(0);
		OrderResponseItem responseItem2 = orderResponse.getItems().get(1);
		assertEquals(item1.getId(), responseItem1.getId());
		assertEquals(item1.getPrice(), responseItem1.getPrice());
		assertEquals(item1.getQuantity(), responseItem1.getQuantity());
		assertEquals(item1.getProduct().getId(), responseItem1.getProduct().getId());

		assertEquals(item2.getId(), responseItem2.getId());
		assertEquals(item2.getPrice(), responseItem2.getPrice());
		assertEquals(item2.getQuantity(), responseItem2.getQuantity());
		assertEquals(item2.getProduct().getId(), responseItem2.getProduct().getId());
	}

}
