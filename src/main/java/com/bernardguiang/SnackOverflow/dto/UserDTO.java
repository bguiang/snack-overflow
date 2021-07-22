package com.bernardguiang.SnackOverflow.dto;

import javax.validation.constraints.NotBlank;

import com.bernardguiang.SnackOverflow.model.User;

// Not used as part of a request since its usually grabbed inside the endpoint using the Authority
// Not yet used as a response
public class UserDTO {
	private Long id;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String username;
	
	@NotBlank
	private String fullName;
	
	@NotBlank
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