package com.bernardguiang.SnackOverflowWeb.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bernardguiang.SnackOverflow.controller.ProductController;
import com.bernardguiang.SnackOverflow.model.Product;

@WebMvcTest(ProductController.class)
class ProductControllerIntTest {

	
	@Test
	void testGetAllProducts() {
		// Arrange
		Product oreos= new Product();
		//oreos.setName("Oreos");
		
		// Act
		//when()
		
		
		// Assert
	}
}
