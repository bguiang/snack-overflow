package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.request.ProductPage;
import com.bernardguiang.SnackOverflow.dto.response.FullProductInfo;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.repository.CategoryRepository;
import com.bernardguiang.SnackOverflow.repository.OrderItemRepository;
import com.bernardguiang.SnackOverflow.repository.ProductRepository;

class ProductServiceTest {

	private ProductService underTest;

	private ProductRepository productRepository;
	private CategoryRepository categoryRepository;
	private OrderItemRepository orderItemRepository;

	private Product product1;
	private Category category1;
	private Instant createdDate1;
	private Page<Product> productPage;

	@BeforeEach
	void setUp() throws Exception {
		productRepository = Mockito.mock(ProductRepository.class);
		categoryRepository = Mockito.mock(CategoryRepository.class);
		orderItemRepository = Mockito.mock(OrderItemRepository.class);

		underTest = new ProductService(productRepository, categoryRepository, orderItemRepository);

		category1 = new Category();
		category1.setId(10L);
		category1.setName("Junk Food");
		category1.setProducts(null);

		createdDate1 = Instant.now();
		
		product1 = new Product();
		product1.setId(3L);
		product1.setName("Chips");
		product1.setDescription("A bag of chips");
		product1.setPrice(new BigDecimal(2.99));
		product1.setCreatedDate(createdDate1);
		product1.setImages(Arrays.asList("Image 1", "Image 2", "Image 3"));
		product1.setCategories(new HashSet<>(Arrays.asList(category1)));
		
		Order order1 = new Order();
		order1.setId(1L);
		Order order2 = new Order();
		order2.setId(2L);
		List<OrderItem> orderedItems = new ArrayList<>();
		OrderItem orderItem1 = new OrderItem();
		orderItem1.setId(1L);
		orderItem1.setQuantity(20);
		orderItem1.setOrder(order1);
		orderItem1.setProduct(product1);
		OrderItem orderItem2 = new OrderItem();
		orderItem2.setId(2L);
		orderItem2.setQuantity(30);
		orderItem2.setOrder(order2);
		orderItem2.setProduct(product1);
		orderedItems.add(orderItem1);
		orderedItems.add(orderItem2);
		product1.setOrderedItems(orderedItems);

		List<Product> products = Arrays.asList(product1);
		Sort sort = Sort.by(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(0, 9, sort);
		productPage = new PageImpl<Product>(products, pageable, products.size());
	}

	@Test
	void saveNewProduct() {
		// Given
		Optional<Category> categoryOptional = Optional.of(category1);
		product1.setId(null);
		product1.setCreatedDate(null);
		
		// When
		when(categoryRepository.findByName("Junk Food")).thenReturn(categoryOptional);
		when(productRepository.save(Mockito.any())).thenAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				Product savedProduct = (Product) args[0];
				savedProduct.setId(25L);
				savedProduct.setCreatedDate(Instant.now());
				return savedProduct;
			}
		});

		ProductDTO savedDTO = underTest.save(new ProductDTO(product1));

		// Then
		assertEquals(25L, savedDTO.getId());
		assertEquals("Chips", savedDTO.getName());
	}

	@Test
	void saveExistingProduct() {
		// Given
		Optional<Category> categoryOptional = Optional.of(category1);

		// When
		when(productRepository.findById(3L)).thenReturn(Optional.ofNullable(product1));
		when(categoryRepository.findByName("Junk Food")).thenReturn(categoryOptional);
		when(productRepository.save(Mockito.any())).thenAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				Product savedProduct = (Product) args[0];
				return savedProduct;
			}
		});

		ProductDTO savedDTO = underTest.save(new ProductDTO(product1));

		// Then
		assertEquals(product1.getId(), savedDTO.getId());
		assertEquals("Chips", savedDTO.getName());
	}

	@Test
	void updateProductThrowExceptionWhenProductDoesNotExist() {
		// Given
		Optional<Product> productOptional = Optional.ofNullable(null);

		// When
		when(productRepository.findById(3L)).thenReturn(productOptional);
		// Then
		assertThrows(IllegalStateException.class, () -> underTest.save(new ProductDTO(product1)), "Could not find product " + 3L);
	}

	@Test
	void saveShouldThrowAnExceptionWhenCategoryDoesNotExist() {
		// Given
		Optional<Category> categoryOptional = Optional.ofNullable(null);

		// When
		when(categoryRepository.findByName("Junk Food")).thenReturn(categoryOptional);

		// Then
		assertThrows(IllegalStateException.class, () -> underTest.save(new ProductDTO(product1)), "Could not find category Junk Food");
	}

	@Test
	void itShouldFindById() {
		// Given
		Optional<Product> productOptional = Optional.of(product1);

		// When
		when(productRepository.findById(3L)).thenReturn(productOptional);
		ProductDTO productDTO = underTest.findById(3L);
		
		// Then
		assertEquals(3L, productDTO.getId());
	}

	@Test
	void findByIdShouldThrowExceptionWhenProductDoesNotExist() {
		// Given
		Product p = null;
		Optional<Product> productOptional = Optional.ofNullable(p);

		// When
		when(productRepository.findById(3L)).thenReturn(productOptional);
		// Then
		assertThrows(IllegalStateException.class, () -> underTest.findById(3L), "Could not find product " + 3L);
	}

	@Test
	void delete() {
		// Given
		ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

		Optional<Product> productOptional = Optional.ofNullable(product1);

		// When
		when(productRepository.findById(3L)).thenReturn(productOptional);
		underTest.deleteById(3L);
		
		// Then
		verify(productRepository).save(productCaptor.capture());
		assertEquals(true, productCaptor.getValue().isDeleted());
	}

	@Test
	void deleteShouldThrowAnExceptionWhenProductDoesNotExist() {
		// Given
		Optional<Product> productOptional = Optional.ofNullable(null);

		// When
		when(productRepository.findById(1L)).thenReturn(productOptional);
		
		// Then
		assertThrows(IllegalStateException.class, () -> underTest.deleteById(1L), "Could not find product " + 1L);
	}

	@Test
	void findProductsPaginatedByUnitsSold() {
		// Given
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

		ProductPage page = new ProductPage();
		page.setSortBy("unitsSold");

		// When
		when(productRepository.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(Mockito.any(), Mockito.any(),
				pageableCaptor.capture())).thenReturn(productPage);
		Page<ProductDTO> result = underTest.findProductsPaginated(page);

		// Then
		assertEquals(1, result.getContent().size());
		Sort s = pageableCaptor.getValue().getSort();
		assertNotNull(s.getOrderFor("(UNITS_SOLD)"));
		assertNotNull(s.getOrderFor("(ID)"));
	}

	@Test
	void findProductsPaginatedById() {
		// Given
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

		ProductPage page = new ProductPage();
		page.setSortBy("id");

		// When
		when(productRepository.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(Mockito.any(), Mockito.any(),
				pageableCaptor.capture())).thenReturn(productPage);
		Page<ProductDTO> result = underTest.findProductsPaginated(page);

		// Then
		assertEquals(1, result.getContent().size());
		Sort s = pageableCaptor.getValue().getSort();
		assertNotNull(s.getOrderFor("(ID)"));
	}

	@Test
	void findProductsPaginatedByName() {
		// Given
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

		ProductPage page = new ProductPage();
		page.setSortBy("name");

		// When
		when(productRepository.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(Mockito.any(), Mockito.any(),
				pageableCaptor.capture())).thenReturn(productPage);
		Page<ProductDTO> result = underTest.findProductsPaginated(page);

		// Then
		assertEquals(1, result.getContent().size());
		Sort s = pageableCaptor.getValue().getSort();
		assertNotNull(s.getOrderFor("(NAME)"));
	}

	@Test
	void findProductsPaginatedByPrice() {
		// Given
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

		ProductPage page = new ProductPage();
		page.setSortBy("price");

		// When
		when(productRepository.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(Mockito.any(), Mockito.any(),
				pageableCaptor.capture())).thenReturn(productPage);
		Page<ProductDTO> result = underTest.findProductsPaginated(page);

		// Then
		assertEquals(1, result.getContent().size());
		Sort s = pageableCaptor.getValue().getSort();
		assertNotNull(s.getOrderFor("(PRICE)"));
	}

	@Test
	void findProductsPaginatedByCreatedDate() {
		// Given
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

		ProductPage page = new ProductPage();
		page.setSortBy("createdDate");

		// When
		when(productRepository.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(Mockito.any(), Mockito.any(),
				pageableCaptor.capture())).thenReturn(productPage);
		Page<ProductDTO> result = underTest.findProductsPaginated(page);

		// Then
		assertEquals(1, result.getContent().size());
		Sort s = pageableCaptor.getValue().getSort();
		assertNotNull(s.getOrderFor("(CREATED_DATE)"));
	}

	@Test
	void findFullProductInfosPaginatedThisMonth() {
		// Given
		LocalDate today = LocalDate.of(2000, 5, 15);

		ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);
		ProductPage page = new ProductPage();
		page.setItemsSold("month");

		MockedStatic<LocalDate> localDateMockedStatic = Mockito.mockStatic(LocalDate.class);

		// When
		localDateMockedStatic.when(() -> LocalDate.now()).thenAnswer(new Answer<LocalDate>() {
			public LocalDate answer(InvocationOnMock invocation) throws Throwable {
				localDateMockedStatic.close();
				return today;
			}
		});
		when(productRepository.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(Mockito.any(),
				instantCaptor.capture(), Mockito.any())).thenReturn(productPage);

		Page<FullProductInfo> result = underTest.findFullProductInfosPaginated(page);
		// Then
		Date convertedDate = Date.from(instantCaptor.getValue());
		assertEquals(1, convertedDate.getDate());
		assertEquals(4, convertedDate.getMonth());
		assertEquals(100, convertedDate.getYear()); // getYear returns calendar year - 1900
		assertEquals(1, result.getContent().size());
		assertEquals("Chips", result.getContent().get(0).getName());
	}

	@Test
	void findFullProductInfosPaginatedThisYear() {
		// Given
		LocalDate today = LocalDate.of(2000, 5, 15);

		ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);
		ProductPage page = new ProductPage();
		page.setItemsSold("year");

		MockedStatic<LocalDate> localDateMockedStatic = Mockito.mockStatic(LocalDate.class);

		// When
		localDateMockedStatic.when(() -> LocalDate.now()).thenAnswer(new Answer<LocalDate>() {
			public LocalDate answer(InvocationOnMock invocation) throws Throwable {
				localDateMockedStatic.close();
				return today;
			}
		});
		when(productRepository.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(Mockito.any(),
				instantCaptor.capture(), Mockito.any())).thenReturn(productPage);

		Page<FullProductInfo> result = underTest.findFullProductInfosPaginated(page);
		// Then
		Date convertedDate = Date.from(instantCaptor.getValue());
		assertEquals(1, convertedDate.getDate());
		assertEquals(0, convertedDate.getMonth());
		assertEquals(100, convertedDate.getYear()); // getYear returns calendar year - 1900
		assertEquals(1, result.getContent().size());
		assertEquals("Chips", result.getContent().get(0).getName());
	}

	@Test
	void findFullProductInfosPaginatedAllTime() {
		// Given
		ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);

		ProductPage page = new ProductPage();
		page.setItemsSold("all");

		// When
		when(productRepository.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(Mockito.any(),
				instantCaptor.capture(), Mockito.any())).thenReturn(productPage);
		Page<FullProductInfo> result = underTest.findFullProductInfosPaginated(page);
		// Then
		assertEquals(Instant.ofEpochMilli(0), instantCaptor.getValue());
		assertEquals(1, result.getContent().size());
		assertEquals("Chips", result.getContent().get(0).getName());
	}

	@Test
	void findFullProductInfosPaginatedShouldSortbyUnitsSold() {
		// Given
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		
		ProductPage page = new ProductPage();
		page.setSortBy("unitsSold");
		
		// When
		when(productRepository.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(
				Mockito.any(), Mockito.any(), pageableCaptor.capture()))
		.thenReturn(productPage);
		Page<FullProductInfo> result = underTest.findFullProductInfosPaginated(page);
		// Then
		Sort s = pageableCaptor.getValue().getSort();
		assertNotNull(s.getOrderFor("(UNITS_SOLD)"));
		assertNotNull(s.getOrderFor("(ID)"));
		assertEquals(1, result.getContent().size());
		assertEquals("Chips", result.getContent().get(0).getName());
	}
	
	@Test
	void findFullProductInfosPaginatedShouldSortbyId() {
		// Given
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		
		ProductPage page = new ProductPage();
		page.setSortBy("id");
		
		// When
		when(productRepository.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(
				Mockito.any(), Mockito.any(), pageableCaptor.capture()))
		.thenReturn(productPage);
		Page<FullProductInfo> result = underTest.findFullProductInfosPaginated(page);
		// Then
		Sort s = pageableCaptor.getValue().getSort();
		assertNotNull(s.getOrderFor("(ID)"));
		assertEquals(1, result.getContent().size());
		assertEquals("Chips", result.getContent().get(0).getName());
	}
	
	@Test
	void findFullProductInfosPaginatedShouldSortbyName() {
		// Given
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		
		ProductPage page = new ProductPage();
		page.setSortBy("name");
		
		// When
		when(productRepository.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(
				Mockito.any(), Mockito.any(), pageableCaptor.capture()))
		.thenReturn(productPage);
		Page<FullProductInfo> result = underTest.findFullProductInfosPaginated(page);
		// Then
		Sort s = pageableCaptor.getValue().getSort();
		assertNotNull(s.getOrderFor("(NAME)"));
		assertEquals(1, result.getContent().size());
		assertEquals("Chips", result.getContent().get(0).getName());
	}
	
	@Test
	void findFullProductInfosPaginatedShouldSortbyPrice() {
		// Given
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		
		ProductPage page = new ProductPage();
		page.setSortBy("price");
		
		// When
		when(productRepository.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(
				Mockito.any(), Mockito.any(), pageableCaptor.capture()))
		.thenReturn(productPage);
		Page<FullProductInfo> result = underTest.findFullProductInfosPaginated(page);
		// Then
		Sort s = pageableCaptor.getValue().getSort();
		assertNotNull(s.getOrderFor("(PRICE)"));
		assertEquals(1, result.getContent().size());
		assertEquals("Chips", result.getContent().get(0).getName());
	}
	
	@Test
	void findFullProductInfosPaginatedShouldSortbyCreatedDate() {
		// Given
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		
		ProductPage page = new ProductPage();
		page.setSortBy("createdDate");
		
		// When
		when(productRepository.findAllBySearchTextAndIncludeOrdersAfterAndNotDeleted(
				Mockito.any(), Mockito.any(), pageableCaptor.capture()))
		.thenReturn(productPage);
		Page<FullProductInfo> result = underTest.findFullProductInfosPaginated(page);
		// Then
		Sort s = pageableCaptor.getValue().getSort();
		assertNotNull(s.getOrderFor("(CREATED_DATE)"));
		assertEquals(1, result.getContent().size());
		assertEquals("Chips", result.getContent().get(0).getName());
	}
	
	@Test
	void findFullProductInfoById() {
		// Given
		Optional<Product> productOptional = Optional.ofNullable(product1);
		
		// When
		when(productRepository.findById(3L)).thenReturn(productOptional);
		FullProductInfo productInfo = underTest.findFullProductInfoById(3L);
		
		// Then
		assertEquals(3L, productInfo.getId());
		assertEquals(createdDate1, productInfo.getCreatedDate());
		assertEquals(2, productInfo.getOrderedItems().size());
	}
	
	@Test
	void findFullProductInfoByIdShouldThrowAnExceptionIfProductDoesNotExist() {
		// Given
		Optional<Product> productOptional = Optional.ofNullable(null);

		// When
		when(productRepository.findById(1L)).thenReturn(productOptional);
		
		// Then
		assertThrows(IllegalStateException.class, () -> underTest.findFullProductInfoById(1L), "Could not find product " + 1L);
	}
}