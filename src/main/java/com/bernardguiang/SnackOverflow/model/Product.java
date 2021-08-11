package com.bernardguiang.SnackOverflow.model;

import java.math.BigDecimal;
import java.time.Instant;
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

import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

@Entity
@Indexed
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	@FullTextField//(analyzer = "english", projectable = Projectable.YES)
	//@KeywordField(name = "title_sort", normalizer = "english", sortable = Sortable.YES)
	@KeywordField(name = "name_sort", sortable = Sortable.YES) 
	private String name;
	@Column(columnDefinition = "TEXT")
	@FullTextField
	private String description;
	private BigDecimal price;
	private Instant createdDate;
	@ElementCollection
	private List<String> images;
	
	// Category has the mappedBy attribute on its @ManyToMany annotation
	// This means that the Product is the owner in the relationship
	// Changes to Product will cascade to the Category as well
	@ManyToMany 
	private Set<Category> categories = new HashSet<>();
	
	@OneToMany(mappedBy="product")
	private List<OrderItem> orderedItems;
	
	@OneToMany(mappedBy="product")
	private List<CartItem> cartItem;
	
	public Product() {
		
	}
	
	public Product(String name, String description, BigDecimal price, Instant createdDate, List<String> images,
			Set<Category> categories, List<OrderItem> orderedItems) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.createdDate = createdDate;
		this.images = images;
		this.categories = categories;
		this.orderedItems = orderedItems;
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

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
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

	public List<CartItem> getCartItem() {
		return cartItem;
	}

	public void setCartItem(List<CartItem> cartItem) {
		this.cartItem = cartItem;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
				+ ", createdDate=" + createdDate + ", images=" + images + ", categories=" + categories
				+ ", orderedItems=" + orderedItems + "]";
	}
}
