package com.bernardguiang.SnackOverflow.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bernardguiang.SnackOverflow.dto.CategoryDTO;
import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.model.ProductPage;
import com.bernardguiang.SnackOverflow.service.CategoryService;
import com.bernardguiang.SnackOverflow.service.ProductIndexingService;
import com.bernardguiang.SnackOverflow.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController 
{
	private final ProductService productService;
	private final CategoryService categoryService;
	private final ProductIndexingService productIndexingService;
	
	@Autowired
	public ProductController(ProductService productService, CategoryService categoryService, ProductIndexingService productIndexingService) {
		this.productService = productService;
		this.categoryService = categoryService;
		this.productIndexingService = productIndexingService;
	}
	
	// Unit test will now fail
//	@GetMapping
//	public List<ProductDTO> getProducts() 
//	{
//		return productService.findAll();
//	}
	
	@GetMapping
	public Page<ProductDTO> getProductsPaginated(ProductPage page) 
	{
		return productService.searchProductsPaginated(page);
	}
	
	@GetMapping("/search")
	public List<ProductDTO> getProducts(String search) 
	{
		return productIndexingService.searchProducts(search);
	}
	
	@GetMapping("/{productId}")
	public ProductDTO getProductById(@PathVariable long productId) 
	{
		return productService.findById(productId);
	}
	@GetMapping(params="productIds")
	public Map<Long, ProductDTO> getProductsByIds(@RequestParam List<Long> productIds) 
	{
		Map<Long, ProductDTO> idToProductMap = new HashMap<>();
		for(Long id : productIds) {
			ProductDTO productInfo = productService.findById(id);
			idToProductMap.put(id, productInfo);
		}
		return idToProductMap;
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('product:write')") // hasRole('ROLE_') hasAnyRole('ROLE_') hasAuthority('permission') hasAnyAuthority('permission')
	public ProductDTO addProduct (@Valid @RequestBody ProductDTO productDTO)
	{
		System.out.println("DTO Received: " + productDTO.toString());
		return productService.save(productDTO);
	}
	
	@DeleteMapping
	@PreAuthorize("hasAuthority('product:write')")
	public void removeProduct()
	{
		
	}
	
	@GetMapping("/categories")
	public List<CategoryDTO> getCategories() 
	{
		return categoryService.findAll();
	}
	
	@GetMapping("/categories/{categoryName}")
	public List<ProductDTO> getProductsByCategory(@RequestParam String categoryName) 
	{
		return productService.findAllByCategoryName(categoryName);
	}
	
	@PostMapping("/categories")
	public CategoryDTO addCategory (@Valid CategoryDTO categoryDTO)
	{
		return categoryService.save(categoryDTO);
	}
}

