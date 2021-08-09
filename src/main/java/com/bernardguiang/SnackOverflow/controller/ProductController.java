package com.bernardguiang.SnackOverflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.request.ProductPage;
import com.bernardguiang.SnackOverflow.service.CategoryService;
import com.bernardguiang.SnackOverflow.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController 
{
	private final ProductService productService;
	//private final CategoryService categoryService;
	//private final ProductIndexingService productIndexingService; // Full Text Search
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping
	public Page<ProductDTO> getProductsPaginated(ProductPage page) 
	{
		return productService.searchProductsPaginated(page);
	}
	
	@GetMapping("/{productId}")
	public ProductDTO getProductById(@PathVariable long productId) 
	{
		return productService.findById(productId);
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

