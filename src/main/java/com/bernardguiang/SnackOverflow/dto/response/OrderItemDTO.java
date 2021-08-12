package com.bernardguiang.SnackOverflow.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.bernardguiang.SnackOverflow.model.OrderItem;

public class OrderItemDTO {
	private Long id;
	
	private int quantity;
	private BigDecimal price;
	
	// Replace Order with Order Id and Order Create Date
	// private Order order;
	Long orderId;
	Instant orderCreatedDate;
	// Replace Product with ProductId
	//	private Product product;
	private Long productId;
	
	public OrderItemDTO() {

	}
	
	public OrderItemDTO(OrderItem orderItem) {
		this.id = orderItem.getId();
		this.quantity = orderItem.getQuantity();
		this.price = orderItem.getPrice();
		this.orderId = orderItem.getOrder().getId();
		this.orderCreatedDate = orderItem.getOrder().getCreatedDate();
		this.productId = orderItem.getProduct().getId();
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Instant getOrderCreatedDate() {
		return orderCreatedDate;
	}

	public void setOrderCreatedDate(Instant orderCreatedDate) {
		this.orderCreatedDate = orderCreatedDate;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	
}
