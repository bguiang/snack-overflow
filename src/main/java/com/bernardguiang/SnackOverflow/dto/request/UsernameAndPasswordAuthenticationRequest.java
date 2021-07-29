package com.bernardguiang.SnackOverflow.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.bernardguiang.SnackOverflow.utils.ValidPassword;

public class UsernameAndPasswordAuthenticationRequest {

	@NotNull(message = "Username cannot be blank or null")
	@Size(min = 6, max = 15, message = "Username must be at least 6 characters and cannot be longer than 15 characters")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message="Username must consist of letters an numbers only")
	private String username;
	
	@ValidPassword
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
