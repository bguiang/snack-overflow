package com.bernardguiang.SnackOverflow.model;

import java.time.Instant;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders") // Order is a a special word in sql so we have to specify the table name for the class
public class Order {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	// Having the mappedBy on the Order class makes this the inverse side and the OrderItem class the Owner side
	// Since this is the inverse side, we have to Cascade changes made so that we can save OrderItems by saving/persisting Order
	// All JPA-specific cascade operations are represented by javax.persistence.CascadeType enum
	// - ALL - propagates all operations including hibernate ones from parent (this) to child (OrderItem) entity
	// - PERSIST
	// - MERGE
	// - REMOVE
	// - REFRESH
	// - DETACH
	// Hibernate supports 3 additional cascade types in org.hibernate.annotations.CascadeType
	// - REPLICATE
	// - SAVE_UPDATE
	// - LOCK
	@OneToMany(mappedBy="order", cascade=CascadeType.ALL) 
	private List<OrderItem> items;
	
	private Instant createdDate;
	
	private String billingName;
	//TODO: embedding the address like this makes the table really flat. Need to Normalize the table. Refactor later with inheritance
	@Embedded
	@AttributeOverrides({
		  @AttributeOverride( name = "addressLineOne", column = @Column(name = "billing_address_line_one")),
		  @AttributeOverride( name = "addressLineTwo", column = @Column(name = "billing_address_line_two")),
		  @AttributeOverride( name = "city", column = @Column(name = "billing_city")),
		  @AttributeOverride( name = "state", column = @Column(name = "billing_state")),
		  @AttributeOverride( name = "postalCode", column = @Column(name = "billing_postal_code")),
		  @AttributeOverride( name = "country", column = @Column(name = "billing_country"))
	})
	private Address billingAddress;
	
	private String shippingName;
	@Embedded
	@AttributeOverrides({
		  @AttributeOverride( name = "addressLineOne", column = @Column(name = "shipping_address_line_one")),
		  @AttributeOverride( name = "addressLineTwo", column = @Column(name = "shipping_address_line_two")),
		  @AttributeOverride( name = "city", column = @Column(name = "shipping_city")),
		  @AttributeOverride( name = "state", column = @Column(name = "shipping_state")),
		  @AttributeOverride( name = "postalCode", column = @Column(name = "shipping_postal_code")),
		  @AttributeOverride( name = "country", column = @Column(name = "shipping_country"))
	})
	private Address shippingAddress;
	private boolean isShippingSameAsBilling;
	private String notes;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;

	public Order() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public String getBillingName() {
		return billingName;
	}

	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public boolean isShippingSameAsBilling() {
		return isShippingSameAsBilling;
	}

	public void setShippingSameAsBilling(boolean isShippingSameAsBilling) {
		this.isShippingSameAsBilling = isShippingSameAsBilling;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	
}
