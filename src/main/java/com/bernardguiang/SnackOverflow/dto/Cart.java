package com.bernardguiang.SnackOverflow.dto;

import java.math.BigDecimal;
import java.util.List;

public class Cart {
	BigDecimal total;
	List<CartItem> items;
	
	public Cart() {
	}
	public Cart(BigDecimal total, List<CartItem> items) {
		this.total = total;
		this.items = items;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public List<CartItem> getItems() {
		return items;
	}
	public void setItems(List<CartItem> items) {
		this.items = items;
	}
	@Override
	public String toString() {
		return "Cart [total=" + total + ", items=" + items + "]";
	}
}
