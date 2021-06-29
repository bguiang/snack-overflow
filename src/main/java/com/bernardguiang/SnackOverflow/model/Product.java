package com.bernardguiang.SnackOverflow.model;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String description;
	
	@ElementCollection
	private List<String> images;
	
	// Category has the mappedBy attribute on its @ManyToMany annotation
	// This means that the Product is the owner in the relationship
	// Changes to Product will cascade to the Category as well
	@ManyToMany 
	private Set<Category> categories = new HashSet<>();
	
	public Product() {
		
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
	
	
}
