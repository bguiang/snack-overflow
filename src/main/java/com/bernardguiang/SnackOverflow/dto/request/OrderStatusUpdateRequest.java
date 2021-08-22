package com.bernardguiang.SnackOverflow.dto.request;

import javax.validation.constraints.NotNull;

import com.bernardguiang.SnackOverflow.model.OrderStatus;

public class OrderStatusUpdateRequest {
	@NotNull(message = "ID cannot be null")
	private Long id;
	@NotNull(message = "OrderStatus cannot be null")
	private OrderStatus status;
	
	public OrderStatusUpdateRequest() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
	
}
