package com.bernardguiang.SnackOverflow.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
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

class FullProductInfoTest {

	@Test
	void itShouldConstructFullProductInfoFromProduct() {
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
		Category category = new Category();
		category.setId(categoryId);
		category.setName(categoryName);
		category.setProducts(categoryProducts);
		Set<Category> productCategories = new HashSet<>(Arrays.asList(category));
		Instant productCreatedDate = Instant.now();

		
		Product product = new Product();
		product.setId(productId);
		product.setName(productName);
		product.setDescription(productDescription);
		product.setPrice(productPrice);
		product.setCreatedDate(productCreatedDate);
		product.setImages(productImages);
		product.setCategories(productCategories);
		
		Order order1 = new Order();
		order1.setId(1L);
		Order order2 = new Order();
		order2.setId(2L);
		List<OrderItem> orderedItems = new ArrayList<>();
		OrderItem orderItem1 = new OrderItem();
		orderItem1.setId(1L);
		orderItem1.setQuantity(20);
		orderItem1.setOrder(order1);
		orderItem1.setProduct(product);
		OrderItem orderItem2 = new OrderItem();
		orderItem2.setId(2L);
		orderItem2.setQuantity(30);
		orderItem2.setOrder(order2);
		orderItem2.setProduct(product);
		orderedItems.add(orderItem1);
		orderedItems.add(orderItem2);

		product.setOrderedItems(orderedItems);

		// When
		FullProductInfo underTest = new FullProductInfo(product);

		// Then
		assertEquals(productId, underTest.getId());
		assertEquals(productName, underTest.getName());
		assertEquals(productPrice, underTest.getPrice());
		assertEquals(productDescription, underTest.getDescription());
		assertEquals(productCategoryStrings, underTest.getCategories());
		assertEquals(productImages, underTest.getImages());
		assertEquals(false, underTest.getDeleted());
		assertEquals(productCreatedDate, underTest.getCreatedDate());
		
		assertEquals(2, underTest.getOrderedItems().size());
		assertEquals(50, underTest.getUnitsSold());
	}

}
