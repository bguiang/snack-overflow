package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.request.CartRequest;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponseItem;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.repository.ProductRepository;

class CartServiceTest {
	
	private CartService underTest;
	
	private ProductRepository productRepository;

	@BeforeEach
	void setUp() throws Exception {
		productRepository = Mockito.mock(ProductRepository.class);
		underTest = new CartService(productRepository);
	}
	
	@Test
	void itShouldGetCartInfo() {
		
		// Given
		Long productId = 2L;
		int quantity = 5;
		BigDecimal price = new BigDecimal(20);
		Set<Category> categories = new HashSet<>();
		Category category = new Category();
			category.setId(1L);
			category.setName("Japanese");
		categories.add(category);
		String description = "description";
		List<String> images = new ArrayList<>();
		images.add("imageURL");
		String productName = "Product name";
		
		List<CartInfoRequestItem> cartInfoRequestItems = new ArrayList<>();
		CartInfoRequestItem item = new CartInfoRequestItem();
		item.setProductId(productId);
		item.setQuantity(quantity);
		cartInfoRequestItems.add(item);
		CartRequest request = new CartRequest();
		request.setItems(cartInfoRequestItems);
		
		Product p = new Product();
		p.setId(productId);
		p.setPrice(price);
		p.setCategories(categories);
		p.setDescription(description);
		p.setImages(images);
		p.setName(productName);
		
		// When
		Optional<Product> productOptional = Optional.of(p);
		when(productRepository.findById(productId)).thenReturn(productOptional);
		CartInfoResponse response = underTest.getCartInfo(request);
		
		// Then
		// ... total
		assertEquals(new BigDecimal(100), response.getTotal());
		// ... # of cart lines/items
		List<CartInfoResponseItem> responseItems = response.getItems();
		assertEquals(1, responseItems.size());
		CartInfoResponseItem responseItem = responseItems.get(0);
		// ... Cart Item
		// ... - quantity
		assertEquals(quantity, responseItem.getQuantity());
		// ... - product
		ProductDTO expectedProductDTO = responseItem.getProduct();
		assertEquals(productId, expectedProductDTO.getId());
		assertEquals(productName, expectedProductDTO.getName());
		assertEquals(description, expectedProductDTO.getDescription());
		assertEquals(price, expectedProductDTO.getPrice());
		assertTrue(expectedProductDTO.getCategories().equals(Arrays.asList("Japanese")));
		assertTrue(expectedProductDTO.getImages().equals(images));
	}
	
	@Test
	void itShouldThrowAnExceptionIfProductIdDoesNotExist() {
		
		// Given
		Long productId = 2L;
		int quantity = 5;
		
		List<CartInfoRequestItem> cartInfoRequestItems = new ArrayList<>();
		CartInfoRequestItem item = new CartInfoRequestItem();
		item.setProductId(productId);
		item.setQuantity(quantity);
		cartInfoRequestItems.add(item);
		CartRequest request = new CartRequest();
		request.setItems(cartInfoRequestItems);
		
		Product p = null;
		// When
		when(productRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(p));
		
		// Then
		// ... total
		assertThrows(
				IllegalStateException.class,
				()->underTest.getCartInfo(request), 
				"Could not find product " + productId);
		}
	}
