package com.bernardguiang.SnackOverflow.dto;

import com.bernardguiang.SnackOverflow.model.Address;
import com.bernardguiang.SnackOverflow.model.BillingDetails;

public class BillingDetailsDTO {
	private Long id;
	private String name;
	private String email;
	private String phone;
	private Address address;
	private Long orderId;
	
	public BillingDetailsDTO() {

	}
	
	public BillingDetailsDTO(BillingDetails billingDetails) {
		this.setId(billingDetails.getId());
		this.setAddress(billingDetails.getAddress());
		this.setName(billingDetails.getName());
		this.setPhone(billingDetails.getPhone());
		this.setEmail(billingDetails.getEmail());
		this.setOrderId(billingDetails.getOrder().getId());
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	
}
