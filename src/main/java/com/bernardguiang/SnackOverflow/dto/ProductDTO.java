package com.bernardguiang.SnackOverflow.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Product;

// Create a separate dto for create request
// Used as request for UPDATING new product in ProductController
// Used as response for GETTING products in ProductController
public class ProductDTO 
{
	private Long id;
	
	@NotBlank(message = "Name cannot be null or blank")
	private String name;
	
	@NotBlank(message = "Description cannot be null or blank")
	private String description;
	
	@NotNull(message = "Price cannot be null")
	private BigDecimal price;
	
	@NotEmpty(message = "Images cannot be empty")
	private List<@NotBlank(message = "Image url cannot be blank or null") String> images;
	
	private List<String> categories;
	
	private boolean deleted;
	
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
		
		this.setDeleted(product.isDeleted());
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
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "ProductDTO [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
				+ ", images=" + images + ", categories=" + categories + "]";
	}
	
	
}
