package com.bernardguiang.SnackOverflow.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.model.OrderStatus;

class OrderStatusUpdateRequestTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void orderStatusUpdateRequestValidationShouldPass() {
		// Given
		OrderStatusUpdateRequest underTest = new OrderStatusUpdateRequest();
		underTest.setId(2L);
		underTest.setStatus(OrderStatus.COMPLETED);

		// When
		Set<ConstraintViolation<OrderStatusUpdateRequest>> violations = validator.validate(underTest);

		// Then
		// ... assert that NO violations exist
		assertEquals(0, violations.size());
	}

	@Test
	void orderStatusUpdateRequestValidationShouldFailIfIdIsNull() {
		// Given
		OrderStatusUpdateRequest underTest = new OrderStatusUpdateRequest();
		underTest.setStatus(OrderStatus.COMPLETED);

		// When
		Set<ConstraintViolation<OrderStatusUpdateRequest>> violations = validator.validate(underTest);

		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<OrderStatusUpdateRequest> violation : violations) {
		    assertEquals("ID cannot be null", violation.getMessage());
		}
	}
	
	@Test
	void orderStatusUpdateRequestValidationShouldFailIfOrderStatusIsNull() {
		// Given
		OrderStatusUpdateRequest underTest = new OrderStatusUpdateRequest();
		underTest.setId(2L);

		// When
		Set<ConstraintViolation<OrderStatusUpdateRequest>> violations = validator.validate(underTest);

		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<OrderStatusUpdateRequest> violation : violations) {
		    assertEquals("OrderStatus cannot be null", violation.getMessage());
		}
	}
}
