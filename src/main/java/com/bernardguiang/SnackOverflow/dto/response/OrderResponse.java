package com.bernardguiang.SnackOverflow.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.bernardguiang.SnackOverflow.dto.BillingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.ShippingDetailsDTO;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;

// Used as response in OrderController
public class OrderResponse {

	private long id;
	private List<OrderResponseItem> items;
	private BigDecimal total;
	private Instant createdDate;
	private BillingDetailsDTO billingDetails;
	private ShippingDetailsDTO shippingDetails;
	private boolean isShippingSameAsBilling;
	private long userId;
	private OrderStatus status;

	public OrderResponse() {
	}
	
	public OrderResponse(Order order) {
		List<OrderResponseItem> itemDTOs = new ArrayList<>();
		for(OrderItem item : order.getItems()) {
			OrderResponseItem itemDTO = new OrderResponseItem(item);
			itemDTOs.add(itemDTO);
		}
		
		this.setId(order.getId());
		this.setItems(itemDTOs);
		this.setTotal(order.getTotal());
		this.setCreatedDate(order.getCreatedDate());
		
		// this null checking should only be used for OrderDTO because Orders are allowed to be partially saved when the paymentIntent is created
		if(order.getBillingDetails() != null) {
			BillingDetailsDTO billing = new BillingDetailsDTO(order.getBillingDetails());
			this.setBillingDetails(billing);
		}
		// this null checking should only be used for OrderDTO because Orders are allowed to be partially saved when the paymentIntent is created
		if(order.isShippingSameAsBilling()) {
			this.setShippingDetails(null);
		}
		else {
			ShippingDetailsDTO shipping = new ShippingDetailsDTO(order.getShippingDetails());
			this.setShippingDetails(shipping);
		}
		this.setIsShippingSameAsBilling(order.isShippingSameAsBilling());
		this.setUserId(order.getUser().getId());
		this.setStatus(order.getStatus());
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<OrderResponseItem> getItems() {
		return items;
	}

	public void setItems(List<OrderResponseItem> items) {
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

	public boolean getIsShippingSameAsBilling() {
		return isShippingSameAsBilling;
	}

	public void setIsShippingSameAsBilling(boolean isShippingSameAsBilling) {
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
