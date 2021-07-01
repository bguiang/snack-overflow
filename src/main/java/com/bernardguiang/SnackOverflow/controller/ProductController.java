package com.bernardguiang.SnackOverflow.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.CategoryDTO;
import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;
import com.bernardguiang.SnackOverflow.service.CategoryService;
import com.bernardguiang.SnackOverflow.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController 
{
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public List<ProductDTO> getProducts() 
	{
		return productService.findAll();
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
	public CategoryDTO addCategory (CategoryDTO categoryDTO)
	{
		return categoryService.save(categoryDTO);
	}
	
	@EventListener(classes = { ContextRefreshedEvent.class})
	public void setInitialProducts() {
		CategoryDTO cdto1 = new CategoryDTO();
		cdto1.setName("Japan");
		CategoryDTO category1 = categoryService.save(cdto1);
		
		CategoryDTO cdto2 = new CategoryDTO();
		cdto1.setName("Korea");
		CategoryDTO category2 = categoryService.save(cdto1);
		
		CategoryDTO cdto3 = new CategoryDTO();
		cdto1.setName("USA");
		CategoryDTO category3 = categoryService.save(cdto1);
		
		CategoryDTO cdto4 = new CategoryDTO();
		cdto1.setName("Australia");
		CategoryDTO category4 = categoryService.save(cdto1);
		
		
		ProductDTO pdto1 = new ProductDTO();
		pdto1.setName("Pocky Chocolate");
		List<String> pdto1Cat = new ArrayList<>();
		pdto1Cat.add(cdto1.getName()); // Japan
		pdto1.setDescription("snack");
		pdto1.setPrice(new BigDecimal("1.99"));
		pdto1.setCategories(pdto1Cat);
		productService.save(pdto1);
		
		ProductDTO pdto2 = new ProductDTO();
		pdto2.setName("Pocky Strawberry");
		List<String> pdto2Cat = new ArrayList<>();
		pdto2Cat.add(cdto1.getName()); // Japan
		pdto2.setDescription("snack");
		pdto2.setPrice(new BigDecimal("1.99"));
		pdto2.setCategories(pdto2Cat);
		productService.save(pdto2);
		
		ProductDTO pdto3 = new ProductDTO();
		pdto3.setName("Taiyaki");
		List<String> pdto3Cat = new ArrayList<>();
		pdto3Cat.add(cdto1.getName()); // Japan
		pdto3.setDescription("snack");
		pdto3.setPrice(new BigDecimal("1.99"));
		pdto3.setCategories(pdto3Cat);
		productService.save(pdto3);
		
		ProductDTO pdto4 = new ProductDTO();
		pdto4.setName("Tim Tam1");
		List<String> pdto4Cat = new ArrayList<>();
		pdto4Cat.add(cdto4.getName()); // Australia
		pdto4.setDescription("snack");
		pdto4.setPrice(new BigDecimal("1.99"));
		pdto4.setCategories(pdto4Cat);
		productService.save(pdto4);
		
		ProductDTO pdto5 = new ProductDTO();
		pdto5.setName("Tim Tam2");
		List<String> pdto5Cat = new ArrayList<>();
		pdto5Cat.add(cdto4.getName()); // Australia
		pdto5.setDescription("snack");
		pdto5.setPrice(new BigDecimal("1.99"));
		pdto5.setCategories(pdto5Cat);
		productService.save(pdto5);
		
		ProductDTO pdto6 = new ProductDTO();
		pdto6.setName("Tim Tam3");
		List<String> pdto6Cat = new ArrayList<>();
		pdto6Cat.add(cdto4.getName()); // Australia
		pdto6.setDescription("snack");
		pdto6.setPrice(new BigDecimal("1.99"));
		pdto6.setCategories(pdto6Cat);
		productService.save(pdto6);
	}
	
}

