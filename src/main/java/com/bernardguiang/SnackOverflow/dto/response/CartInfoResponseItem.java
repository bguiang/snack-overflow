package com.bernardguiang.SnackOverflow.dto.response;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;

public class CartInfoResponseItem {
	@Min(value = 1, message = "Item quantity cannot be zero")
	int quantity;
	@NotNull
	ProductDTO product;
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public ProductDTO getProduct() {
		return product;
	}
	public void setProduct(ProductDTO product) {
		this.product = product;
	}
	@Override
	public String toString() {
		return "CartItem [quantity=" + quantity + ", product=" + product + "]";
	}
	
	
}
