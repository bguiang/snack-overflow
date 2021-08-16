package com.bernardguiang.SnackOverflow.dto.response;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.User;

// The main use of FullUserDTO is to display the User Page which lists the orders
public class FullUserDTO {
	private Long id;
	private String email;
	private String username;
	private String fullName;
	private String role;
	private List<OrderDTO> orders;
	private Address address;
	private Instant joinDate;
	
	public FullUserDTO() {
		
	}
	
	public FullUserDTO(User user) {
		this.setId(user.getId());
		this.setUsername(user.getUsername());
		this.setEmail(user.getEmail());
		this.setFullName(user.getFullName());
		this.setId(user.getId());
		
		List<OrderDTO> orderDTOs = new ArrayList<>();
		List<Order> orders = user.getOrders();
		for(Order order : orders) {
			orderDTOs.add(new OrderDTO(order));
		}
		
		this.setJoinDate(user.getJoinDate());
		
		this.setOrders(orderDTOs);
		
		this.setRole(user.getRole());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Instant getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Instant joinDate) {
		this.joinDate = joinDate;
	}

	public List<OrderDTO> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDTO> orders) {
		this.orders = orders;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
