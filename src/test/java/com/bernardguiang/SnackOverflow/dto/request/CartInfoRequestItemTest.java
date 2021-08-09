package com.bernardguiang.SnackOverflow.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

class CartInfoRequestItemTest {
	
	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	@Test
	void cartInfoRequestItemValidationShouldPass() {
		// Given
		CartInfoRequestItem cartInfoRequestItem = new CartInfoRequestItem();
		cartInfoRequestItem.setProductId(1L);
		cartInfoRequestItem.setQuantity(1);
		
		// When
		Set<ConstraintViolation<CartInfoRequestItem>> violations = validator.validate(cartInfoRequestItem);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	@Test
	void cartInfoRequestItemValidationShouldFailIfIdIsNull() {
		// Given
		CartInfoRequestItem cartInfoRequestItem = new CartInfoRequestItem();
		cartInfoRequestItem.setQuantity(1);
		
		// When
		Set<ConstraintViolation<CartInfoRequestItem>> violations = validator.validate(cartInfoRequestItem);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<CartInfoRequestItem> violation : violations) {
		    assertEquals("ProductId cannot be null", violation.getMessage());
		}
	}
	
	@Test
	void cartInfoRequestItemValidationShouldFailIfQuantityIsLessThanOne() {
		// Given
		CartInfoRequestItem cartInfoRequestItem = new CartInfoRequestItem();
		cartInfoRequestItem.setProductId(1L);
		
		// When
		Set<ConstraintViolation<CartInfoRequestItem>> violations = validator.validate(cartInfoRequestItem);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<CartInfoRequestItem> violation : violations) {
		    assertEquals("Item quantity cannot be zero", violation.getMessage());
		}
	}
}
