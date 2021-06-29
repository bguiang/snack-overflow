package com.bernardguiang.SnackOverflow.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ProductDTO 
{
	private Long id;
	
	private String name;
	
	private String description;
	
	private List<String> images;
	
	private Set<String> categories = new HashSet<>();
	
	public ProductDTO()
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Set<String> getCategories() {
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}
	
	
}
