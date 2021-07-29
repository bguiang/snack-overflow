package com.bernardguiang.SnackOverflow.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class CartInfoResponse {
	BigDecimal total;
	List<CartInfoResponseItem> items;
	
	public CartInfoResponse() {
	}
	public CartInfoResponse(BigDecimal total, List<CartInfoResponseItem> items) {
		this.total = total;
		this.items = items;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public List<CartInfoResponseItem> getItems() {
		return items;
	}
	public void setItems(List<CartInfoResponseItem> items) {
		this.items = items;
	}
	@Override
	public String toString() {
		return "Cart [total=" + total + ", items=" + items + "]";
	}
}
