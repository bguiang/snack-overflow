package com.bernardguiang.SnackOverflow.dto;

import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.bernardguiang.SnackOverflow.model.Address;

public class OrderDTO {

	private long id;
	@NotEmpty
	private List<OrderItemDTO> items;
	private Instant createdDate;
	private String notes;
	@NotEmpty
	private String billingName;
	@NotNull
	private Address billingAddress;
	private String shippingName;
	private Address shippingAddress; // shipping can be null if same as billing
	private boolean isShippingSameAsBilling;
	private long userId; //TODO: validation for id?

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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		String toprint = "OrderDTO [id=" + id + ", createdDate=" + createdDate + ", notes=" + notes
				+ ", billingName=" + billingName + ", billingAddress=" + billingAddress + ", shippingName="
				+ shippingName + ", shippingAddress=" + shippingAddress + ", isShippingSameAsBilling="
				+ isShippingSameAsBilling + ", userId=" + userId + "]\n";
		for(OrderItemDTO item : items) {
			toprint += item.toString() + "\n";
		}
		return toprint;
	}
}
