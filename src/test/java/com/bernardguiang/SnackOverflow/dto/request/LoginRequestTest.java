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
		LoginRequest request = new LoginRequest();
		request.setUsername("username");
		request.setPassword("Password123!");
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	@Test
	void loginRequestValidationShouldFailIfUsernameLengthIsLessThanSix() {
		// Given
		LoginRequest request = new LoginRequest();
		request.setUsername( "abc");
		request.setPassword("Password123!");
		
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
		LoginRequest request = new LoginRequest();
		request.setUsername("123456789012345678901");
		request.setPassword("Password123!");
		
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
		LoginRequest request = new LoginRequest();
		request.setUsername("abcdefg$");
		request.setPassword("Password123!");
		
		// When
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<LoginRequest> violation : violations) {
		    assertEquals("Username must consist of letters an numbers only", violation.getMessage());
		}
	}
	
	@Test
	void loginRequestValidationShouldShouldFailIfPasswordIsNull() {
		// Given
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password is null");
		
		LoginRequest request = new LoginRequest();
		request.setUsername("username");
		request.setPassword(null);
		
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
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password must be 6 or more characters in length.");

		LoginRequest request = new LoginRequest();
		request.setUsername("username");
		request.setPassword("Aa!2");
		
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
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password must be no more than 20 characters in length.");
		
		LoginRequest request = new LoginRequest();
		request.setUsername("username");
		request.setPassword("Aa!200000000000000000");
		
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
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password must contain 1 or more uppercase characters.");
		
		LoginRequest request = new LoginRequest();
		request.setUsername("username");
		request.setPassword("password123!");
		
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
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password must contain 1 or more lowercase characters.");

		LoginRequest request = new LoginRequest();
		request.setUsername("username");
		request.setPassword("PASSWORD123!");
		
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
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password must contain 1 or more digit characters.");
		
		LoginRequest request = new LoginRequest();
		request.setUsername("username");
		request.setPassword("Password!");
		
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
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password must contain 1 or more special characters.");
		
		LoginRequest request = new LoginRequest();
		request.setUsername("username");
		request.setPassword("Password123");
		
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
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password contains a whitespace character.");
		
		LoginRequest request = new LoginRequest();
		request.setUsername("username");
		request.setPassword("P assword123!");
		
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
