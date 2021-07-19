package com.bernardguiang.SnackOverflow.dto;

import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.ShippingDetails;

public class OrderDTO {

	private long id;
	@NotEmpty
	private List<OrderItemDTO> items;
	private Instant createdDate;
	private String notes;
	@NotNull
	private BillingDetailsDTO billingDetails;
	private ShippingDetailsDTO shippingDetails; // shipping can be null if same as billing
	private boolean isShippingSameAsBilling;
	private long userId; //TODO: validation for id?
	private OrderStatus status;

	public OrderDTO() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<OrderItemDTO> getItems() {
		return items;
	}

	public void setItems(List<OrderItemDTO> items) {
		this.items = items;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
}
