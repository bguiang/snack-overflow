package com.bernardguiang.SnackOverflow.dto.request;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

class LoginRequestTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	private final String defaultPasswordViolationMessage = "Invalid Password";

	@Test
	void loginRequestValidationShouldPass() {
		// Given
		String username = "username";
		String password = "Password123!";
		
		LoginRequest request = new LoginRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	@Test
	void loginRequestValidationShouldFailIfUsernameLengthIsLessThanSix() {
		// Given
		String username = "abc";
		String password = "Password123!";
		
		LoginRequest request = new LoginRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<LoginRequest> violation : violations) {
		    assertEquals("Username must be at least 6 characters and cannot be longer than 20 characters", violation.getMessage());
		}
	}
	
	@Test
	void loginRequestValidationShouldFailIfUsernameLengthIsGreaterThanTwenty() {
		// Given
		String username = "123456789012345678901";
		String password = "Password123!";
		
		LoginRequest request = new LoginRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<LoginRequest> violation : violations) {
		    assertEquals("Username must be at least 6 characters and cannot be longer than 20 characters", violation.getMessage());
		}
	}
	
	@Test
	void loginRequestValidationShouldShouldFailIfUsernameContainsNonAlphabeticOrNonNumericCharacter() {
		// Given
		String username = "abcdefg$";
		String password = "Password123!";
		
		LoginRequest request = new LoginRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<LoginRequest> violation : violations) {
		    assertEquals("Username must consist of letters an numbers only", violation.getMessage());
		}
	}
	
	////////////////////////////
	@Test
	void loginRequestValidationShouldShouldFailIfPasswordIsNull() {
		// Given
		String expectedPasswordViolationMessage = "Password is null";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String username = "username";
		String password = null;
		
		LoginRequest request = new LoginRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		for (ConstraintViolation<LoginRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}
	
	@Test
	void loginRequestValidationShouldShouldFailIfPasswordLengthIsLessThanSix() {
		// Given
		String expectedPasswordViolationMessage = "Password must be 6 or more characters in length.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String username = "username";
		String password = "Aa!2";
		
		LoginRequest request = new LoginRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<LoginRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}
	
	@Test
	void loginRequestValidationShouldShouldFailIfPasswordLengthIsMoreThanTwenty() {
		// Given
		String expectedPasswordViolationMessage = "Password must be no more than 20 characters in length.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String username = "username";
		String password = "Aa!200000000000000000";
		
		LoginRequest request = new LoginRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<LoginRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}

	@Test
	void loginRequestValidationShouldShouldFailIfPasswordDoesNotContainAtLeastOneUppercase() {
		// Given
		String expectedPasswordViolationMessage = "Password must contain 1 or more uppercase characters.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String username = "username";
		String password = "password123!";
		
		LoginRequest request = new LoginRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<LoginRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}
	
	@Test
	void loginRequestValidationShouldShouldFailIfPasswordDoesNotContainAtLeastOneLowercase() {
		// Given
		String expectedPasswordViolationMessage = "Password must contain 1 or more lowercase characters.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String username = "username";
		String password = "PASSWORD123!";
		
		LoginRequest request = new LoginRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<LoginRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}
	
	@Test
	void loginRequestValidationShouldShouldFailIfPasswordDoesNotContainAtLeastOneDigit() {
		// Given
		String expectedPasswordViolationMessage = "Password must contain 1 or more digit characters.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String username = "username";
		String password = "Password!";
		
		LoginRequest request = new LoginRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<LoginRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}
	
	@Test
	void loginRequestValidationShouldShouldFailIfPasswordDoesNotContainAtLeastOneSpecialCharacter() {
		// Given
		String expectedPasswordViolationMessage = "Password must contain 1 or more special characters.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String username = "username";
		String password = "Password123";
		
		LoginRequest request = new LoginRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<LoginRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}
	
	@Test
	void loginRequestValidationShouldShouldFailIfPasswordDoesNotContainContainsWhitespace() {
		// Given
		String expectedPasswordViolationMessage = "Password contains a whitespace character.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String username = "username";
		String password = "P assword123!";
		
		LoginRequest request = new LoginRequest();
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<LoginRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}

}
