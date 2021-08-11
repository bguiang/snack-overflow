package com.bernardguiang.SnackOverflow.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.bernardguiang.SnackOverflow.dto.BillingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.ShippingDetailsDTO;
import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;

//TODO: test
// Used when retrieving order information on admin dashboard. 
// Includes UserDTO instead of just userID unlike in OrderResponse
public class OrderDTO {

	private long id;
	private List<OrderResponseItem> items;
	private BigDecimal total;
	private Instant createdDate;
	private BillingDetailsDTO billingDetails;
	private ShippingDetailsDTO shippingDetails;
	private boolean isShippingSameAsBilling;
	private UserDTO user;
	private OrderStatus status;
	
	public OrderDTO() {
		
	}
	
	public OrderDTO(Order order) {
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
		if(order.isShippingSameAsBilling()) {
			this.setShippingDetails(null);
		}
		else {
			if(order.getShippingDetails() != null) {
				ShippingDetailsDTO shipping = new ShippingDetailsDTO(order.getShippingDetails());
				this.setShippingDetails(shipping);
			}
		}
		this.setIsShippingSameAsBilling(order.isShippingSameAsBilling());
		this.setUser(new UserDTO(order.getUser()));
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
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
	
	
}
