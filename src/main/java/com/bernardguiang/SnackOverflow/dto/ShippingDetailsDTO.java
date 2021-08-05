package com.bernardguiang.SnackOverflow.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.bernardguiang.SnackOverflow.model.ShippingDetails;

//Used as request by UpdateBillingAndShippingRequest
//Used as response as part of OrderResponse
public class ShippingDetailsDTO {
	private Long id;
	@NotBlank(message = "Name cannot be null or blank")
	private String name;
	
	@NotBlank(message ="Phone cannot be null or blank")
	@Pattern(regexp="(^$|[0-9]{10})", message="Phone number must consist of ten digits")
	private String phone;
	
	@NotNull(message = "Address cannot be null")
	@Valid
	private Address address;
	private Long orderId;
	
	public ShippingDetailsDTO() {

	}
	
	public ShippingDetailsDTO(ShippingDetails shippingDetails) {
		this.setId(shippingDetails.getId());
		this.setAddress(shippingDetails.getAddress());
		this.setName(shippingDetails.getName());
		this.setPhone(shippingDetails.getPhone());
		this.setOrderId(shippingDetails.getOrder().getId());
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
