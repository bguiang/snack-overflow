package com.bernardguiang.SnackOverflow.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Cart {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToMany(mappedBy="cart", cascade = CascadeType.ALL)
	private List<CartItem> items;
	
	private BigDecimal total;
	
	@OneToOne(mappedBy = "cart")
	private StripePaymentIntent stripePaymentIntent;
	
	public Cart() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<CartItem> getItems() {
		return items;
	}

	public void setItems(List<CartItem> items) {
		this.items = items;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public StripePaymentIntent getStripePaymentIntent() {
		return stripePaymentIntent;
	}

	public void setStripePaymentIntent(StripePaymentIntent stripePaymentIntent) {
		this.stripePaymentIntent = stripePaymentIntent;
	}

}
