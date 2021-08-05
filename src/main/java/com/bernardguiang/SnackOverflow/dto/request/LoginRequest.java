package com.bernardguiang.SnackOverflow.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.bernardguiang.SnackOverflow.utils.ValidPassword;

public class LoginRequest {

	@NotNull(message = "Username cannot be null")
	@Size(min = 6, max = 20, message = "Username must be at least 6 characters and cannot be longer than 20 characters")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message="Username must consist of letters an numbers only")
	private String username;

	@ValidPassword //@ValidPassword returns a default "Invalid Password" violation with the password constraint violations
	private String password;
	
	public String getUsername() {
		return username;
	}
	
	public LoginRequest() {
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
