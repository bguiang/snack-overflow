package com.bernardguiang.SnackOverflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.AuthenticationResponse;
import com.bernardguiang.SnackOverflow.dto.RefreshTokenRequest;
import com.bernardguiang.SnackOverflow.dto.RegisterRequest;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.UserRepository;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

@Service
public class AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenService refreshTokenService;
	
	@Autowired
	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.refreshTokenService = refreshTokenService;
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
	public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		
		return null;
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
