package com.bernardguiang.SnackOverflow.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Product;

class CategoryDTOTest {

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

}
