package com.bernardguiang.SnackOverflow.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bernardguiang.SnackOverflow.service.CategoryService;
import com.bernardguiang.SnackOverflow.service.ProductIndexingService;
import com.bernardguiang.SnackOverflow.service.ProductService;

class ProductControllerTest {

	private ProductController underTest;
	
	private ProductService productService;
//	private CategoryService categoryService;

	@BeforeEach
	void setUp() throws Exception {
		productService = Mockito.mock(ProductService.class);
//		categoryService = Mockito.mock(CategoryService.class);
		
		//underTest = new ProductController(productService, categoryService);
		underTest = new ProductController(productService);
	}

	@Test
	void test() {
//		fail("Not yet implemented");
	}

}
