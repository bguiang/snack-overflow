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
import org.springframework.data.jpa.domain.JpaSort;

import com.bernardguiang.SnackOverflow.model.Product;

@DataJpaTest(properties = { "spring.jpa.properties.javax.persistence.validation.mode=none" })
class ProductRepositoryIntegrationTest {
	
	@Autowired
	private ProductRepository underTest;

	Product product1, product2, product3;
	
	@BeforeEach
	void setUp() throws Exception {
		List<String> images = new ArrayList<>();
		images.add("Image 1");
		
		Product p1 = new Product();
		p1.setName("Product 1");
		p1.setDescription("Description 1");
		p1.setPrice(new BigDecimal(2));
		p1.setCreatedDate(Instant.now());
		p1.setImages(images);
		p1.setDeleted(false);
		
		product1 = underTest.save(p1);

		Product p2 = new Product();
		p2.setName("Product 2");
		p2.setDescription("Description 2");
		p2.setPrice(new BigDecimal(5));
		p2.setCreatedDate(Instant.now());
		p2.setImages(images);
		p2.setDeleted(false);
		product2 = underTest.save(p2);

		Product p3 = new Product();
		p3.setName("Product 3");
		p3.setDescription("Description 3");
		p3.setPrice(new BigDecimal(1));
		p3.setCreatedDate(Instant.now());
		p3.setImages(images);
		p3.setDeleted(true);
		product3 = underTest.save(p3);
	}

	@Test
	void itShouldStart() {
		
	}
	
	@Test
	void itShouldFindAllBySearchTextAndIncludeOrdersAfter() {
		// Given
		Instant beforeAll = Instant.ofEpochMilli(0);
		Sort sort = JpaSort.unsafe(Sort.Direction.ASC, "(PRICE)"); // Native query requires unsafe sort
		Pageable pageable = PageRequest.of(0, 10, sort);
		
		// When
		Page<Product> result = underTest.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted("prod", beforeAll, pageable);
		
		// Then
		assertEquals(2, result.getTotalElements());
	}

}
