package com.bernardguiang.SnackOverflow.dto;

import static org.junit.jupiter.api.Assertions.*;

import javax.validation.constraints.NotBlank;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Order;

class BillingDetailsDTOTest {

	@Test
	void itShouldConstructBillingDetailsDTOFromBillingDetails() {
		// Given
		Long id = 4L;
		String name = "Billing Name";
		String email = "billing@billing.com";
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
		
		BillingDetails billingDetails = new BillingDetails(id, name, email, phone, address, order);
		
		// When
		BillingDetailsDTO dto = new BillingDetailsDTO(billingDetails);
		
		// Then
		assertEquals(id, dto.getId());
		assertEquals(address, dto.getAddress());
		assertEquals(name, dto.getName());
		assertEquals(phone, dto.getPhone());
		assertEquals(email, dto.getEmail());
		assertEquals(orderId, dto.getOrderId());
	}
	
	@Test
	void itShouldThrowAnExceptionIfBillingDetailsIsNull() {
		// Given
		BillingDetails billingDetails = null;
		
		// When
		// Then
		assertThrows(NullPointerException.class, ()-> new BillingDetailsDTO(billingDetails));
	}

}
