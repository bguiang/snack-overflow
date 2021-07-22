package com.bernardguiang.SnackOverflow.dto.request;

public class UsernameAndPasswordAuthenticationRequest {

	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	
	public UsernameAndPasswordAuthenticationRequest() {
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
