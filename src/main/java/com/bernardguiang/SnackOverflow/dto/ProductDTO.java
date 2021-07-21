package com.bernardguiang.SnackOverflow.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Product;


public class ProductDTO 
{
	private Long id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String description;
	
	@NotNull
	private BigDecimal price;
	
	private List<String> images;
	
	private List<String> categories;
	
	public ProductDTO()
	{
		
	}
	
	public ProductDTO(Product product) {
		
		this.setId(product.getId());
		this.setName(product.getName());
		this.setDescription(product.getDescription());
		this.setPrice(product.getPrice());
		this.setImages(product.getImages());
		
		List<String> categoriesDTO = new ArrayList<>();
		for(Category category : product.getCategories())
		{
			categoriesDTO.add(category.getName());
		}
		this.setCategories(categoriesDTO);
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "ProductDTO [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
				+ ", images=" + images + ", categories=" + categories + "]";
	}
	
	
}
