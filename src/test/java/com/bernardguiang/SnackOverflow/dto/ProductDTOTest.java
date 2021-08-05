package com.bernardguiang.SnackOverflow.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

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

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	private final Long validId = 1L;
	private final List<String> validCategories = new ArrayList<>();
	
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
		Instant productCreatedDate = Instant.now();

		Product product = new Product(productName, productDescription, productPrice, productCreatedDate, productImages,
				productCategories, orderedItems);

		// When
		ProductDTO dto = new ProductDTO(product);

		// Then
		//assertEquals(productCreatedDate, dto.getCreatedDate()); //TODO: add asserts for createdDate on ProductDTO
		assertEquals(productName, dto.getName());
		assertEquals(productPrice, dto.getPrice());
		assertEquals(productDescription, dto.getDescription());
		assertEquals(productCategoryStrings, dto.getCategories());
		assertEquals(productImages, dto.getImages());
	}

	@Test
	void productDTOValidationShouldPass() {
		// Given
		String name ="name";
		String description = "description";
		BigDecimal price = new BigDecimal(2);
		List<String> images = Arrays.asList("image1");
		
		ProductDTO product = new ProductDTO();
		product.setId(validId);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setImages(images);
		product.setCategories(validCategories);
		
		// When
		Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
		
		// Then
		//... assert that NO violations exist
		assertEquals(0, violations.size());
	}

	@Test
	void productDTOValidationShouldFailIfNameIsNull() {
		// Given
		String name = null;
		String description = "description";
		BigDecimal price = new BigDecimal(2);
		List<String> images = Arrays.asList("image1");

		ProductDTO product = new ProductDTO();
		product.setId(validId);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setImages(images);
		product.setCategories(validCategories);

		// When
		Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);

		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ProductDTO> violation : violations) {
			assertEquals("Name cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void productDTOValidationShouldFailIfNameIsBlank() {
		// Given
		String name = "";
		String description = "description";
		BigDecimal price = new BigDecimal(2);
		List<String> images = Arrays.asList("image1");

		ProductDTO product = new ProductDTO();
		product.setId(validId);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setImages(images);
		product.setCategories(validCategories);

		// When
		Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);

		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ProductDTO> violation : violations) {
			assertEquals("Name cannot be null or blank", violation.getMessage());
		}
	}

	@Test
	void productDTOValidationShouldFailIfDescriptionIsNull() {
		// Given
		String name = "name";
		String description = null;
		BigDecimal price = new BigDecimal(2);
		List<String> images = Arrays.asList("image1");

		ProductDTO product = new ProductDTO();
		product.setId(validId);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setImages(images);
		product.setCategories(validCategories);

		// When
		Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);

		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ProductDTO> violation : violations) {
			assertEquals("Description cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void productDTOValidationShouldFailIfDescriptionIsBlank() {
		// Given
		String name = "name";
		String description = "";
		BigDecimal price = new BigDecimal(2);
		List<String> images = Arrays.asList("image1");

		ProductDTO product = new ProductDTO();
		product.setId(validId);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setImages(images);
		product.setCategories(validCategories);

		// When
		Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);

		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ProductDTO> violation : violations) {
			assertEquals("Description cannot be null or blank", violation.getMessage());
		}
	}
	
	@Test
	void productDTOValidationShouldFailIfPriceIsNull() {
		// Given
		String name = "name";
		String description = "descripotion";
		BigDecimal price = null;
		List<String> images = Arrays.asList("image1");

		ProductDTO product = new ProductDTO();
		product.setId(validId);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setImages(images);
		product.setCategories(validCategories);

		// When
		Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);

		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ProductDTO> violation : violations) {
			assertEquals("Price cannot be null", violation.getMessage());
		}
	}
	
	@Test
	void productDTOValidationShouldFailIfImagesIsEmpty() {
		// Given
		String name = "name";
		String description = "description";
		BigDecimal price = new BigDecimal(2);
		List<String> images = new ArrayList<>();

		ProductDTO product = new ProductDTO();
		product.setId(validId);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setImages(images);
		product.setCategories(validCategories);

		// When
		Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);

		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ProductDTO> violation : violations) {
			assertEquals("Images cannot be empty", violation.getMessage());
		}
	}
	
	@Test
	void productDTOValidationShouldFailIfImageIsBlank() {
		// Given
		String name = "name";
		String description = "description";
		BigDecimal price = new BigDecimal(2);
		List<String> images = Arrays.asList("");

		ProductDTO product = new ProductDTO();
		product.setId(validId);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setImages(images);
		product.setCategories(validCategories);

		// When
		Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);

		// Then
		assertEquals(1, violations.size());
		for (ConstraintViolation<ProductDTO> violation : violations) {
			assertEquals("Image url cannot be blank or null", violation.getMessage());
		}
	}
}
