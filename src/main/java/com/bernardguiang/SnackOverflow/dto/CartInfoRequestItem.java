package com.bernardguiang.SnackOverflow.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CartInfoRequestItem {
	@NotNull
	Long productId;
	@Min(value = 1, message = "Item quantity cannot be zero")
	int quantity;
	
	public CartInfoRequestItem() {
	}
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
