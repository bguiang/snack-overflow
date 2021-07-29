package com.bernardguiang.SnackOverflow.dto.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CartInfoRequest {
	@NotEmpty(message = "Cart Items cannot be empty")
	List<@NotNull CartInfoRequestItem> items;
	
	public CartInfoRequest() {}

	public List<CartInfoRequestItem> getItems() {
		return items;
	}

	public void setItems(List<CartInfoRequestItem> items) {
		this.items = items;
	}
	
	
}
