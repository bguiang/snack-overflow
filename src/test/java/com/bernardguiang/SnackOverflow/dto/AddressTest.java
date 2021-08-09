package com.bernardguiang.SnackOverflow.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.dto.request.UpdateBillingAndShippingRequest;

class AddressTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void addressValidationShouldPass() {
		// Given
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);

		// When
		Set<ConstraintViolation<Address>> violations = validator.validate(address);

		// Then
		// ... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	@Test
	void addressValidationShouldFailIfAddressLineOneIsNull() {
		// Given
		String addressLine1 = null;
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		
		// When
		Set<ConstraintViolation<Address>> violations = validator.validate(address);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<Address> violation : violations) {
		    assertEquals("Address Line One cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void addressValidationShouldFailIfAddressLineOneIsBlank() {
		// Given
		String addressLine1 = "   ";
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		
		// When
		Set<ConstraintViolation<Address>> violations = validator.validate(address);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<Address> violation : violations) {
		    assertEquals("Address Line One cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void addressValidationShouldFailIfCityIsNull() {
		// Given
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = null;
		String state = "state";
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		
		// When
		Set<ConstraintViolation<Address>> violations = validator.validate(address);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<Address> violation : violations) {
		    assertEquals("City cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void addressValidationShouldFailIfCityIsBlank() {
		// Given
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "   ";
		String state = "state";
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		
		// When
		Set<ConstraintViolation<Address>> violations = validator.validate(address);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<Address> violation : violations) {
		    assertEquals("City cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void addressValidationShouldFailIfStateIsNull() {
		// Given
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = null;
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		
		// When
		Set<ConstraintViolation<Address>> violations = validator.validate(address);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<Address> violation : violations) {
		    assertEquals("State cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void addressValidationShouldFailIfStateIsBlank() {
		// Given
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = "    ";
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		
		// When
		Set<ConstraintViolation<Address>> violations = validator.validate(address);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<Address> violation : violations) {
		    assertEquals("State cannot be null or blank", violation.getMessage());
		}
	}

	@Test
	void addressValidationShouldFailIfPostalCodeIsNull() {
		// Given
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = null;
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		
		// When
		Set<ConstraintViolation<Address>> violations = validator.validate(address);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<Address> violation : violations) {
		    assertEquals("PostalCode cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void addressValidationShouldFailIfPostalCodeIsBlank() {
		// Given
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = "    ";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		
		// When
		Set<ConstraintViolation<Address>> violations = validator.validate(address);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<Address> violation : violations) {
		    assertEquals("PostalCode cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void addressValidationShouldFailIfCountryIsNull() {
		// Given
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = "postal code";
		String country = null;
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		
		// When
		Set<ConstraintViolation<Address>> violations = validator.validate(address);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<Address> violation : violations) {
		    assertEquals("Country cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void addressValidationShouldFailIfCountryIsBlank() {
		// Given
		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = "postal code";
		String country = "   ";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);
		
		// When
		Set<ConstraintViolation<Address>> violations = validator.validate(address);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<Address> violation : violations) {
		    assertEquals("Country cannot be null or blank", violation.getMessage());
		}
	}
}
