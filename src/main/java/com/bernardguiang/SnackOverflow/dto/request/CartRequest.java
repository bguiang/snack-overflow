package com.bernardguiang.SnackOverflow.dto.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class CartRequest {
	@NotEmpty(message = "Cart Items cannot be null or empty")
	List<@Valid CartInfoRequestItem> items;
	
	public CartRequest() {}

	public List<CartInfoRequestItem> getItems() {
		return items;
	}

	public void setItems(List<CartInfoRequestItem> items) {
		this.items = items;
	}
	
	
}
