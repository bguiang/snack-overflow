package com.bernardguiang.SnackOverflow.dto.request;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
