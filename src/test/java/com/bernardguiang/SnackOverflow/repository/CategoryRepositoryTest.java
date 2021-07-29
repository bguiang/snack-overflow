package com.bernardguiang.SnackOverflow.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bernardguiang.SnackOverflow.model.Category;

//@DataJpaTest annotation is required to test against the h2 db. Creates a clean db and drops after the test
//This property is needed so the @Column annotations in the class isn't ignored by the unit test
@DataJpaTest(properties = { "spring.jpa.properties.javax.persistence.validation.mode=none" })
//@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository underTest;
	
	private Category japan;
	private Category korea;
	private Category vietnam;
	
	@BeforeEach
	void setUp() throws Exception {
		Category category1 = new Category();
		category1.setName("Japan");
		
		Category category2 = new Category();
		category2.setName("Korea");
		
		Category category3 = new Category();
		category3.setName("Vietnam");
		
		japan = underTest.save(category1);
		korea = underTest.save(category2);
		vietnam = underTest.save(category3);
	}
	
	@Test
	void ifShouldFindAll() {
		// Given
		// When
		// Then
		Iterable<Category> categoryIterator = underTest.findAll();
		List<Category> result = new ArrayList<Category>();
		categoryIterator.forEach(result::add);
		
		assertEquals(3, result.size());
		assertTrue(result.contains(japan));
		assertTrue(result.contains(korea));
		assertTrue(result.contains(vietnam));
	}
	
	@Test
	void itshouldFindByName() {
		// Given	
		String name = "Japan";
		// When
		Optional<Category> result = underTest.findByName(name);
		
		// Then
		Category resultCategory = result.get();
		assertEquals(name, resultCategory.getName());
		assertEquals(japan.getId(), resultCategory.getId());
	}
}
