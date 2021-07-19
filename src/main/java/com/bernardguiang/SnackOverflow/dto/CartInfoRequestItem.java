package com.bernardguiang.SnackOverflow.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CartInfoRequestItem {
	@NotEmpty
	String productName;
	@NotNull
	Long productId;
	@Min(value = 1, message = "Item quantity cannot be zero")
	int quantity;
	
	public CartInfoRequestItem() {
	}
	
	public CartInfoRequestItem(@NotEmpty String productName, @NotNull Long productId,
			@Min(value = 1, message = "Item quantity cannot be zero") int quantity) {
		this.productName = productName;
		this.productId = productId;
		this.quantity = quantity;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
