package com.bernardguiang.SnackOverflow.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatsRequestTest {
	
	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void statsRequestShouldPassIfRangeIsAll() {
		// Given
		StatsRequest underTest = new StatsRequest();
		underTest.setRange("all");
		
		// When
		Set<ConstraintViolation<StatsRequest>> violations = validator.validate(underTest);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	@Test
	void statsRequestShouldPassIfRangeIsMonth() {
		// Given
		StatsRequest underTest = new StatsRequest();
		underTest.setRange("month");
		
		// When
		Set<ConstraintViolation<StatsRequest>> violations = validator.validate(underTest);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	@Test
	void statsRequestShouldPassIfRangeIsYear() {
		// Given
		StatsRequest underTest = new StatsRequest();
		underTest.setRange("year");
		
		// When
		Set<ConstraintViolation<StatsRequest>> violations = validator.validate(underTest);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	
	@Test
	void cartInfoRequestItemValidationShouldFailIfRangeIsBlank() {
		// Given
		StatsRequest underTest = new StatsRequest();
		underTest.setRange("");
		
		// When
		Set<ConstraintViolation<StatsRequest>> violations = validator.validate(underTest);
		
		// Then
		assertEquals(2, violations.size());
		for (ConstraintViolation<StatsRequest> violation : violations) {
		    assertEquals("Please include a `range` query parameter with value of `all`, `month`, or `year`", violation.getMessage());
		}
	}

	@Test
	void cartInfoRequestItemValidationShouldFailIfRangeIsInvalid() {
		// Given
		StatsRequest underTest = new StatsRequest();
		underTest.setRange("notyear");
		
		// When
		Set<ConstraintViolation<StatsRequest>> violations = validator.validate(underTest);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<StatsRequest> violation : violations) {
		    assertEquals("Please include a `range` query parameter with value of `all`, `month`, or `year`", violation.getMessage());
		}
	}
}
