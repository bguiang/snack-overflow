package com.bernardguiang.SnackOverflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.request.ProductPage;
import com.bernardguiang.SnackOverflow.dto.request.ProductPageAdmin;
import com.bernardguiang.SnackOverflow.dto.response.FullProductDTO;
import com.bernardguiang.SnackOverflow.service.CategoryService;
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
		return productService.searchProductsPaginated(page);
	}
	
	@GetMapping("/api/v1/products/{productId}")
	public ProductDTO getProductById(@PathVariable long productId) 
	{
		return productService.findById(productId);
	}
	
	// TODO: test
//	@GetMapping("/api/v1/admin/products")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
//	public Page<FullProductDTO> getProductsPaginatedForAdmin(ProductPage page) 
//	{
//		return productService.searchFullProductDTOsPaginated(page);
//	}
//	
	
//	@GetMapping("/api/v1/admin/products")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
//	public List<FullProductDTO> getProductsPaginatedForAdmin(ProductPage page) 
//	{
//		
//		return productService.getTopSellingProductsThisMonth(page);
//	}
	
	@GetMapping("/api/v1/admin/products")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Page<FullProductDTO> getProductsPaginatedForAdmin(ProductPageAdmin page) 
	{
		// This Should always Return all products?
		return productService.searchFullProductDTOs(page);
	}
	
	// TODO: test
	@GetMapping("/api/v1/admin/products/{productId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public FullProductDTO getProductByIdForAdmin(@PathVariable long productId) 
	{
		return productService.findFullProductDTOById(productId);
	}
	
//	@PostMapping
//	@PreAuthorize("hasAuthority('product:write')") // hasRole('ROLE_') hasAnyRole('ROLE_') hasAuthority('permission') hasAnyAuthority('permission')
//	public ProductDTO addProduct (@Valid @RequestBody ProductDTO productDTO)
//	{
//		System.out.println("DTO Received: " + productDTO.toString());
//		return productService.save(productDTO);
//	}
//	
//	@DeleteMapping
//	@PreAuthorize("hasAuthority('product:write')")
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

