package com.bernardguiang.SnackOverflow.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class RegisterRequest {
	
	@NotBlank
	@Email(message = "Must use a valid email")
	private String email;
	
	@NotBlank(message = "FullName cannot be blank or null")
	private String fullName;
	@NotBlank(message = "Username cannot be blank or null")
	private String username; //TODO: username validation
	@NotBlank(message = "Must use a valid password")
	private String password; //TODO: password validation
	
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
