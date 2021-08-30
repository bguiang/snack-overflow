package com.bernardguiang.SnackOverflow.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

class RefreshTokenRequestTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void refreshTokenRequestValidationShouldPass() {
		// Given
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken("refresh token");
		request.setUsername("username");
		
		// When
		Set<ConstraintViolation<RefreshTokenRequest>> violations = validator.validate(request);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	@Test
	void refreshTokenRequestValidationShouldFailIfRefreshTokenIsNull() {
		// Given
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken(null);
		request.setUsername("username");
		
		// When
		Set<ConstraintViolation<RefreshTokenRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RefreshTokenRequest> violation : violations) {
		    assertEquals("RefreshToken cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void refreshTokenRequestValidationShouldFailIfRefreshTokenIsEmpty() {
		// Given
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken("  ");
		request.setUsername("username");
		
		// When
		Set<ConstraintViolation<RefreshTokenRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RefreshTokenRequest> violation : violations) {
		    assertEquals("RefreshToken cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void refreshTokenRequestValidationShouldFailIfUsernameIsNull() {
		// Given
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken("refresh token");
		request.setUsername(null);
		
		// When
		Set<ConstraintViolation<RefreshTokenRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RefreshTokenRequest> violation : violations) {
		    assertEquals("Username cannot be null", violation.getMessage());
		}
	}
	
	@Test
	void refreshTokenRequestValidationShouldFailIfUsernameLengthIsLessThanSix() {
		// Given
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken("refresh token");
		request.setUsername("abc12");
		
		// When
		Set<ConstraintViolation<RefreshTokenRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RefreshTokenRequest> violation : violations) {
		    assertEquals("Username must be at least 6 characters and cannot be longer than 20 characters", violation.getMessage());
		}
	}
	
	@Test
	void refreshTokenRequestValidationShouldFailIfUsernameLengthIsMoreThanTwenty() {
		// Given
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken("refresh token");
		request.setUsername("12345678901234567890111");
		
		// When
		Set<ConstraintViolation<RefreshTokenRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RefreshTokenRequest> violation : violations) {
		    assertEquals("Username must be at least 6 characters and cannot be longer than 20 characters", violation.getMessage());
		}
	}
	
	@Test
	void refreshTokenRequestValidationShouldFailIfUsernameContainsNonAlphabeticOrNonNumericCharacter() {
		// Given		
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken("refresh token");
		request.setUsername("abcdefg$");
		
		// When
		Set<ConstraintViolation<RefreshTokenRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RefreshTokenRequest> violation : violations) {
		    assertEquals("Username must consist of letters an numbers only", violation.getMessage());
		}
	}

}
