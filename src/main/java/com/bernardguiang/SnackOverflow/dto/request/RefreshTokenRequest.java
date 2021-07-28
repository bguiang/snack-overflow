package com.bernardguiang.SnackOverflow.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RefreshTokenRequest {
	@NotBlank
	private String refreshToken;
	
	@NotNull(message = "Username cannot be null")
	@Size(min = 6, max = 15, message = "Username must be at least 6 characters and cannot be longer than 15 characters")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message="Username must consist of letters an numbers only")
	private String username;

	public RefreshTokenRequest() {

	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
