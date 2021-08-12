package com.bernardguiang.SnackOverflow.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.Product;

public class FullProductDTO 
{
	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private List<String> images;
	private List<String> categories;
	
	private List<OrderItemDTO> orderedItems;
	
	public FullProductDTO()
	{
		
	}
	
	public FullProductDTO(Product product) {
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
		
		List<OrderItemDTO> orderedItemsDTO = new ArrayList<>();
		for(OrderItem orderItem : product.getOrderedItems()) {
			orderedItemsDTO.add(new OrderItemDTO(orderItem));
		}
		this.setOrderedItems(orderedItemsDTO);
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

	public List<OrderItemDTO> getOrderedItems() {
		return orderedItems;
	}

	public void setOrderedItems(List<OrderItemDTO> orderedItems) {
		this.orderedItems = orderedItems;
	}
	
	
}