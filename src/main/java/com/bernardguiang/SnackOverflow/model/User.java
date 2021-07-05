package com.bernardguiang.SnackOverflow.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String email;
	private String username;
	private String password;
	private String fullName;
	private String role;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "token_id", referencedColumnName = "id") // owning side contains the @JoinColumns (owns the foreign key column). Must save refresh token through User
	private RefreshToken refreshToken;
	
	public User() {}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
	
	public RefreshToken getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(RefreshToken refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", username=" + username + ", password=" + password
				+ ", fullName=" + fullName + ", role=" + role + ", refreshToken=" + refreshToken + "]";
	}
}
