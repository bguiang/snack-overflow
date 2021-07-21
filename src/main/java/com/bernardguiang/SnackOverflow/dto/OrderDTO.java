package com.bernardguiang.SnackOverflow.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.ShippingDetails;

public class OrderDTO {

	private long id;
	@NotEmpty
	private List<OrderItemDTO> items;
	private BigDecimal total;
	private Instant createdDate;
	@NotNull
	private BillingDetailsDTO billingDetails;
	private ShippingDetailsDTO shippingDetails;
	private boolean isShippingSameAsBilling;
	private long userId;
	private OrderStatus status;

	public OrderDTO() {
	}
	
	public OrderDTO(Order order) {
		List<OrderItemDTO> itemDTOs = new ArrayList<>();
		for(OrderItem item : order.getItems()) {
			
			OrderItemDTO itemDTO = new OrderItemDTO(item);
		}
		
		this.setId(order.getId());
		this.setItems(itemDTOs);
		this.setTotal(order.getTotal());
		this.setCreatedDate(order.getCreatedDate());
		BillingDetailsDTO billing = new BillingDetailsDTO(order.getBillingDetails());
		this.setBillingDetails(billing);
		ShippingDetailsDTO shipping = new ShippingDetailsDTO(order.getShippingDetails());
		this.setShippingDetails(shipping);
		this.setShippingSameAsBilling(order.isShippingSameAsBilling());
		this.setUserId(order.getUser().getId());
		this.setStatus(order.getStatus());
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<OrderItemDTO> getItems() {
		return items;
	}

	public void setItems(List<OrderItemDTO> items) {
		this.items = items;
	}
	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public BillingDetailsDTO getBillingDetails() {
		return billingDetails;
	}

	public void setBillingDetails(BillingDetailsDTO billingDetails) {
		this.billingDetails = billingDetails;
	}

	public ShippingDetailsDTO getShippingDetails() {
		return shippingDetails;
	}

	public void setShippingDetails(ShippingDetailsDTO shippingDetails) {
		this.shippingDetails = shippingDetails;
	}

	public boolean isShippingSameAsBilling() {
		return isShippingSameAsBilling;
	}

	public void setShippingSameAsBilling(boolean isShippingSameAsBilling) {
		this.isShippingSameAsBilling = isShippingSameAsBilling;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
}
