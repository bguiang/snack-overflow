package com.bernardguiang.SnackOverflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.AuthenticationResponse;
import com.bernardguiang.SnackOverflow.dto.RegisterRequest;
import com.bernardguiang.SnackOverflow.model.RefreshToken;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.UserRepository;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

@Service
public class AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenService refreshTokenService;
	private final JwtService jwtprovider;
	
	@Autowired
	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService, JwtService jwtprovider) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.refreshTokenService = refreshTokenService;
		this.jwtprovider = jwtprovider;
	}

	// Customer Signup
	public void customerSignup(RegisterRequest registerRequest) {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setRole(ApplicationUserRole.CUSTOMER.name());
		user.setFullName(registerRequest.getFullName());
		User saved = userRepository.save(user);
		System.out.println(saved);
	}
	
	// Refresh Token
	public AuthenticationResponse refreshToken(String refreshTokenString) {
		
		// Validate refresh token on db. Will throw runtime exception if invalid
		RefreshToken refreshToken = refreshTokenService.validateAndRetrieveRefreshToken(refreshTokenString);
		
		System.out.println("Validated Token: " + refreshToken.toString());
		
		// Generate new Access token
		String accessToken = jwtprovider.generateTokenWithUser(refreshToken.getUser());
		
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		authenticationResponse.setAuthenticationToken(accessToken);
		authenticationResponse.setUsername(refreshToken.getUser().getUsername());
		
		// Return Access Token and username in response object
		return authenticationResponse;
	}
	
	@EventListener(classes = { ContextRefreshedEvent.class})
	public void setDefaultUsers() {
		
		User user = new User();
		user.setUsername("bernard");
		user.setPassword(passwordEncoder.encode("password"));
		user.setRole(ApplicationUserRole.ADMIN.name());
		user.setFullName("Bernard Guiang");
		User saved = userRepository.save(user);
		System.out.println(saved);
	}
}
