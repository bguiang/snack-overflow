package com.bernardguiang.SnackOverflow.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.bernardguiang.SnackOverflow.dto.Address;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique=true)
	private String email;
	@Column(unique=true)
	private String username;
	private String password;
	private String fullName;
	private String role;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL) 
	private List<Order> orders;
	
	@Embedded
	private Address address;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "token_id", referencedColumnName = "id") // owning side contains the @JoinColumns (owns the foreign key column). Must save refresh token through User
	private RefreshToken refreshToken;
	
	public User() {}
	
	public User(Long id, String email, String username, String password, String fullName, String role,
			List<Order> orders, Address address, RefreshToken refreshToken) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.role = role;
		this.orders = orders;
		this.address = address;
		this.refreshToken = refreshToken;
	}
	
	public User(String email, String username, String password, String fullName, String role,
			List<Order> orders, Address address, RefreshToken refreshToken) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.role = role;
		this.orders = orders;
		this.address = address;
		this.refreshToken = refreshToken;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public RefreshToken getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(RefreshToken refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", username=" + username + ", password=" + password
				+ ", fullName=" + fullName + ", role=" + role + ", address=" + address + ", refreshToken="
				+ refreshToken + "]";
	}
}
