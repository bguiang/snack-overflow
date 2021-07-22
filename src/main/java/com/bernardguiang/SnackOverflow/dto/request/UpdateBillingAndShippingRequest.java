package com.bernardguiang.SnackOverflow.dto.request;

import javax.validation.constraints.NotNull;

import com.bernardguiang.SnackOverflow.dto.BillingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.ShippingDetailsDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateBillingAndShippingRequest {
	@NotNull
	private Long id;
	@NotNull
	private BillingDetailsDTO billingDetails;
	private ShippingDetailsDTO shippingDetails; // shipping can be null if same as billing
	@JsonProperty
	private boolean isShippingSameAsBilling; // @JsonProperty is required because jackson doesn't like getters without the word "get"
	
	public UpdateBillingAndShippingRequest() {

	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
