package com.bernardguiang.SnackOverflow.dto.request;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

class RegisterRequestTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	private final String defaultPasswordViolationMessage = "Invalid Password";
	
	@Test
	void registerRequestValidationShouldPass() {
		// Given
		String email = "test@email.com";
		String fullName = "full name";
		String username = "username";
		String password = "Password123!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}
	
	@Test
	void registerRequestValidationShouldFailIfEmailIsNull() {
		// Given
		String email = null;
		String fullName = "full name";
		String username = "username";
		String password = "Password123!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertEquals("Email cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void registerRequestValidationShouldFailIfEmailIsBlank() {
		// Given
		String email = "";
		String fullName = "full name";
		String username = "username";
		String password = "Password123!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertEquals("Email cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void registerRequestValidationShouldFailIfEmailIsInvalid() {
		// Given
		String email = "notanEmail";
		String fullName = "full name";
		String username = "username";
		String password = "Password123!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertEquals("Must use a valid email", violation.getMessage());
		}
	}
	
	@Test
	void registerRequestValidationShouldFailIfFullNameIsNull() {
		// Given
		String email = "test@gmail.com";
		String fullName = null;
		String username = "username";
		String password = "Password123!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertEquals("FullName cannot be blank or null", violation.getMessage());
		}
	}
	
	@Test
	void registerRequestValidationShouldFailIfFullNameIsBlank() {
		// Given
		String email = "test@gmail.com";
		String fullName = "   ";
		String username = "username";
		String password = "Password123!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertEquals("FullName cannot be blank or null", violation.getMessage());
		}
	}
	
	@Test
	void registerRequestValidationShouldFailIfUsernameIsNull() {
		// Given
		String email = "test@gmail.com";
		String fullName = "Full Name";
		String username = null;
		String password = "Password123!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertEquals("Username cannot be null", violation.getMessage());
		}
	}
	
	@Test
	void registerRequestValidationShouldFailIfUsernameLengthIsLessThanSix() {
		// Given
		String email = "test@gmail.com";
		String fullName = "Full Name";
		String username = "abc";
		String password = "Password123!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertEquals("Username must be at least 6 characters and cannot be longer than 20 characters", violation.getMessage());
		}
	}
	
	@Test
	void registerRequestValidationShouldFailIfUsernameLengthIsGreaterThanTwenty() {
		// Given
		String email = "test@gmail.com";
		String fullName = "Full Name";
		String username = "123456789012345678901";
		String password = "Password123!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertEquals("Username must be at least 6 characters and cannot be longer than 20 characters", violation.getMessage());
		}
	}
	
	@Test
	void registerRequestValidationShouldShouldFailIfUsernameContainsNonAlphabeticOrNonNumericCharacter() {
		// Given
		String email = "test@gmail.com";
		String fullName = "Full Name";
		String username = "abcdefg$";
		String password = "Password123!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertEquals("Username must consist of letters an numbers only", violation.getMessage());
		}
	}
	
	@Test
	void registerRequestValidationShouldShouldFailIfPasswordIsNull() {
		// Given
		String expectedPasswordViolationMessage = "Password is null";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String email = "test@gmail.com";
		String fullName = "Full Name";
		String username = "username";
		String password = null;
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}
	
	@Test
	void registerRequestValidationShouldShouldFailIfPasswordLengthIsLessThanSix() {
		// Given
		String expectedPasswordViolationMessage = "Password must be 6 or more characters in length.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String email = "test@gmail.com";
		String fullName = "Full Name";
		String username = "username";
		String password = "Aa!2";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}
	
	@Test
	void registerRequestValidationShouldShouldFailIfPasswordLengthIsMoreThanTwenty() {
		// Given
		String expectedPasswordViolationMessage = "Password must be no more than 20 characters in length.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String email = "test@gmail.com";
		String fullName = "Full Name";
		String username = "username";
		String password = "Aa!200000000000000000";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}

	@Test
	void registerRequestValidationShouldShouldFailIfPasswordDoesNotContainAtLeastOneUppercase() {
		// Given
		String expectedPasswordViolationMessage = "Password must contain 1 or more uppercase characters.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String email = "test@gmail.com";
		String fullName = "Full Name";
		String username = "username";
		String password = "password123!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}
	
	@Test
	void registerRequestValidationShouldShouldFailIfPasswordDoesNotContainAtLeastOneLowercase() {
		// Given
		String expectedPasswordViolationMessage = "Password must contain 1 or more lowercase characters.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String email = "test@gmail.com";
		String fullName = "Full Name";
		String username = "username";
		String password = "PASSWORD123!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}
	
	@Test
	void registerRequestValidationShouldShouldFailIfPasswordDoesNotContainAtLeastOneDigit() {
		// Given
		String expectedPasswordViolationMessage = "Password must contain 1 or more digit characters.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String email = "test@gmail.com";
		String fullName = "Full Name";
		String username = "username";
		String password = "Password!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}
	
	@Test
	void registerRequestValidationShouldShouldFailIfPasswordDoesNotContainAtLeastOneSpecialCharacter() {
		// Given
		String expectedPasswordViolationMessage = "Password must contain 1 or more special characters.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String email = "test@gmail.com";
		String fullName = "Full Name";
		String username = "username";
		String password = "Password123";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}
	
	@Test
	void registerRequestValidationShouldShouldFailIfPasswordDoesNotContainContainsWhitespace() {
		// Given
		String expectedPasswordViolationMessage = "Password contains a whitespace character.";
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add(expectedPasswordViolationMessage);
		
		String email = "test@gmail.com";
		String fullName = "Full Name";
		String username = "username";
		String password = "P assword123!";
		
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setFullName(fullName);
		request.setUsername(username);
		request.setPassword(password);
		
		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
		
		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the password constraint violations
		assertEquals(2, violations.size());
		
		for (ConstraintViolation<RegisterRequest> violation : violations) {
		    assertTrue("Violation Message Not Found: " + violation.getMessage(),
		    		expectedViolationMessages.contains(violation.getMessage()));
		}
	}
}
