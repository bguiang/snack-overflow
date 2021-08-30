package com.bernardguiang.SnackOverflow.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartRequest;
import com.bernardguiang.SnackOverflow.dto.request.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponseItem;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.repository.ProductRepository;
import com.bernardguiang.SnackOverflow.service.ApplicationUserDetailsService;
import com.bernardguiang.SnackOverflow.service.AuthService;
import com.bernardguiang.SnackOverflow.service.CartService;
import com.bernardguiang.SnackOverflow.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(CartController.class)
class CartControllerIntegrationTest {

	@MockBean
	private CartService cartService;

	@MockBean
	private ProductRepository productRepository;
	
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
	void itShouldGetCartInfo() throws Exception {
		// Given
		Long productId = 1L;
		int quantity = 10;
		
		BigDecimal total = new BigDecimal(20);
		
		List<CartInfoRequestItem> requestItems = new ArrayList<>();
		CartInfoRequestItem cartItem = new CartInfoRequestItem();
		cartItem.setProductId(productId);
		cartItem.setQuantity(quantity);
		requestItems.add(cartItem);
		
		CartRequest request = new CartRequest();
		request.setItems(requestItems);
		
		
		CartInfoResponse cartInfoResponse = new CartInfoResponse();
		List<CartInfoResponseItem> responseItems = new ArrayList<>();
		CartInfoResponseItem responseItem = new CartInfoResponseItem();
		//responseItem.setProduct(new ProductDTO()); // TODO: needs to use a full productDTO to check serialization
		
		String productName = "Chips";
		String productDescription = "A bag of chips";
		BigDecimal productPrice = new BigDecimal(2.99);
		List<String> productImages = Arrays.asList("Image 1", "Image 2", "Image 3");
		Long categoryId = 10L;
		String categoryName = "Junk Food";
		Set<Product> categoryProducts = null;
		Category category = new Category();
		category.setId(categoryId);
		category.setName(categoryName);
		category.setProducts(categoryProducts);
		Set<Category> productCategories = new HashSet<>(
				Arrays.asList(category));
		List<OrderItem> orderedItems = null;
		Instant productCreatedDate = Instant.now();

		Product product = new Product();
		product.setId(productId);
		product.setName(productName);
		product.setDescription(productDescription);
		product.setPrice(productPrice);
		product.setCreatedDate(productCreatedDate);
		product.setImages(productImages);
		product.setCategories(productCategories);
		product.setOrderedItems(orderedItems);
		ProductDTO productDTO = new ProductDTO(product);
		responseItem.setProduct(productDTO);
		
		
		
		responseItem.setQuantity(10);
		responseItems.add(responseItem);
		cartInfoResponse.setItems(responseItems);
		cartInfoResponse.setTotal(total);
		
		// When
		when(cartService.getCartInfo(Mockito.argThat(new ArgumentMatcher<CartRequest>() {

			@Override
			public boolean matches(CartRequest argument) {
				return 
					argument.getItems().get(0).getProductId() == productId &&
					argument.getItems().get(0).getQuantity() == quantity;
			}
		}))).thenReturn(cartInfoResponse);
		
		// Then
		mockMvc.perform(post("/api/v1/cart/info").content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.total").value(total))
				.andExpect(jsonPath("$.items").isNotEmpty())
				.andExpect(jsonPath("$.items[0].quantity").value(10))
				.andExpect(jsonPath("$.items[0].product").exists())
				.andExpect(jsonPath("$.items[0].product.id").value(productId))
				.andExpect(jsonPath("$.items[0].product.name").value(productName))
				.andExpect(jsonPath("$.items[0].product.description").value(productDescription))
				.andExpect(jsonPath("$.items[0].product.price").value(productPrice))
				.andExpect(jsonPath("$.items[0].product.images").isNotEmpty())
				.andExpect(jsonPath("$.items[0].product.images[0]").value("Image 1"))
				.andExpect(jsonPath("$.items[0].product.images[1]").value("Image 2"))
				.andExpect(jsonPath("$.items[0].product.images[2]").value("Image 3"))
				.andExpect(jsonPath("$.items[0].product.categories[0]").value("Junk Food"))
				.andExpect(jsonPath("$.items[0].product.deleted").value(false))
				;
		
		verify(cartService).getCartInfo(Mockito.any());
		
	}
	
	@Test
	void getCartInfoShouldFailIfInvalidRequest() throws Exception {
		// Given
		Long productId = null; // invalid
		int quantity = 10;
		
		List<CartInfoRequestItem> requestItems = new ArrayList<>();
		CartInfoRequestItem cartItem = new CartInfoRequestItem();
		cartItem.setProductId(productId);
		cartItem.setQuantity(quantity);
		requestItems.add(cartItem);
		CartRequest request = new CartRequest();
		request.setItems(requestItems);
		
		// When	
		// Then
		mockMvc.perform(post("/api/v1/cart/info").content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
