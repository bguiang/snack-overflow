package com.bernardguiang.SnackOverflow.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.bernardguiang.SnackOverflow.model.Product;

public class CategoryDTO {
	private Long id;
	private String name;
//	private Set<ProductDTO> products = new HashSet<>();
	
	public CategoryDTO() 
	{
	
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public Set<ProductDTO> getProducts() {
//		return products;
//	}
//	public void setProducts(Set<ProductDTO> products) {
//		this.products = products;
//	}
	
	
}
