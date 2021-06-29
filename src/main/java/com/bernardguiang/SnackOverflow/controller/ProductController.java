package com.bernardguiang.SnackOverflow.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	public ProductDTO addProduct (ProductDTO productDTO)
	{
		return productService.save(productDTO);
	}
	
	@DeleteMapping
	@PreAuthorize("hasAuthority('product:write')")
	public void removeProduct()
	{
		
	}
	
	@PostMapping("/categories")
	public CategoryDTO addCategory (CategoryDTO categoryDTO)
	{
		return categoryService.save(categoryDTO);
	}
	
	@GetMapping("/categories")
	@ResponseBody
	public List<ProductDTO> getProductsByCategory(@RequestParam String category) 
	{
		return productService.findAll();
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
		Set<String> pdto1Cat = new HashSet<>();
		pdto1Cat.add(cdto1.getName()); // Japan
		pdto1.setCategories(pdto1Cat);
		productService.save(pdto1);
		
		ProductDTO pdto2 = new ProductDTO();
		pdto2.setName("Pocky Strawberry");
		Set<String> pdto2Cat = new HashSet<>();
		pdto2Cat.add(cdto1.getName()); // Japan
		pdto2.setCategories(pdto2Cat);
		productService.save(pdto2);
		
		ProductDTO pdto3 = new ProductDTO();
		pdto3.setName("Taiyaki");
		Set<String> pdto3Cat = new HashSet<>();
		pdto3Cat.add(cdto1.getName()); // Japan
		pdto3.setCategories(pdto3Cat);
		productService.save(pdto3);
		
		ProductDTO pdto4 = new ProductDTO();
		pdto4.setName("Tim Tam");
		Set<String> pdto4Cat = new HashSet<>();
		pdto4Cat.add(cdto4.getName()); // Australia
		pdto4.setCategories(pdto4Cat);
		productService.save(pdto4);
		
		ProductDTO pdto5 = new ProductDTO();
		pdto5.setName("Tim Tam");
		Set<String> pdto5Cat = new HashSet<>();
		pdto5Cat.add(cdto4.getName()); // Australia
		pdto5.setCategories(pdto5Cat);
		productService.save(pdto5);
		
		ProductDTO pdto6 = new ProductDTO();
		pdto6.setName("Tim Tam");
		Set<String> pdto6Cat = new HashSet<>();
		pdto6Cat.add(cdto4.getName()); // Australia
		pdto6.setCategories(pdto6Cat);
		productService.save(pdto6);
	}
	
}

