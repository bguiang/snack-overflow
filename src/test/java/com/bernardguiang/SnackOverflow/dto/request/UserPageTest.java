package com.bernardguiang.SnackOverflow.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

class UserPageTest {

	@Test
	void itShouldHaveDefaultValues() {
		// Given
		// When
		UserPage underTest = new UserPage();
		
		// Then
		assertEquals("", underTest.getSearch());
		assertEquals(0, underTest.getPageNumber());
		assertEquals(9, underTest.getPageSize());
		assertEquals(Sort.Direction.DESC, underTest.getSortDirection());
		assertEquals("joinDate", underTest.getSortBy());
	}

}
