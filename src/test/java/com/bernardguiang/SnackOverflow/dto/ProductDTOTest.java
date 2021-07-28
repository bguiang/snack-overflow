package com.bernardguiang.SnackOverflow.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.Product;

class ProductDTOTest {

	@Test
	void itShouldConstructProductDTOFromProduct() {
		// Given
		Long productId = 3L;
		String productName = "Chips";
		String productDescription = "A bag of chips";
		BigDecimal productPrice = new BigDecimal(2.99);
		List<String> productImages = Arrays.asList("Image 1", "Image 2", "Image 3");
		Long categoryId = 10L;
		String categoryName = "Junk Food";
		Set<Product> categoryProducts = null;
		List<String> productCategoryStrings = Arrays.asList(categoryName);
		Set<Category> productCategories = new HashSet<>(
				Arrays.asList(new Category(categoryId, categoryName, categoryProducts)));
		List<OrderItem> orderedItems = null;

		Product product = new Product(productId, productName, productDescription, productPrice, productImages,
				productCategories, orderedItems);

		// When
		ProductDTO dto = new ProductDTO(product);

		// Then
		assertEquals(productId, dto.getId());
		assertEquals(productName, dto.getName());
		assertEquals(productPrice, dto.getPrice());
		assertEquals(productDescription, dto.getDescription());
		assertEquals(productCategoryStrings, dto.getCategories());
		assertEquals(productImages, dto.getImages());
	}
}
