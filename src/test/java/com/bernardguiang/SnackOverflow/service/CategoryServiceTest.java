package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bernardguiang.SnackOverflow.dto.CategoryDTO;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.repository.CategoryRepository;

class CategoryServiceTest {
	
	private CategoryService underTest;
	
	private CategoryRepository categoryRepository;

	@BeforeEach
	void setUp() throws Exception {
		categoryRepository = Mockito.mock(CategoryRepository.class);
		underTest = new CategoryService(categoryRepository);
	}

	@Test
	void itShouldSave() {
		// Given
		CategoryDTO toSave = new CategoryDTO();
		toSave.setName("Japan");

		Category saved = new Category();
		saved.setId(5L);
		saved.setName("Japan");
		// When
		when(categoryRepository
				.save(Mockito.argThat(
						(Category input) -> input.getName().equalsIgnoreCase(toSave.getName())))).thenReturn(saved);
		
		CategoryDTO savedDTO = underTest.save(toSave);
		
		// Then
		assertEquals(5L, savedDTO.getId());
		assertEquals("Japan", savedDTO.getName());
	}
	
	@Test
	void itShouldFindAll() {
		// Given
		List<Category> categories = new ArrayList<>();
		Category c1 = new Category();
		c1.setId(1L);
		c1.setName("Japan");
		Category c2 = new Category();
		c2.setId(2L);
		c2.setName("Thailand");
		Category c3 = new Category();
		c3.setId(3L);
		c3.setName("Vietnam");
		categories.add(c1);
		categories.add(c2);
		categories.add(c3);
		
		// When
		when(categoryRepository.findAll()).thenReturn(categories);
		List<CategoryDTO> categoryDTOs = underTest.findAll();
		
		// Then
		assertEquals(1L, categoryDTOs.get(0).getId());
		assertEquals("Japan", categoryDTOs.get(0).getName());
		
		assertEquals(2L, categoryDTOs.get(1).getId());
		assertEquals("Thailand", categoryDTOs.get(1).getName());
		
		assertEquals(3L, categoryDTOs.get(2).getId());
		assertEquals("Vietnam", categoryDTOs.get(2).getName());
	}

}
