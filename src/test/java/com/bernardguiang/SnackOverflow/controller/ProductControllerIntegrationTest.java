package com.bernardguiang.SnackOverflow.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
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
import com.bernardguiang.SnackOverflow.dto.request.OrderPage;
import com.bernardguiang.SnackOverflow.dto.request.ProductPage;
import com.bernardguiang.SnackOverflow.dto.response.FullProductInfo;
import com.bernardguiang.SnackOverflow.dto.response.OrderDTO;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.repository.CategoryRepository;
import com.bernardguiang.SnackOverflow.repository.ProductRepository;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;
import com.bernardguiang.SnackOverflow.service.ApplicationUserDetailsService;
import com.bernardguiang.SnackOverflow.service.AuthService;
import com.bernardguiang.SnackOverflow.service.JwtService;
import com.bernardguiang.SnackOverflow.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;

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
	
	private static List<Map<String, String>> adminRoleAuthorities;
	
	private final Long adminId = 1L;
	private final String adminUsername = "adminUsername";
	private final String adminEmail = "admin@email.com";
	private final String adminRole = ApplicationUserRole.ADMIN.name();
	private final String adminAuthToken = "adminAuthToken";

	@BeforeAll
	static void setupBeforeAll() {		
		adminRoleAuthorities = new ArrayList<>();
		
		Map<String, String> adminAuth1 = new HashMap<>();
		adminAuth1.put("authority", "ROLE_ADMIN");
		adminRoleAuthorities.add(adminAuth1);
		Map<String, String> adminAuth2 = new HashMap<>();
		adminAuth2.put("authority", "order:write");
		adminRoleAuthorities.add(adminAuth2);
		Map<String, String> adminAuth3 = new HashMap<>();
		adminAuth3.put("authority", "category:read");
		adminRoleAuthorities.add(adminAuth3);
		Map<String, String> adminAuth4 = new HashMap<>();
		adminAuth4.put("authority", "product:read");
		adminRoleAuthorities.add(adminAuth4);
		Map<String, String> adminAuth5 = new HashMap<>();
		adminAuth5.put("authority", "order:read");
		adminRoleAuthorities.add(adminAuth5);
		
		Map<String, String> adminAuth6 = new HashMap<>();
		adminAuth6.put("authority", "product:write");
		adminRoleAuthorities.add(adminAuth6);
		Map<String, String> adminAuth7 = new HashMap<>();
		adminAuth7.put("authority", "category:write");
		adminRoleAuthorities.add(adminAuth7);
	}
	
	void setAdminAuthorization() {
		// ... Handle token authorization via filter
		Claims tokenPayload = Mockito.mock(Claims.class);
		when(tokenPayload.getSubject()).thenReturn(adminUsername);
		when(tokenPayload.get("authorities")).thenReturn(adminRoleAuthorities);
		when(jwtService.getTokenPayload(adminAuthToken)).thenReturn(tokenPayload);
	}

	@Test
	void getProductsPaginated() throws Exception {
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
	void getProduct() throws Exception {
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
	
	@Test
	void updateProduct() throws Exception {
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
		setAdminAuthorization();
		when(productService.save(Mockito.argThat(new ArgumentMatcher<ProductDTO>() {

			@Override
			public boolean matches(ProductDTO argument) {
				return argument.getId() == 2L;
			}
		}))).thenReturn(product);
		
		// Then
		mockMvc.perform(
			put("/api/v1/admin/products").content(asJsonString(product))
				.header("Authorization", "Bearer " + adminAuthToken)
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

	@Test
	void createProduct() throws Exception {
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
		setAdminAuthorization();
		when(productService.save(Mockito.argThat(new ArgumentMatcher<ProductDTO>() {

			@Override
			public boolean matches(ProductDTO argument) {
				return argument.getName().equals(name);
			}
		}))).thenReturn(product);
		
		// Then
		mockMvc.perform(
			post("/api/v1/admin/products").content(asJsonString(product))
				.header("Authorization", "Bearer " + adminAuthToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(productId))
				.andExpect(jsonPath("$.name").value(name))
				.andExpect(jsonPath("$.description").value(description))
				.andExpect(jsonPath("$.price").value(price))
				.andExpect(jsonPath("$.images").isNotEmpty())
				.andExpect(jsonPath("$.categories").isNotEmpty()
		);
	}
	
	@Test
	void getProductsPaginatedForAdmin() throws Exception {
		// Given
		ProductPage page = new ProductPage();
		page.setSearch("chip");
		
		Product product = new Product();
		product.setId(3L);
		product.setName("Chips");
		product.setDescription("A Bag of Chips");
		product.setPrice(new BigDecimal(3));
		product.setCreatedDate(Instant.now());
		List<String> productImages = Arrays.asList("Image 1", "Image 2", "Image 3");
		product.setImages(productImages);
		Category category = new Category();
		category.setId(1L);
		category.setName("Junk Food");
		Set<Category> productCategories = new HashSet<>(
				Arrays.asList(category));
		product.setCategories(productCategories);
		List<OrderItem> orderedItems = new ArrayList<>();
		product.setOrderedItems(orderedItems);

		List<FullProductInfo> content = Arrays.asList(new FullProductInfo(product));
		Page<FullProductInfo> pageResult = new PageImpl<FullProductInfo>(content);
		
		// When
		setAdminAuthorization();
		when(productService.findFullProductInfosPaginated(Mockito.argThat(new ArgumentMatcher<ProductPage>() {

			@Override
			public boolean matches(ProductPage argument) {
				return argument.getSearch().equals("chip");
			}
		}))).thenReturn(pageResult);
		
		// Then
		mockMvc.perform(
			get("/api/v1/admin/products")
				.param("search", "chip")
				.header("Authorization", "Bearer " + adminAuthToken)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$.content").isNotEmpty())
				.andExpect(jsonPath("$.content[0].id").value(3L))
				.andExpect(jsonPath("$.content[0].name").value("Chips"))
				.andExpect(jsonPath("$.content[0].description").value("A Bag of Chips"));
	}
	
	@Test
	void getProductByIdForAdmin() throws Exception {
		// Given		
		Product product = new Product();
		product.setId(3L);
		product.setName("Chips");
		product.setDescription("A Bag of Chips");
		product.setPrice(new BigDecimal(3));
		product.setCreatedDate(Instant.now());
		List<String> productImages = Arrays.asList("Image 1", "Image 2", "Image 3");
		product.setImages(productImages);
		Category category = new Category();
		category.setId(1L);
		category.setName("Junk Food");
		Set<Category> productCategories = new HashSet<>(
				Arrays.asList(category));
		product.setCategories(productCategories);
		List<OrderItem> orderedItems = new ArrayList<>();
		product.setOrderedItems(orderedItems);
		FullProductInfo productInfo = new FullProductInfo(product);
		// When
		setAdminAuthorization();
		when(productService.findFullProductInfoById(3L)).thenReturn(productInfo);
		
		// Then
		mockMvc.perform(
			get("/api/v1/admin/products/3")
				.header("Authorization", "Bearer " + adminAuthToken)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$.id").value(3L))
				.andExpect(jsonPath("$.name").value("Chips"))
				.andExpect(jsonPath("$.description").value("A Bag of Chips"));
	}
	
	@Test
	void deleteProductById() throws Exception {
		// Given	
		// When
		setAdminAuthorization();
		// Then
		mockMvc.perform(
			delete("/api/v1/admin/products/3")
				.header("Authorization", "Bearer " + adminAuthToken)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		verify(productService).deleteById(3L);
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
