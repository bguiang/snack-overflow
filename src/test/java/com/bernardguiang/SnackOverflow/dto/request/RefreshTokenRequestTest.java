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
		String refreshToken = "refresh token";
		String username = "username";
		
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken(refreshToken);
		request.setUsername(username);
		
		// When
		Set<ConstraintViolation<RefreshTokenRequest>> violations = validator.validate(request);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	@Test
	void refreshTokenRequestValidationShouldFailIfRefreshTokenIsNull() {
		// Given
		String refreshToken = null;
		String username = "username";
		
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken(refreshToken);
		request.setUsername(username);
		
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
		String refreshToken = "  ";
		String username = "username";
		
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken(refreshToken);
		request.setUsername(username);
		
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
		String refreshToken = "refresh token";
		String username = null;
		
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken(refreshToken);
		request.setUsername(username);
		
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
		String refreshToken = "refresh token";
		String username = "abc12";
		
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken(refreshToken);
		request.setUsername(username);
		
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
		String refreshToken = "refresh token";
		String username = "12345678901234567890111";
		
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken(refreshToken);
		request.setUsername(username);
		
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
		String refreshToken = "refresh token";
		String username = "abcdefg$";
		
		RefreshTokenRequest request = new RefreshTokenRequest();
		request.setRefreshToken(refreshToken);
		request.setUsername(username);
		
		// When
		Set<ConstraintViolation<RefreshTokenRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RefreshTokenRequest> violation : violations) {
		    assertEquals("Username must consist of letters an numbers only", violation.getMessage());
		}
	}

}
