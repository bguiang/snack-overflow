package com.bernardguiang.SnackOverflow.service;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
import com.bernardguiang.SnackOverflow.dto.request.ProductPageAdmin;
import com.bernardguiang.SnackOverflow.dto.response.FullProductDTO;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.OrderItem;
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
		Product product = new Product();
		product.setId(productDTO.getId());
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		product.setCreatedDate(Instant.now());
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

	public List<ProductDTO> findAll() {
		Iterable<Product> productsIterator = productRepository.findAll();

		List<ProductDTO> productDTOs = new ArrayList<>();
		for (Product product : productsIterator) {
			ProductDTO productDTO = new ProductDTO(product);
			productDTOs.add(productDTO);
		}

		return productDTOs;
	}

	public List<ProductDTO> findAllByCategoryName(String categoryName) {
		List<ProductDTO> productDTOs = new ArrayList<>();

		Category category = categoryRepository.findByName(categoryName)
				.orElseThrow(() -> new IllegalStateException("Could not find category " + categoryName));
		for (Product product : category.getProducts()) {
			productDTOs.add(new ProductDTO(product));
		}

		return productDTOs;
	}

	public Page<ProductDTO> searchProductsPaginated(ProductPage page) {
		Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
		Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);

		Page<Product> result = productRepository.findAllByNameContainingIgnoreCase(page.getSearch(), pageable);

		// Returns a new Page with the content of the current one mapped by the given
		// Function.
		Page<ProductDTO> dtoPage = result.map(new Function<Product, ProductDTO>() {
			@Override
			public ProductDTO apply(Product entity) {
				ProductDTO dto = new ProductDTO(entity);
				return dto;
			}
		});

		return dtoPage;
	}


	public Page<FullProductDTO> searchFullProductDTOs(ProductPageAdmin page) {
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
				sort = JpaSort.unsafe(page.getSortDirection(), "(UNITS_SOLD)");
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

		Page<FullProductDTO> dtoPage = result.map(new Function<Product, FullProductDTO>() {
			@Override
			public FullProductDTO apply(Product product) {

				// Update ordered items list on date range on orders to include
				if (page.getItemsSold().equalsIgnoreCase("month")) {
					LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
					Instant start = firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
					Iterable<OrderItem> iterable = orderItemRepository
							.findAllByProductIdAndOrderCreatedDateAfter(product.getId(), start);
					List<OrderItem> orderItemsThisMonth = new ArrayList<>();
					iterable.forEach(orderItemsThisMonth::add);
					product.setOrderedItems(orderItemsThisMonth);
				} else if (page.getItemsSold().equalsIgnoreCase("year")) {
					LocalDate firstDayOfYear = LocalDate.now().with(firstDayOfYear());
					Instant start = firstDayOfYear.atStartOfDay(ZoneId.systemDefault()).toInstant();
					Iterable<OrderItem> iterable = orderItemRepository
							.findAllByProductIdAndOrderCreatedDateAfter(product.getId(), start);
					List<OrderItem> orderItemsThisMonth = new ArrayList<>();
					iterable.forEach(orderItemsThisMonth::add);
					product.setOrderedItems(orderItemsThisMonth);
				}

				FullProductDTO dto = new FullProductDTO(product);
				return dto;
			}
		});

		return dtoPage;
	}

	public FullProductDTO findFullProductDTOById(long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalStateException("Could not find product " + id));
		return new FullProductDTO(product);
	}
}
