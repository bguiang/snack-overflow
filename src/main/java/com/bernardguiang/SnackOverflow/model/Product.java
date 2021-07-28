package com.bernardguiang.SnackOverflow.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String name;
	@Column(columnDefinition = "TEXT")
	private String description;
	private BigDecimal price;
	@ElementCollection
	private List<String> images;
	
	// Category has the mappedBy attribute on its @ManyToMany annotation
	// This means that the Product is the owner in the relationship
	// Changes to Product will cascade to the Category as well
	@ManyToMany 
	private Set<Category> categories = new HashSet<>();
	
	@OneToMany(mappedBy="product")
	private List<OrderItem> orderedItems;
	
	public Product() {
		
	}
	
	public Product(Long id, String name, String description, BigDecimal price, List<String> images,
			Set<Category> categories, List<OrderItem> orderedItems) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.images = images;
		this.categories = categories;
		this.orderedItems = orderedItems;
	}
	
	public Product(String name) {
		this.name = name;
	}
	
	public Product(Long id, String name) {
		this.id = id;
		this.name = name;
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

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
	

	public List<OrderItem> getOrderedItems() {
		return orderedItems;
	}

	public void setOrderedItems(List<OrderItem> orderedItems) {
		this.orderedItems = orderedItems;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
				+ ", images=" + images + "]";
	}
}
