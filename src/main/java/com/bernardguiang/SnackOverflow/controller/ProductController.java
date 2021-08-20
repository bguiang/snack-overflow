package com.bernardguiang.SnackOverflow.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.request.ProductPage;
import com.bernardguiang.SnackOverflow.dto.response.FullProductInfo;
import com.bernardguiang.SnackOverflow.service.ProductService;

@RestController
public class ProductController 
{
	private final ProductService productService;
	//private final CategoryService categoryService;
	//private final ProductIndexingService productIndexingService; // Full Text Search
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/api/v1/products")
	public Page<ProductDTO> getProductsPaginated(ProductPage page) 
	{
		return productService.findProductsPaginated(page);
	}
	
	@GetMapping("/api/v1/products/{productId}")
	public ProductDTO getProductById(@PathVariable long productId) 
	{
		return productService.findById(productId);
	}
	
	@PutMapping("/api/v1/admin/products")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ProductDTO updateProduct(@RequestBody @Valid ProductDTO product, HttpServletResponse response) {
		ProductDTO dto = productService.save(product);
		return dto;
	}
	
	@PostMapping("/api/v1/admin/products")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ProductDTO createProduct(@RequestBody @Valid ProductDTO product, HttpServletResponse response) {
		ProductDTO dto = productService.save(product);
		response.setStatus(201);
		return dto;
	}
	
	@GetMapping("/api/v1/admin/products")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Page<FullProductInfo> getProductsPaginatedForAdmin(ProductPage page) 
	{
		// This Should always Return all products?
		return productService.findFullProductInfosPaginated(page);
	}
	
	@GetMapping("/api/v1/admin/products/{productId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public FullProductInfo getProductByIdForAdmin(@PathVariable long productId) 
	{
		return productService.findFullProductInfoById(productId);
	}
	
	// Soft Delete
	// Product Will no longer show up on search but can still be loaded by id
	@DeleteMapping("/api/v1/admin/products/{productId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteProductById(@PathVariable long productId) {
		productService.deleteById(productId);
	}
	
//	@DeleteMapping
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
//	public void removeProduct()
//	{
//		
//	}
//	
//	@GetMapping("/categories")
//	public List<CategoryDTO> getCategories() 
//	{
//		return categoryService.findAll();
//	}
//	
//	@GetMapping("/categories/{categoryName}")
//	public List<ProductDTO> getProductsByCategory(@RequestParam String categoryName) 
//	{
//		return productService.findAllByCategoryName(categoryName);
//	}
//	
//	@PostMapping("/categories")
//	public CategoryDTO addCategory (@Valid CategoryDTO categoryDTO)
//	{
//		return categoryService.save(categoryDTO);
//	}
}

