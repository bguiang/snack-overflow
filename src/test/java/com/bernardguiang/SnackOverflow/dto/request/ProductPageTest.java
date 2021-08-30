package com.bernardguiang.SnackOverflow.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

class ProductPageTest {

	@Test
	void itShouldHaveDefaultValues() {
		// Given
		// When
		ProductPage underTest = new ProductPage();
		
		// Then
		assertEquals("", underTest.getSearch());
		assertEquals(0, underTest.getPageNumber());
		assertEquals(9, underTest.getPageSize());
		assertEquals(Sort.Direction.ASC, underTest.getSortDirection());
		assertEquals("createdDate", underTest.getSortBy());
		assertEquals("all", underTest.getItemsSold());
	}

}
