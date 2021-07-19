package com.bernardguiang.SnackOverflow.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CheckoutRequest {
	@NotEmpty
	List<CartInfoRequestItem> items;
	//private Instant createdDate;
	private String notes;
	@NotNull
	private BillingDetailsDTO billingDetails;
	private ShippingDetailsDTO shippingDetails; // shipping can be null if same as billing
	private boolean isShippingSameAsBilling;
	//private OrderStatus status;
	
	public CheckoutRequest() {

	}
	
	public List<CartInfoRequestItem> getItems() {
		return items;
	}
	
	public void setItems(List<CartInfoRequestItem> items) {
		this.items = items;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public BillingDetailsDTO getBillingDetails() {
		return billingDetails;
	}
	public void setBillingDetails(BillingDetailsDTO billingDetails) {
		this.billingDetails = billingDetails;
	}
	public ShippingDetailsDTO getShippingDetails() {
		return shippingDetails;
	}
	public void setShippingDetails(ShippingDetailsDTO shippingDetails) {
		this.shippingDetails = shippingDetails;
	}
	public boolean isShippingSameAsBilling() {
		return isShippingSameAsBilling;
	}
	public void setShippingSameAsBilling(boolean isShippingSameAsBilling) {
		this.isShippingSameAsBilling = isShippingSameAsBilling;
	}
}
