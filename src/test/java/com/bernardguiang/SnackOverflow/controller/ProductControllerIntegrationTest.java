package com.bernardguiang.SnackOverflow.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.request.ProductPage;
import com.bernardguiang.SnackOverflow.repository.CategoryRepository;
import com.bernardguiang.SnackOverflow.repository.ProductRepository;
import com.bernardguiang.SnackOverflow.service.ApplicationUserDetailsService;
import com.bernardguiang.SnackOverflow.service.AuthService;
import com.bernardguiang.SnackOverflow.service.JwtService;
import com.bernardguiang.SnackOverflow.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
class ProductControllerIntegrationTest {

	// ProductController Dependencies
	@MockBean
	private ProductService productService;
	@MockBean
	private ProductRepository productRepository;
	@MockBean
	private CategoryRepository categoryRepository;
	
	// JWT Auth Filter Dependencies
	@MockBean
	private AuthService authService;
	@MockBean
	private PasswordEncoder passwordEncoder;
	@MockBean
	private ApplicationUserDetailsService applicationUserDetailsService;
	@MockBean
	private JwtService jwtService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void itShouldGetProductsPaginated() throws Exception {
		// Given
		ProductPage page = new ProductPage();
		page.setPageNumber(0);
		page.setPageSize(20);
		page.setSearch("");
		page.setSortBy("createdDate");
		page.setSortDirection(Sort.Direction.ASC);
		
		List<String> categories = Arrays.asList("Japan", "Korea");
		String description = "product description";
		List<String> images = Arrays.asList("Image 1", "Image 2");
		String name = "Simple Product";
		BigDecimal price = new BigDecimal(20);
		
		ProductDTO product = new ProductDTO();
		product.setId(2L);
		product.setCategories(categories);
		product.setDescription(description);
		product.setImages(images);
		product.setName(name);
		product.setPrice(price);
		
		List<ProductDTO> resultsList = new ArrayList<>();
		resultsList.add(product);
		
		Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
		Pageable pageable = PageRequest.of(
				page.getPageNumber(), 
				page.getPageSize(), 
				sort);
		Page<ProductDTO> resultsPage = new PageImpl<>(resultsList, pageable, resultsList.size());

		
		// When
		when(productService.findProductsPaginated(Mockito.any())).thenReturn(resultsPage);
		
		// Then
		mockMvc.perform(
			get("/api/v1/products")
				.content(asJsonString(page))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.pageable.pageSize").value(page.getPageSize()))
				.andExpect(jsonPath("$.pageable.pageNumber").value(page.getPageNumber()))
				.andExpect(jsonPath("$.size").value(page.getPageSize()))
				.andExpect(jsonPath("$.number").value(page.getPageNumber()))
				.andExpect(jsonPath("$.content[0].id").value(2L))
				.andExpect(jsonPath("$.content[0].name").value(name))
				.andExpect(jsonPath("$.content[0].description").value(description))
				.andExpect(jsonPath("$.content[0].price").value(price))
				.andExpect(jsonPath("$.content[0].images").isNotEmpty())
				.andExpect(jsonPath("$.content[0].categories").isNotEmpty()
		);
	}
	
	@Test
	void itShouldGetProduct() throws Exception {
		// Given
		List<String> categories = Arrays.asList("Japan", "Korea");
		String description = "product description";
		List<String> images = Arrays.asList("Image 1", "Image 2");
		String name = "Simple Product";
		BigDecimal price = new BigDecimal(20);
		
		Long productId = 2L;
		ProductDTO product = new ProductDTO();
		product.setId(productId);
		product.setCategories(categories);
		product.setDescription(description);
		product.setImages(images);
		product.setName(name);
		product.setPrice(price);

		// When
		when(productService.findById(productId)).thenReturn(product);
		
		// Then
		mockMvc.perform(
			get("/api/v1/products/" + productId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(productId))
				.andExpect(jsonPath("$.name").value(name))
				.andExpect(jsonPath("$.description").value(description))
				.andExpect(jsonPath("$.price").value(price))
				.andExpect(jsonPath("$.images").isNotEmpty())
				.andExpect(jsonPath("$.categories").isNotEmpty()
		);
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
