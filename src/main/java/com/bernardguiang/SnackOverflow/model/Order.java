package com.bernardguiang.SnackOverflow.model;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "billing_details_id", referencedColumnName = "id") // owning side contains the @JoinColumns (owns the foreign key column). Must save billing through order
	private BillingDetails billingDetails;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "shipping_details_id", referencedColumnName = "id") // owning side contains the @JoinColumns (owns the foreign key column). Must save shipping through order
	private ShippingDetails shippingDetails;
	
	private boolean isShippingSameAsBilling;
	private String notes;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;
	
	private OrderStatus status = OrderStatus.CREATED;

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

	public BillingDetails getBillingDetails() {
		return billingDetails;
	}

	public void setBillingDetails(BillingDetails billingDetails) {
		this.billingDetails = billingDetails;
	}

	public ShippingDetails getShippingDetails() {
		return shippingDetails;
	}

	public void setShippingDetails(ShippingDetails shippingDetails) {
		this.shippingDetails = shippingDetails;
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

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
	
}
