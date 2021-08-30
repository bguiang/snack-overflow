package com.bernardguiang.SnackOverflow.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@email.com");
		request.setFullName("full name");
		request.setUsername("username");
		request.setPassword("Password123!");

		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

		// Then
		// ... assert that NO violations exist
		assertEquals(0, violations.size());
	}

	@Test
	void registerRequestValidationShouldFailIfEmailIsNull() {
		// Given
		RegisterRequest request = new RegisterRequest();
		request.setEmail(null);
		request.setFullName("full name");
		request.setUsername("username");
		request.setPassword("Password123!");

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
		RegisterRequest request = new RegisterRequest();
		request.setEmail("");
		request.setFullName("full name");
		request.setUsername("username");
		request.setPassword("Password123!");

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
		RegisterRequest request = new RegisterRequest();
		request.setEmail("notanEmail");
		request.setFullName("full name");
		request.setUsername("username");
		request.setPassword("Password123!");

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
		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName(null);
		request.setUsername("username");
		request.setPassword("Password123!");

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
		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName("   ");
		request.setUsername("username");
		request.setPassword("Password123!");

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
		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName("Full Name");
		request.setUsername(null);
		request.setPassword("Password123!");

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
		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName("Full Name");
		request.setUsername("abc");
		request.setPassword("Password123!");

		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RegisterRequest> violation : violations) {
			assertEquals("Username must be at least 6 characters and cannot be longer than 20 characters",
					violation.getMessage());
		}
	}

	@Test
	void registerRequestValidationShouldFailIfUsernameLengthIsGreaterThanTwenty() {
		// Given
		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName("Full Name");
		request.setUsername("123456789012345678901");
		request.setPassword("Password123!");

		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<RegisterRequest> violation : violations) {
			assertEquals("Username must be at least 6 characters and cannot be longer than 20 characters",
					violation.getMessage());
		}
	}

	@Test
	void registerRequestValidationShouldShouldFailIfUsernameContainsNonAlphabeticOrNonNumericCharacter() {
		// Given
		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName("Full Name");
		request.setUsername("abcdefg$");
		request.setPassword("Password123!");

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
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password is null");

		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName("Full Name");
		request.setUsername("username");
		request.setPassword(null);

		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the
		// password constraint violations
		assertEquals(2, violations.size());
		for (ConstraintViolation<RegisterRequest> violation : violations) {
			assertTrue(expectedViolationMessages.contains(violation.getMessage()));
		}
	}

	@Test
	void registerRequestValidationShouldShouldFailIfPasswordLengthIsLessThanSix() {
		// Given
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password must be 6 or more characters in length.");

		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName("Full Name");
		request.setUsername("username");
		request.setPassword("Aa!2");

		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the
		// password constraint violations
		assertEquals(2, violations.size());

		for (ConstraintViolation<RegisterRequest> violation : violations) {
			assertTrue(expectedViolationMessages.contains(violation.getMessage()));
		}
	}

	@Test
	void registerRequestValidationShouldShouldFailIfPasswordLengthIsMoreThanTwenty() {
		// Given
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password must be no more than 20 characters in length.");

		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName("Full Name");
		request.setUsername("username");
		request.setPassword("Aa!200000000000000000");

		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the
		// password constraint violations
		assertEquals(2, violations.size());

		for (ConstraintViolation<RegisterRequest> violation : violations) {
			assertTrue(expectedViolationMessages.contains(violation.getMessage()));
		}
	}

	@Test
	void registerRequestValidationShouldShouldFailIfPasswordDoesNotContainAtLeastOneUppercase() {
		// Given
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password must contain 1 or more uppercase characters.");

		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName("Full Name");
		request.setUsername("username");
		request.setPassword("password123!");

		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the
		// password constraint violations
		assertEquals(2, violations.size());

		for (ConstraintViolation<RegisterRequest> violation : violations) {
			assertTrue(expectedViolationMessages.contains(violation.getMessage()));
		}
	}

	@Test
	void registerRequestValidationShouldShouldFailIfPasswordDoesNotContainAtLeastOneLowercase() {
		// Given
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password must contain 1 or more lowercase characters.");

		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName("Full Name");
		request.setUsername("username");
		request.setPassword("PASSWORD123!");

		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the
		// password constraint violations
		assertEquals(2, violations.size());

		for (ConstraintViolation<RegisterRequest> violation : violations) {
			assertTrue(expectedViolationMessages.contains(violation.getMessage()));
		}
	}

	@Test
	void registerRequestValidationShouldShouldFailIfPasswordDoesNotContainAtLeastOneDigit() {
		// Given
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password must contain 1 or more digit characters.");

		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName("Full Name");
		request.setUsername("username");
		request.setPassword("Password!");

		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the
		// password constraint violations
		assertEquals(2, violations.size());

		for (ConstraintViolation<RegisterRequest> violation : violations) {
			assertTrue(expectedViolationMessages.contains(violation.getMessage()));
		}
	}

	@Test
	void registerRequestValidationShouldShouldFailIfPasswordDoesNotContainAtLeastOneSpecialCharacter() {
		// Given
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password must contain 1 or more special characters.");

		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName("Full Name");
		request.setUsername("username");
		request.setPassword("Password123");

		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the
		// password constraint violations
		assertEquals(2, violations.size());

		for (ConstraintViolation<RegisterRequest> violation : violations) {
			assertTrue(expectedViolationMessages.contains(violation.getMessage()));
		}
	}

	@Test
	void registerRequestValidationShouldShouldFailIfPasswordDoesNotContainContainsWhitespace() {
		// Given
		Set<String> expectedViolationMessages = new HashSet<>();
		expectedViolationMessages.add(defaultPasswordViolationMessage);
		expectedViolationMessages.add("Password contains a whitespace character.");

		RegisterRequest request = new RegisterRequest();
		request.setEmail("test@gmail.com");
		request.setFullName("Full Name");
		request.setUsername("username");
		request.setPassword("P assword123!");

		// When
		Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

		// Then
		// ... @Valid password returns a default "Invalid Password" violation with the
		// password constraint violations
		assertEquals(2, violations.size());

		for (ConstraintViolation<RegisterRequest> violation : violations) {
			assertTrue(expectedViolationMessages.contains(violation.getMessage()));
		}
	}
}
