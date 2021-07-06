package com.bernardguiang.SnackOverflow.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderItemDTO {
	private long id;
	private long productId;
	@NotNull
	private BigDecimal price;
	@Min(1)
	private int quantity;
	
	public OrderItemDTO() {
	}
	
	public OrderItemDTO(long id, long productId, @NotNull BigDecimal price, @Min(1) int quantity) {
		super();
		this.id = id;
		this.productId = productId;
		this.price = price;
		this.quantity = quantity;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "OrderItemDTO [id=" + id + ", productId=" + productId + ", price=" + price + ", quantity=" + quantity
				+ "]";
	}
	
	
}
