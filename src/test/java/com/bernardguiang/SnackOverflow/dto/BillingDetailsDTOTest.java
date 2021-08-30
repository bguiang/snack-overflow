package com.bernardguiang.SnackOverflow.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Order;

class BillingDetailsDTOTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	private final Address validAddress = new Address(
			"Address Line 1",
			null, 
			"City",
			"State",
			"Postal Code",
			"Country");
	
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

		//TODO: the valid address already exists?
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
		
		BillingDetails billingDetails = new BillingDetails();
		billingDetails.setId(id);
		billingDetails.setAddress(address);
		billingDetails.setEmail(email);
		billingDetails.setName(name);
		billingDetails.setOrder(order);
		billingDetails.setPhone(phone);
		
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
	void billingDetailsDTOValidationShouldPass() {
		// Given
		BillingDetailsDTO validBillingDetails = new BillingDetailsDTO();
		validBillingDetails.setAddress(validAddress);
		validBillingDetails.setEmail("valid@email.com");
		validBillingDetails.setId(null);
		validBillingDetails.setName("valid name");
		validBillingDetails.setOrderId(1L);
		validBillingDetails.setPhone("1234567890");
		
		// When
		Set<ConstraintViolation<BillingDetailsDTO>> violations = validator.validate(validBillingDetails);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	@Test
	void billingDetailsDTOValidationShouldFailIfAddressIsNull() {
		// Given
		Address invalidAddress = null;
		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
		billingDetails.setAddress(invalidAddress);
		billingDetails.setEmail("valid@email.com");
		billingDetails.setId(null);
		billingDetails.setName("valid name");
		billingDetails.setOrderId(1L);
		billingDetails.setPhone("1234567890");
		
		// When
		Set<ConstraintViolation<BillingDetailsDTO>> violations = validator.validate(billingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<BillingDetailsDTO> violation : violations) {
		    assertEquals("Address cannot be null", violation.getMessage());
		}
	}
	
	@Test
	void billingDetailsDTOValidationShouldFailIfAddressIsInvalid() {
		// Given
		Address invalidAddress = new Address(
				null,
				null, 
				"City",
				"State",
				"Postal Code",
				"Country");
		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
		billingDetails.setAddress(invalidAddress);
		billingDetails.setEmail("valid@email.com");
		billingDetails.setId(null);
		billingDetails.setName("valid name");
		billingDetails.setOrderId(1L);
		billingDetails.setPhone("1234567890");
		
		// When
		Set<ConstraintViolation<BillingDetailsDTO>> violations = validator.validate(billingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<BillingDetailsDTO> violation : violations) {
		    assertEquals("Address Line One cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void billingDetailsDTOValidationShouldFailIfNameIsNull() {
		// Given
		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
		billingDetails.setAddress(validAddress);
		billingDetails.setEmail("valid@email.com");
		billingDetails.setId(null);
		billingDetails.setName(null);
		billingDetails.setOrderId(1L);
		billingDetails.setPhone("1234567890");
		
		// When
		Set<ConstraintViolation<BillingDetailsDTO>> violations = validator.validate(billingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<BillingDetailsDTO> violation : violations) {
		    assertEquals("Name cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void billingDetailsDTOValidationShouldFailIfNameIsBlank() {
		// Given
		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
		billingDetails.setAddress(validAddress);
		billingDetails.setEmail("valid@email.com");
		billingDetails.setId(null);
		billingDetails.setName("     ");
		billingDetails.setOrderId(1L);
		billingDetails.setPhone("1234567890");
		
		// When
		Set<ConstraintViolation<BillingDetailsDTO>> violations = validator.validate(billingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<BillingDetailsDTO> violation : violations) {
		    assertEquals("Name cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void billingDetailsDTOValidationShouldFailIfEmailIsNull() {
		// Given
		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
		billingDetails.setAddress(validAddress);
		billingDetails.setEmail(null);
		billingDetails.setId(null);
		billingDetails.setName("name");
		billingDetails.setOrderId(1L);
		billingDetails.setPhone("1234567890");
		
		// When
		Set<ConstraintViolation<BillingDetailsDTO>> violations = validator.validate(billingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<BillingDetailsDTO> violation : violations) {
		    assertEquals("Email cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void billingDetailsDTOValidationShouldFailIfEmailIsBlank() {
		// Given
		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
		billingDetails.setAddress(validAddress);
		billingDetails.setEmail("");
		billingDetails.setId(null);
		billingDetails.setName("name");
		billingDetails.setOrderId(1L);
		billingDetails.setPhone("1234567890");
		
		// When
		Set<ConstraintViolation<BillingDetailsDTO>> violations = validator.validate(billingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<BillingDetailsDTO> violation : violations) {
		    assertEquals("Email cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void billingDetailsDTOValidationShouldFailIfEmailIsInvalid() {
		// Given
		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
		billingDetails.setAddress(validAddress);
		billingDetails.setEmail("asdlkuj");
		billingDetails.setId(null);
		billingDetails.setName("name");
		billingDetails.setOrderId(1L);
		billingDetails.setPhone("1234567890");
		
		// When
		Set<ConstraintViolation<BillingDetailsDTO>> violations = validator.validate(billingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<BillingDetailsDTO> violation : violations) {
		    assertEquals("Must use a valid email", violation.getMessage());
		}
	}
	
	@Test
	void billingDetailsDTOValidationShouldFailIfPhoneIsNull() {
		// Given
		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
		billingDetails.setAddress(validAddress);
		billingDetails.setEmail("test@email.com");
		billingDetails.setId(null);
		billingDetails.setName("name");
		billingDetails.setOrderId(1L);
		billingDetails.setPhone(null);
		
		// When
		Set<ConstraintViolation<BillingDetailsDTO>> violations = validator.validate(billingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<BillingDetailsDTO> violation : violations) {
		    assertEquals("Phone cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void billingDetailsDTOValidationShouldFailIfPhoneIsBlank() {
		// Given
		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
		billingDetails.setAddress(validAddress);
		billingDetails.setEmail("test@email.com");
		billingDetails.setId(null);
		billingDetails.setName("name");
		billingDetails.setOrderId(1L);
		billingDetails.setPhone("");
		
		// When
		Set<ConstraintViolation<BillingDetailsDTO>> violations = validator.validate(billingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<BillingDetailsDTO> violation : violations) {
		    assertEquals("Phone cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void billingDetailsDTOValidationShouldFailIfPhoneDoesNotContainTenDigits() {
		// Given
		BillingDetailsDTO billingDetails = new BillingDetailsDTO();
		billingDetails.setAddress(validAddress);
		billingDetails.setEmail("test@email.com");
		billingDetails.setId(null);
		billingDetails.setName("name");
		billingDetails.setOrderId(1L);
		billingDetails.setPhone("asd123");
		
		// When
		Set<ConstraintViolation<BillingDetailsDTO>> violations = validator.validate(billingDetails);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<BillingDetailsDTO> violation : violations) {
		    assertEquals("Phone number must consist of ten digits", violation.getMessage());
		}
	}
}
