package com.bernardguiang.SnackOverflow.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bernardguiang.SnackOverflow.dto.AuthenticationResponse;
import com.bernardguiang.SnackOverflow.dto.RefreshTokenRequest;
import com.bernardguiang.SnackOverflow.dto.RegisterRequest;
import com.bernardguiang.SnackOverflow.service.AuthService;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		authService.customerSignup(registerRequest);
		return new ResponseEntity<>("User Registration Successful", HttpStatus.CREATED);
	}
	
	// Make sure to delete the token from frontend
	// Token BlackListing but it defeats the purpose of using JWTs (stateless). 
	//  Storing tokens on the db and requiring  a lookup for authorization makes it stateful
	//	Howeever performance could be improved with an in-memory db
	public void Logout() {
		
	}

	@PostMapping("/refresh")
	public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		return null;
	}
}
