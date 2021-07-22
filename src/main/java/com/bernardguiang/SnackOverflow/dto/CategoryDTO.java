package com.bernardguiang.SnackOverflow.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.bernardguiang.SnackOverflow.model.Category;

public class CategoryDTO {
	private Long id;
	@NotBlank
	private String name;
	
	public CategoryDTO() 
	{
	
	}
	
	public CategoryDTO(Category category) 
	{
		this.setId(category.getId());
		this.setName(category.getName());
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
	
}
