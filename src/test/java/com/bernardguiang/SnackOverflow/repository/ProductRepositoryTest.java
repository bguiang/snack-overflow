package com.bernardguiang.SnackOverflow.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.ProductPage;

@DataJpaTest(properties = { "spring.jpa.properties.javax.persistence.validation.mode=none" })
class ProductRepositoryTest {
	
	@Autowired
	private ProductRepository underTest;

	Product product1, product2, product3;
	
	@BeforeEach
	void setUp() throws Exception {
		List<String> images = new ArrayList<>();
		images.add("Image 1");
		
		product1 = underTest.save(new Product(
				"Product 1", 
				"Description 1", 
				new BigDecimal(2), 
				Instant.now(), 
				images,
				null, 
				null));

		product2 = underTest.save(new Product(
				"Product 2", 
				"Description 2", 
				new BigDecimal(2), 
				Instant.now(), 
				images,
				null, 
				null));

		product3 = underTest.save(new Product(
				"Product 3", 
				"Description 3", 
				new BigDecimal(2), 
				Instant.now(), 
				images,
				null, 
				null));
	}

	@Test
	void itShouldFindAllByNameContainingIgnoreCase() {
		// Given
		String name = "prod";
		ProductPage productPage=  new ProductPage();

		Sort sort = Sort.by(productPage.getSortDirection(), productPage.getSortBy());
		Pageable pageable = PageRequest.of(
				productPage.getPageNumber(), 
				productPage.getPageSize(), 
				sort);
		
		// When
		Page<Product> result = underTest.findAllByNameContainingIgnoreCase(name, pageable);
		
		// Then
		assertEquals(3, result.getTotalElements());
	}

}
