package com.bernardguiang.SnackOverflow.dto;

public class RegisterRequest {
	
	private String email;
	private String fullName;
	private String username;
	private String password;
	
	public RegisterRequest() {
	}
	
	public RegisterRequest(String email, String fullName, String username, String password) {
		this.email = email;
		this.fullName = fullName;
		this.username = username;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getUsername() {
		return username;
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
	
	
}
