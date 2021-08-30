package com.bernardguiang.SnackOverflow.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.request.ProductPage;
import com.bernardguiang.SnackOverflow.dto.response.FullProductInfo;
import com.bernardguiang.SnackOverflow.service.CategoryService;
import com.bernardguiang.SnackOverflow.service.ProductIndexingService;
import com.bernardguiang.SnackOverflow.service.ProductService;

class ProductControllerTest {

	private ProductController underTest;

	private ProductService productService;

	@BeforeEach
	void setUp() throws Exception {
		productService = Mockito.mock(ProductService.class);
		underTest = new ProductController(productService);
	}

	@Test
	void getProductsPaginated() {
		// Given
		Page<ProductDTO> productPageMock = Mockito.mock(Page.class);
		ProductPage page = new ProductPage();

		// When
		when(productService.findProductsPaginated(page)).thenReturn(productPageMock);
		Page<ProductDTO> pageResult = underTest.getProductsPaginated(page);

		// Then
		assertEquals(productPageMock, pageResult);
	}

	@Test
	void getProductById() {
		// Given
		ProductDTO productDTOMock = Mockito.mock(ProductDTO.class);

		// When
		when(productService.findById(1L)).thenReturn(productDTOMock);
		ProductDTO result = underTest.getProductById(1L);

		// Then
		assertEquals(productDTOMock, result);

	}

	@Test
	void updateProduct() {
		// Given
		ProductDTO productDTOMock = Mockito.mock(ProductDTO.class);

		// When
		when(productService.save(productDTOMock)).thenReturn(productDTOMock);
		ProductDTO result = underTest.updateProduct(productDTOMock);

		// Then
		assertEquals(productDTOMock, result);
	}

	@Test
	void createProduct() {
		// Given
		ProductDTO productDTOMock = Mockito.mock(ProductDTO.class);
		HttpServletResponse httpServletResponseMock = Mockito.mock(HttpServletResponse.class);

		// When
		when(productService.save(productDTOMock)).thenReturn(productDTOMock);
		ProductDTO result = underTest.createProduct(productDTOMock, httpServletResponseMock);

		// Then
		verify(httpServletResponseMock).setStatus(201);
		assertEquals(productDTOMock, result);
	}

	@Test
	void getProductsPaginatedForAdmin() {
		// Given
		ProductPage productPageMock = Mockito.mock(ProductPage.class);
		Page<FullProductInfo> resultPageMock = Mockito.mock(Page.class);

		// When
		when(productService.findFullProductInfosPaginated(productPageMock)).thenReturn(resultPageMock);
		Page<FullProductInfo> resultPage = underTest.getProductsPaginatedForAdmin(productPageMock);

		// Then
		assertEquals(resultPageMock, resultPage);
	}

	@Test
	void getProductByIdForAdmin() {
		// Given
		FullProductInfo fullProductInfoMock = Mockito.mock(FullProductInfo.class);

		// When
		when(productService.findFullProductInfoById(1L)).thenReturn(fullProductInfoMock);
		FullProductInfo result =  underTest.getProductByIdForAdmin(1L);

		// Then
		assertEquals(fullProductInfoMock, result);
	}

	@Test
	void deleteProductById() {
		// Given

		// When
		underTest.deleteProductById(1L);

		// Then
		verify(productService).deleteById(1L);
	}

}
