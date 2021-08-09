package com.bernardguiang.SnackOverflow.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.dto.BillingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.ShippingDetailsDTO;

class UpdateBillingAndShippingRequestTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	private final ShippingDetailsDTO ignoredShippingDetails = null;
	private final Address validAddress = new Address(
			"Address Line 1",
			null, 
			"City",
			"State",
			"Postal Code",
			"Country");

	@Test
	void UpdateBillingAndShippingRequestValidationShouldPass() {
		// Given
		Long id = 1L;
		BillingDetailsDTO validBillingDetails = new BillingDetailsDTO();
		validBillingDetails.setAddress(validAddress);
		validBillingDetails.setEmail("valid@email.com");
		validBillingDetails.setId(id);
		validBillingDetails.setName("valid name");
		validBillingDetails.setOrderId(1L);
		validBillingDetails.setPhone("1234567890");
		
		UpdateBillingAndShippingRequest request = new UpdateBillingAndShippingRequest();
		request.setBillingDetails(validBillingDetails);
		request.setId(id);
		request.setShippingDetails(ignoredShippingDetails);
		request.setShippingSameAsBilling(true);
		
		// When
		Set<ConstraintViolation<UpdateBillingAndShippingRequest>> violations = validator.validate(request);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	@Test
	void UpdateBillingAndShippingRequestValidationShouldFailIfIdIsNull() {
		// Given
		Long id = null;
		
		BillingDetailsDTO validBillingDetails = new BillingDetailsDTO();
		validBillingDetails.setAddress(validAddress);
		validBillingDetails.setEmail("valid@email.com");
		validBillingDetails.setId(id);
		validBillingDetails.setName("valid name");
		validBillingDetails.setOrderId(1L);
		validBillingDetails.setPhone("1234567890");
		
		UpdateBillingAndShippingRequest request = new UpdateBillingAndShippingRequest();
		request.setBillingDetails(validBillingDetails);
		request.setId(id);
		request.setShippingDetails(ignoredShippingDetails);
		request.setShippingSameAsBilling(true);
		
		// When
		Set<ConstraintViolation<UpdateBillingAndShippingRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<UpdateBillingAndShippingRequest> violation : violations) {
		    assertEquals("ID cannot be null", violation.getMessage());
		}
	}
	
	@Test
	void UpdateBillingAndShippingRequestValidationShouldFailIfBillingDetailsIsNull() {
		// Given
		Long id = 1L;
		
		UpdateBillingAndShippingRequest request = new UpdateBillingAndShippingRequest();
		request.setBillingDetails(null);
		//request.setBillingDetails(null);
		request.setId(id);
		request.setShippingDetails(ignoredShippingDetails);
		request.setShippingSameAsBilling(true);
		
		// When
		Set<ConstraintViolation<UpdateBillingAndShippingRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<UpdateBillingAndShippingRequest> violation : violations) {
		    assertEquals("Billing Details cannot be null", violation.getMessage());
		}
	}
	
	@Test
	void UpdateBillingAndShippingRequestValidationShouldFailIfBillingDetailsIsInvalid() {
		// Given
		Long id = 1L;
		
		BillingDetailsDTO invalidBillingDetails = new BillingDetailsDTO();
		invalidBillingDetails.setAddress(validAddress);
		invalidBillingDetails.setEmail(""); // invalid value
		invalidBillingDetails.setId(id);
		invalidBillingDetails.setName("valid name");
		invalidBillingDetails.setOrderId(1L);
		invalidBillingDetails.setPhone("1234567890");
		
		UpdateBillingAndShippingRequest request = new UpdateBillingAndShippingRequest();
		request.setBillingDetails(invalidBillingDetails);
		request.setId(id);
		request.setShippingDetails(ignoredShippingDetails);
		request.setShippingSameAsBilling(true);
		
		// When
		Set<ConstraintViolation<UpdateBillingAndShippingRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<UpdateBillingAndShippingRequest> violation : violations) {
		    assertEquals("Email cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void UpdateBillingAndShippingRequestValidationShouldFailIfShippingDetailsIsInvalid() {
		// Given
		Long id = 1L;
		BillingDetailsDTO validBillingDetails = new BillingDetailsDTO();
		validBillingDetails.setAddress(validAddress);
		validBillingDetails.setEmail("valid@email.com");
		validBillingDetails.setId(id);
		validBillingDetails.setName("valid name");
		validBillingDetails.setOrderId(1L);
		validBillingDetails.setPhone("1234567890");
		
		ShippingDetailsDTO invalidShippingDetails = new ShippingDetailsDTO();
		invalidShippingDetails.setAddress(validAddress);
		invalidShippingDetails.setId(id);
		invalidShippingDetails.setName("valid name");
		invalidShippingDetails.setOrderId(1L);
		invalidShippingDetails.setPhone("invalid_phone"); // invalid phone
		
		UpdateBillingAndShippingRequest request = new UpdateBillingAndShippingRequest();
		request.setBillingDetails(validBillingDetails);
		request.setId(id);
		request.setShippingDetails(invalidShippingDetails);
		request.setShippingSameAsBilling(false);
		
		// When
		Set<ConstraintViolation<UpdateBillingAndShippingRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<UpdateBillingAndShippingRequest> violation : violations) {
		    assertEquals("Phone number must consist of ten digits", violation.getMessage());
		}
	}
}
