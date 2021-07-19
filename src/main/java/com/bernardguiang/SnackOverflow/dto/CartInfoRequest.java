package com.bernardguiang.SnackOverflow.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

public class CartInfoRequest {
	@NotEmpty
	List<CartInfoRequestItem> items;
	
	public CartInfoRequest() {}
	public CartInfoRequest(@NotEmpty List<CartInfoRequestItem> items) {
		this.items = items;
	}

	public List<CartInfoRequestItem> getItems() {
		return items;
	}

	public void setItems(List<CartInfoRequestItem> items) {
		this.items = items;
	}
	
	
}
