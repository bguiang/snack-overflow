package com.bernardguiang.SnackOverflow.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.bernardguiang.SnackOverflow.model.User;

// Not used as part of a request since its usually grabbed inside the endpoint using the Authority
// Not yet used as a response
public class UserDTO {
	private Long id;
	
	@NotBlank(message = "Email cannot be null or blank")
	@Email(message = "Must use a valid email")
	private String email;
	
	@NotNull(message = "Username cannot be blank or null")
	@Size(min = 6, max = 15, message = "Username must be at least 6 characters and cannot be longer than 15 characters")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message="Username must consist of letters an numbers only")
	private String username;
	
	@NotBlank(message = "FullName cannot be null or blank")
	private String fullName;
	
	@NotBlank(message = "Role cannot be null or blank")
	private String role;
	
	public UserDTO() {
		
	}
	
	public UserDTO(User user) {
		this.setId(user.getId());
		this.setUsername(user.getUsername());
		this.setEmail(user.getEmail());
		this.setFullName(user.getFullName());
		this.setId(user.getId());
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
	
}