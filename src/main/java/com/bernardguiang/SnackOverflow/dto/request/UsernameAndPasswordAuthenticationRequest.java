package com.bernardguiang.SnackOverflow.dto.request;

import javax.validation.constraints.NotBlank;

public class UsernameAndPasswordAuthenticationRequest {

	@NotBlank
	private String username;
	@NotBlank
	private String password; //TODO: password validation
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
