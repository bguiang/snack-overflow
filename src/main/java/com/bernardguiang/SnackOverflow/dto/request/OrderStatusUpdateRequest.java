package com.bernardguiang.SnackOverflow.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.bernardguiang.SnackOverflow.model.OrderStatus;

public class OrderStatusUpdateRequest {
	@NotNull
	private long id;
	@NotNull
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
