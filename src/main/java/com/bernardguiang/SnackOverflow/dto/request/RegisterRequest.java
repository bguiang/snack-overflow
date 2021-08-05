package com.bernardguiang.SnackOverflow.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.bernardguiang.SnackOverflow.utils.ValidPassword;

public class RegisterRequest {
	
	@NotBlank(message = "Email cannot be null or blank")
	@Email(message = "Must use a valid email")
	private String email;
	
	@NotBlank(message = "FullName cannot be blank or null")
	private String fullName;
	
	@NotNull(message = "Username cannot be null")
	@Size(min = 6, max = 20, message = "Username must be at least 6 characters and cannot be longer than 20 characters")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message="Username must consist of letters an numbers only")
	private String username;
	
	@ValidPassword //@ValidPassword returns a default "Invalid Password" violation with the password constraint violations
	private String password;
	
	public RegisterRequest() {
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
