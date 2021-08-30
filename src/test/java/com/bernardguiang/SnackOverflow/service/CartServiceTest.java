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
		Set<Category> categories = new HashSet<>();
		Category category = new Category();
			category.setId(1L);
			category.setName("Japanese");
		categories.add(category);
		List<String> images = new ArrayList<>();
		images.add("imageURL");
		
		List<CartInfoRequestItem> cartInfoRequestItems = new ArrayList<>();
		CartInfoRequestItem item = new CartInfoRequestItem();
		item.setProductId(2L);
		item.setQuantity(5);
		cartInfoRequestItems.add(item);
		CartRequest request = new CartRequest();
		request.setItems(cartInfoRequestItems);
		
		Product p = new Product();
		p.setId(2L);
		p.setPrice(new BigDecimal(20));
		p.setCategories(categories);
		p.setDescription("description");
		p.setImages(images);
		p.setName("Product name");
		
		// When
		Optional<Product> productOptional = Optional.of(p);
		when(productRepository.findById(2L)).thenReturn(productOptional);
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
		assertEquals(5, responseItem.getQuantity());
		// ... - product
		ProductDTO expectedProductDTO = responseItem.getProduct();
		assertEquals(2L, expectedProductDTO.getId());
		assertEquals("Product name", expectedProductDTO.getName());
		assertEquals("description", expectedProductDTO.getDescription());
		assertEquals(new BigDecimal(20), expectedProductDTO.getPrice());
		assertTrue(expectedProductDTO.getCategories().equals(Arrays.asList("Japanese")));
		assertTrue(expectedProductDTO.getImages().equals(images));
	}
	
	@Test
	void itShouldThrowAnExceptionIfProductIdDoesNotExist() {
		
		// Given
		
		List<CartInfoRequestItem> cartInfoRequestItems = new ArrayList<>();
		CartInfoRequestItem item = new CartInfoRequestItem();
		item.setProductId(2L);
		item.setQuantity(5);
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
				"Could not find product " + 2L);
		}
	}
