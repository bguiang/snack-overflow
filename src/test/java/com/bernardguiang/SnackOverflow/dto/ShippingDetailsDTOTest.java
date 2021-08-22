package com.bernardguiang.SnackOverflow.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.ShippingDetails;

class ShippingDetailsDTOTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	private final Address validAddress = new Address(
			"Address Line 1",
			null, 
			"City",
			"State",
			"Postal Code",
			"Country");
	
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
		
		ShippingDetails shippingDetails = new ShippingDetails();
		shippingDetails.setId(id);
		shippingDetails.setName(name);
		shippingDetails.setPhone(phone);
		shippingDetails.setAddress(address);
		shippingDetails.setOrder(order);
		
		// When
		ShippingDetailsDTO dto = new ShippingDetailsDTO(shippingDetails);
		
		// Then
		assertEquals(id, dto.getId());
		assertEquals(address, dto.getAddress());
		assertEquals(name, dto.getName());
		assertEquals(phone, dto.getPhone());
		assertEquals(orderId, dto.getOrderId());
	}

	@Test
	void shippingDetailsDTOValidationShouldPass() {
		// Given
		ShippingDetailsDTO validShippingDetails = new ShippingDetailsDTO();
		validShippingDetails.setAddress(validAddress);
		validShippingDetails.setId(null);
		validShippingDetails.setName("valid name");
		validShippingDetails.setOrderId(1L);
		validShippingDetails.setPhone("1234567890");
		
		// When
		Set<ConstraintViolation<ShippingDetailsDTO>> violations = validator.validate(validShippingDetails);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	@Test
	void shippingDetailsDTOValidationShouldFailIfAddressIsNull() {
		// Given
		Address invalidAddress = null;
		ShippingDetailsDTO ShippingDetails = new ShippingDetailsDTO();
		ShippingDetails.setAddress(invalidAddress);
		ShippingDetails.setId(null);
		ShippingDetails.setName("valid name");
		ShippingDetails.setOrderId(1L);
		ShippingDetails.setPhone("1234567890");
		
		// When
		Set<ConstraintViolation<ShippingDetailsDTO>> violations = validator.validate(ShippingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ShippingDetailsDTO> violation : violations) {
		    assertEquals("Address cannot be null", violation.getMessage());
		}
	}
	
	@Test
	void shippingDetailsDTOValidationShouldFailIfAddressIsInvalid() {
		// Given
		Address invalidAddress = new Address(
				null,
				null, 
				"City",
				"State",
				"Postal Code",
				"Country");
		ShippingDetailsDTO ShippingDetails = new ShippingDetailsDTO();
		ShippingDetails.setAddress(invalidAddress);
		ShippingDetails.setId(null);
		ShippingDetails.setName("valid name");
		ShippingDetails.setOrderId(1L);
		ShippingDetails.setPhone("1234567890");
		
		// When
		Set<ConstraintViolation<ShippingDetailsDTO>> violations = validator.validate(ShippingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ShippingDetailsDTO> violation : violations) {
		    assertEquals("Address Line One cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void shippingDetailsDTOValidationShouldFailIfNameIsNull() {
		// Given
		ShippingDetailsDTO ShippingDetails = new ShippingDetailsDTO();
		ShippingDetails.setAddress(validAddress);
		ShippingDetails.setId(null);
		ShippingDetails.setName(null);
		ShippingDetails.setOrderId(1L);
		ShippingDetails.setPhone("1234567890");
		
		// When
		Set<ConstraintViolation<ShippingDetailsDTO>> violations = validator.validate(ShippingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ShippingDetailsDTO> violation : violations) {
		    assertEquals("Name cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void shippingDetailsDTOValidationShouldFailIfNameIsBlank() {
		// Given
		ShippingDetailsDTO ShippingDetails = new ShippingDetailsDTO();
		ShippingDetails.setAddress(validAddress);
		ShippingDetails.setId(null);
		ShippingDetails.setName("     ");
		ShippingDetails.setOrderId(1L);
		ShippingDetails.setPhone("1234567890");
		
		// When
		Set<ConstraintViolation<ShippingDetailsDTO>> violations = validator.validate(ShippingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ShippingDetailsDTO> violation : violations) {
		    assertEquals("Name cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void shippingDetailsDTOValidationShouldFailIfPhoneIsNull() {
		// Given
		ShippingDetailsDTO ShippingDetails = new ShippingDetailsDTO();
		ShippingDetails.setAddress(validAddress);
		ShippingDetails.setId(null);
		ShippingDetails.setName("name");
		ShippingDetails.setOrderId(1L);
		ShippingDetails.setPhone(null);
		
		// When
		Set<ConstraintViolation<ShippingDetailsDTO>> violations = validator.validate(ShippingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ShippingDetailsDTO> violation : violations) {
		    assertEquals("Phone cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void shippingDetailsDTOValidationShouldFailIfPhoneIsBlank() {
		// Given
		ShippingDetailsDTO ShippingDetails = new ShippingDetailsDTO();
		ShippingDetails.setAddress(validAddress);
		ShippingDetails.setId(null);
		ShippingDetails.setName("name");
		ShippingDetails.setOrderId(1L);
		ShippingDetails.setPhone("");
		
		// When
		Set<ConstraintViolation<ShippingDetailsDTO>> violations = validator.validate(ShippingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ShippingDetailsDTO> violation : violations) {
		    assertEquals("Phone cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void shippingDetailsDTOValidationShouldFailIfPhoneDoesNotContainTenDigits() {
		// Given
		ShippingDetailsDTO ShippingDetails = new ShippingDetailsDTO();
		ShippingDetails.setAddress(validAddress);
		ShippingDetails.setId(null);
		ShippingDetails.setName("name");
		ShippingDetails.setOrderId(1L);
		ShippingDetails.setPhone("asd123");
		
		// When
		Set<ConstraintViolation<ShippingDetailsDTO>> violations = validator.validate(ShippingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ShippingDetailsDTO> violation : violations) {
		    assertEquals("Phone number must consist of ten digits", violation.getMessage());
		}
	}
}
