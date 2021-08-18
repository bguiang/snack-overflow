package com.bernardguiang.SnackOverflow.service;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.request.ProductPage;
import com.bernardguiang.SnackOverflow.dto.response.FullProductInfo;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.repository.CategoryRepository;
import com.bernardguiang.SnackOverflow.repository.OrderItemRepository;
import com.bernardguiang.SnackOverflow.repository.ProductRepository;

@Service
public class ProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final OrderItemRepository orderItemRepository;

	@Autowired
	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository,
			OrderItemRepository orderItemRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.orderItemRepository = orderItemRepository;
	}

	public ProductDTO save(ProductDTO productDTO) {
		// Convert Product to ProductDTO
		Product product;
		
		// Check if product exists
		Long id = productDTO.getId();
		if(id != null) {
			product = productRepository.findById(id)
					.orElseThrow(() -> new IllegalStateException("Could not find product " + id));
		}
		else {
			product = new Product();
			product.setCreatedDate(Instant.now());
		}
		
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		
		product.setImages(productDTO.getImages());

		// Should it throw an error if category doesn't exist?
		Set<Category> categories = new HashSet<>();
		for (String categoryName : productDTO.getCategories()) {
			Category category = categoryRepository.findByName(categoryName)
					.orElseThrow(() -> new IllegalStateException("Could not find category " + categoryName));
			categories.add(category);
		}
		product.setCategories(categories);

		Product saved = productRepository.save(product);
		ProductDTO productDTOSaved = new ProductDTO(saved);

		return productDTOSaved;
	}

	public ProductDTO findById(long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalStateException("Could not find product " + id));
		return new ProductDTO(product);
	}

//	public Page<ProductDTO> findProductsPaginated(ProductPage page) {
//		Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
//		Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
//
//		Page<Product> result = productRepository.findAllByNameContainingIgnoreCase(page.getSearch(), pageable);
//
//		// Returns a new Page with the content of the current one mapped by the given
//		// Function.
//		Page<ProductDTO> dtoPage = result.map(new Function<Product, ProductDTO>() {
//			@Override
//			public ProductDTO apply(Product entity) {
//				ProductDTO dto = new ProductDTO(entity);
//				return dto;
//			}
//		});
//
//		return dtoPage;
//	}
	
	
	public Page<ProductDTO> findProductsPaginated(ProductPage page) {
		Sort sort = null;
		switch(page.getSortBy()) {
			case "unitsSold": 
				sort = JpaSort.unsafe(page.getSortDirection(), "(UNITS_SOLD)", "(ID)");
				break;
			case "id": 
				sort = JpaSort.unsafe(page.getSortDirection(), "(ID)");
				break;
			case "name": 
				sort = JpaSort.unsafe(page.getSortDirection(), "(NAME)");
				break;
			case "price": 
				sort = JpaSort.unsafe(page.getSortDirection(), "(PRICE)");
				break;
			case "createdDate": 
				sort = JpaSort.unsafe(page.getSortDirection(), "(CREATED_DATE)");
				break;
		}
		Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);

		Page<Product> result = productRepository.findAllBySearchTextAndIncludeOrdersAfter(page.getSearch(), null, pageable);;

		Page<ProductDTO> dtoPage = result.map(new Function<Product, ProductDTO>() {
			@Override
			public ProductDTO apply(Product product) {

				ProductDTO dto = new ProductDTO(product);
				return dto;
			}
		});

		return dtoPage;
	}
	
	

	public Page<FullProductInfo> findFullProductInfosPaginated(ProductPage page) {
		Sort sort = null;
		switch(page.getSortBy()) {
			case "unitsSold": 
				// Jpa sorting doesnt work for Aggregates as Field Aliases as it looks for
				// existing columns
				// https://github.com/spring-projects/spring-data-jpa/issues/1404
				// JpaSort.unsafe() will be unchecked by Spring, and is susceptible to SQL
				// injection
				// do not let users pass in data though it manually
				// Also, () are needed to use am Alias with JpaSort.unsafe()
				// https://github.com/spring-projects/spring-data-jpa/issues/1404#issuecomment-784871680
				
				// Since the UNITS_SOLD column doesn't exist, we have to sort by another column to keep the order
				// of the results consistent
				sort = JpaSort.unsafe(page.getSortDirection(), "(UNITS_SOLD)", "(ID)");
				break;
			case "id": 
				sort = JpaSort.unsafe(page.getSortDirection(), "(ID)");
				break;
			case "name": 
				sort = JpaSort.unsafe(page.getSortDirection(), "(NAME)");
				break;
			case "price": 
				sort = JpaSort.unsafe(page.getSortDirection(), "(PRICE)");
				break;
			case "createdDate": 
				sort = JpaSort.unsafe(page.getSortDirection(), "(CREATED_DATE)");
				break;
		}
		Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);

		Page<Product> result = null;
		if (page.getItemsSold().equalsIgnoreCase("month")) {
			LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
			Instant start = firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
			result = productRepository.findAllBySearchTextAndIncludeOrdersAfter(page.getSearch(), start, pageable);
		} else if (page.getItemsSold().equalsIgnoreCase("year")) {
			LocalDate firstDayOfYear = LocalDate.now().with(firstDayOfYear());
			Instant start = firstDayOfYear.atStartOfDay(ZoneId.systemDefault()).toInstant();
			result = productRepository.findAllBySearchTextAndIncludeOrdersAfter(page.getSearch(), start, pageable);
		} else { // ALL
			result = productRepository.findAllBySearchTextAndIncludeOrdersAfter(page.getSearch(), null, pageable);
		}

		Page<FullProductInfo> dtoPage = result.map(new Function<Product, FullProductInfo>() {
			@Override
			public FullProductInfo apply(Product product) {

				// Update ordered items list on date range on orders to include
				List<OrderStatus> excludedStatuses = 
						Arrays.asList(OrderStatus.FAILED, OrderStatus.CANCELLED, OrderStatus.REFUNDED);
				
				Iterable<OrderItem> iterable;
				List<OrderItem> orderedItems = new ArrayList<>();
				if (page.getItemsSold().equalsIgnoreCase("month")) {
					LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
					Instant start = firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
					iterable = orderItemRepository
							.findAllByProductIdAndOrderCreatedDateAfterAndOrderStatusNotIn(product.getId(), start, excludedStatuses);
				} 
				else if (page.getItemsSold().equalsIgnoreCase("year")) {
					LocalDate firstDayOfYear = LocalDate.now().with(firstDayOfYear());
					Instant start = firstDayOfYear.atStartOfDay(ZoneId.systemDefault()).toInstant();
					iterable = orderItemRepository
							.findAllByProductIdAndOrderCreatedDateAfterAndOrderStatusNotIn(product.getId(), start, excludedStatuses);
				}
				else {
					iterable = orderItemRepository
							.findAllByProductIdAndOrderStatusNotIn(product.getId(), excludedStatuses);
				}

				iterable.forEach(orderedItems::add);
				product.setOrderedItems(orderedItems);

				FullProductInfo dto = new FullProductInfo(product);
				return dto;
			}
		});

		return dtoPage;
	}

	public FullProductInfo findFullProductInfoById(long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalStateException("Could not find product " + id));
		return new FullProductInfo(product);
	}
}
