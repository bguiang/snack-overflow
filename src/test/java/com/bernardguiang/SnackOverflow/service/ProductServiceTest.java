package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.repository.CategoryRepository;
import com.bernardguiang.SnackOverflow.repository.ProductRepository;

class ProductServiceTest {

	private ProductService underTest;

	private ProductRepository productRepository;
	private CategoryRepository categoryRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		productRepository = Mockito.mock(ProductRepository.class);
		categoryRepository = Mockito.mock(CategoryRepository.class);
		
		underTest = new ProductService(productRepository, categoryRepository);
	}

	@Test
	void itShouldSave() {
		// Given
		List<String> categories = Arrays.asList("Japan", "Korea");
		String description = "product description";
		List<String> images = Arrays.asList("Image 1", "Image 2");
		String name = "Simple Product";
		BigDecimal price = new BigDecimal(20);
		
		ProductDTO input = new ProductDTO();
		input.setCategories(categories);
		input.setDescription(description);
		input.setImages(images);
		input.setName(name);
		input.setPrice(price);
		
		Long savedId = 5L;
		
		Category japan = new Category();
		japan.setId(1L);
		japan.setName("Japan");
		Category korea = new Category();
		korea.setId(2L);
		korea.setName("Korea");
		Optional<Category> japanOptional = Optional.of(japan);
		Optional<Category> koreaOptional = Optional.of(korea);
		
		// When
		when(categoryRepository.findByName("Japan")).thenReturn(japanOptional);
		when(categoryRepository.findByName("Korea")).thenReturn(koreaOptional);
		when(productRepository.save(Mockito.any())).thenAnswer(
				new Answer() {
					@Override
					public Object answer(InvocationOnMock invocation) throws Throwable {
						Object[] args = invocation.getArguments();
						Product savedProduct = (Product) args[0];
						savedProduct.setId(savedId);
						return savedProduct;
					}
		});
		
		ProductDTO savedDTO = underTest.save(input);
		
		// Then
		assertEquals(savedId, savedDTO.getId());
		assertEquals(2, savedDTO.getCategories().size());
		assertTrue(categories.containsAll(savedDTO.getCategories()));
		assertTrue(savedDTO.getCategories().containsAll(categories));
		
		assertEquals(images, savedDTO.getImages());
		assertEquals(description, savedDTO.getDescription());
		assertEquals(name, savedDTO.getName());
		assertEquals(price, savedDTO.getPrice());
	}
	
	@Test
	void itShouldThrowAnExceptionWhenCategoryDoesNotExist() {
		// Given
		List<String> categories = Arrays.asList("Japan");
		String description = "product description";
		List<String> images = Arrays.asList("Image 1", "Image 2");
		String name = "Simple Product";
		BigDecimal price = new BigDecimal(20);
		
		ProductDTO input = new ProductDTO();
		input.setCategories(categories);
		input.setDescription(description);
		input.setImages(images);
		input.setName(name);
		input.setPrice(price);

		// When
		when(categoryRepository.findByName("Japan")).thenReturn(Optional.ofNullable(null));
		
		// Then
		assertThrows(IllegalStateException.class,
			()-> underTest.save(input),
			"Could not find category Japan");
	}

	@Test
	void itShouldFindById() {
		// Given
		Long id = 7L;
		Product p = new Product();
		p.setId(id);
		Optional<Product> productOptional = Optional.of(p);
		
		// When
		when(productRepository.findById(id)).thenReturn(productOptional);
		ProductDTO productDTO = underTest.findById(id);
		// Then
		verify(productRepository).findById(id);
		assertEquals(id, productDTO.getId());
	}
	
	@Test
	void itShouldThrowExceptionWhenProductDoesNotExist() {
		// Given
		Long id = 7L;
		Product p = null;
		Optional<Product> productOptional = Optional.ofNullable(p);
		
		// When
		when(productRepository.findById(id)).thenReturn(productOptional);
		// Then
		assertThrows(IllegalStateException.class,
				()->underTest.findById(id),
				"Could not find product " + id);
	}
	
	@Test
	void itShouldFindAll() {
		
		// Given
		List<Product> products = new ArrayList<>();
		Product p1 = new Product();
		Product p2 = new Product();
		products.add(p1);
		products.add(p2);
		
		// When
		when(productRepository.findAll()).thenReturn(products);
		
		// Then
		List<ProductDTO> productDTOs = underTest.findAll();
		assertEquals(2, productDTOs.size());
	}
	
	@Test
	void itShouldFindAllByCategoryName() {
		
		// Given
		String categoryName = "Japan";
		Category category = new Category();
		category.setId(1L);
		category.setName(categoryName);
		
		List<Product> products = new ArrayList<>();
		Product p1 = new Product();
		Product p2 = new Product();
		products.add(p1);
		products.add(p2);
		
		category.setProducts(new HashSet<>(products));
		
		Optional<Category> categoryOptional = Optional.of(category);
		
		// When
		when(categoryRepository.findByName(categoryName)).thenReturn(categoryOptional);
		
		// Then
		List<ProductDTO> productDTOs = underTest.findAllByCategoryName(categoryName);
		assertEquals(2, productDTOs.size());
	}
	
	@Test
	void FindAllByCategoryShouldThrowAnExceptionWhenCategoryDoesNotExist() {
		// Given
		String categoryName = "Japan";

		// When
		when(categoryRepository.findByName(categoryName)).thenReturn(Optional.ofNullable(null));
		
		// Then
		assertThrows(IllegalStateException.class,
			()-> underTest.findAllByCategoryName(categoryName),
			"Could not find category Japan");
	}
}
