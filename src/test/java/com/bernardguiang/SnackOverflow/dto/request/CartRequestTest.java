package com.bernardguiang.SnackOverflow.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

class CartRequestTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	@Test
	void cartInfoRequestValidationShouldPass() {
		// Given
		CartRequest request = new CartRequest();
		List<CartInfoRequestItem> emptyList = new ArrayList<>();
		CartInfoRequestItem item = new CartInfoRequestItem();
		item.setProductId(1L);
		item.setQuantity(1);
		emptyList.add(item);
		request.setItems(emptyList);
		
		// When
		Set<ConstraintViolation<CartRequest>> violations = validator.validate(request);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	@Test
	void cartInfoRequestValidationShouldFailIfItemsIsNull() {
		// Given
		CartRequest request = new CartRequest();
		
		// When
		Set<ConstraintViolation<CartRequest>> violations = validator.validate(request);
		
		// Then
		//... assert that violations exist
		assertTrue(!violations.isEmpty());
		for (ConstraintViolation<CartRequest> violation : violations) {
		    assertEquals("Cart Items cannot be null or empty", violation.getMessage());
		}
	}
	
	@Test
	void cartInfoRequestValidationShouldFailIfItemsIsEmpty() {
		// Given
		CartRequest request = new CartRequest();
		List<CartInfoRequestItem> emptyList = new ArrayList<>();
		request.setItems(emptyList);
		
		// When
		Set<ConstraintViolation<CartRequest>> violations = validator.validate(request);
		
		// Then
		//... assert that violations exist
		assertTrue(!violations.isEmpty());
		for (ConstraintViolation<CartRequest> violation : violations) {
		    assertEquals("Cart Items cannot be null or empty", violation.getMessage());
		}
	}

	@Test
	void cartInfoRequestValidationShouldFailIfItemIsNotValid() {
		// Given
		CartRequest request = new CartRequest();
		List<CartInfoRequestItem> list = new ArrayList<>();
		CartInfoRequestItem itemWithNoValues = new CartInfoRequestItem();
		list.add(itemWithNoValues);
		request.setItems(list);
		
		// When
		Set<ConstraintViolation<CartRequest>> violations = validator.validate(request);
		
		// Then
		//... assert that violations exist
		assertTrue(!violations.isEmpty());
	}
}
