package com.bernardguiang.SnackOverflow.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.AuthenticationResponse;
import com.bernardguiang.SnackOverflow.dto.RegisterRequest;
import com.bernardguiang.SnackOverflow.service.AuthService;
import com.bernardguiang.SnackOverflow.service.RefreshTokenService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private final AuthService authService;
	private final RefreshTokenService refreshTokenService;
	
	public AuthController(AuthService authService, RefreshTokenService refreshTokenService) {
		this.authService = authService;
		this.refreshTokenService = refreshTokenService;
	}
	
	// Login is handled by JwtUsernameAndPasswordAuthentication Filter
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		authService.customerSignup(registerRequest);
		return new ResponseEntity<>("User Registration Successful", HttpStatus.CREATED);
	}
	
	// Make sure to delete the token from frontend
	// Token BlackListing but it defeats the purpose of using JWTs (supposedly stateless). 
	//  Storing tokens on the db and requiring  a lookup for authorization makes it stateful
	//	However performance could be improved with an in-memory db
	public void Logout() {
		// Delete Refresh Token
		// remove current token from client
	}

	//TODO: IMPORTANT - when generating refresh token cookies, you have to set secure to false or it won't work without https
	//TODO: refresh tokens are currently mapped one-to-one with users which allows refreshing tokens only on a single device/browser. 
	//TODO: It might be possible to instead use a one-to-many relationship and track the devices that was used for login/refresh as a token parameter/attribute
	//TODO: also the current refresh tokens don't have an expiration date
	@GetMapping("/refresh")
	public AuthenticationResponse refreshTokens(
			HttpServletResponse response, 
			@CookieValue(name = "refresh-token", defaultValue = "") String refreshToken
			) {	
		System.out.println("TOKEN REFRESH: " + refreshToken);
		
		// Validate Token, generate new Access Token
		AuthenticationResponse authenticationResponse =  authService.refreshToken(refreshToken);
		
		// Generate new and update refresh token and store in an http only cookie
		Cookie refreshCookie = refreshTokenService.generateRefreshTokenCookie(authenticationResponse.getUsername());
		response.addCookie(refreshCookie);
		
		return authenticationResponse;
	}
}
