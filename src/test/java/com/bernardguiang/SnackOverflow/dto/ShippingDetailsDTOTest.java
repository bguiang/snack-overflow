package com.bernardguiang.SnackOverflow.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.ShippingDetails;

class ShippingDetailsDTOTest {

	@Test
	void itShouldConstructShippingDetailsDTOFromShippingDetails() {
		// Given
		Long id = 4L;
		String name = "Shipping Name";
		String phone = "1234567890";
		String addressLineOne = "address line 1";
		String addressLineTwo = "address line 2";
		String city = "city";
		String state = "state";
		String postalCode = "postal";
		String country = "country";

		Address address = new Address(
				addressLineOne,
				addressLineTwo, 
				city,
				state,
				postalCode,
				country);
		Order order = new Order();
		
		Long orderId = 2L;
		order.setId(orderId);
		
		ShippingDetails shippingDetails = new ShippingDetails(id, name, phone, address, order);
		
		// When
		ShippingDetailsDTO dto = new ShippingDetailsDTO(shippingDetails);
		
		// Then
		assertEquals(id, dto.getId());
		assertEquals(address, dto.getAddress());
		assertEquals(name, dto.getName());
		assertEquals(phone, dto.getPhone());
		assertEquals(orderId, dto.getOrderId());
	}
}
