package com.bernardguiang.SnackOverflow.dto;

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

import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.Product;

class CategoryDTOTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void itShouldConstructCategoryDTOFromCategory() {
		// Given
		Long id = 2L;
		String name = "Japan";
		Set<Product> products = null;
		Category category = new Category(id, name, products);

		// When
		CategoryDTO dto = new CategoryDTO(category);

		// Then
		assertEquals(id, dto.getId());
		assertEquals(name, dto.getName());
	}

	@Test
	void categoryDTOValidationShouldPass() {
		// Given
		String categoryName = "category";
		CategoryDTO category = new CategoryDTO();
		category.setName(categoryName);

		// When
		Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(category);

		// Then
		// ... assert that NO violations exist
		assertEquals(0, violations.size());
	}

	@Test
	void categoryDTOValidationShouldFailifNameIsNull() {
		// Given
		String categoryName = null;
		CategoryDTO category = new CategoryDTO();
		category.setName(categoryName);

		// When
		Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(category);

		// Then

		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<CategoryDTO> violation : violations) {
			assertEquals("Name cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void categoryDTOValidationShouldFailifNameIsempty() {
		// Given
		String categoryName = "";
		CategoryDTO category = new CategoryDTO();
		category.setName(categoryName);

		// When
		Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(category);

		// Then

		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<CategoryDTO> violation : violations) {
			assertEquals("Name cannot be null or blank", violation.getMessage());
		}
	}
}
