package com.bernardguiang.SnackOverflow.service;

import java.time.Instant;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.model.RefreshToken;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {
	
	private final RefreshTokenRepository refreshTokenRepository;
	private final UserService userService;
	
	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserService userService) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.userService = userService;
	}
	
	private RefreshToken generateRefreshToken(User user) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setCreatedDate(Instant.now());
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setUser(user);
		
		// User owns RefreshToken so we must save via User
		//return refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}
	
	private Cookie generateCookie(String refreshToken) {
		Cookie refreshCookie = new Cookie("refresh-token", refreshToken);
		refreshCookie.setMaxAge(86400);
		refreshCookie.setSecure(false);
		refreshCookie.setHttpOnly(true);
		refreshCookie.setPath("/api/v1/auth");
		
		return refreshCookie;
	}
	
	public Cookie generateRefreshTokenCookie(String username) {
		User user = userService.findUserByUsername(username);
		
		String tokenString = "";
		
		// User has no token - create
		if(user.getRefreshToken() == null) {
			RefreshToken refreshToken = generateRefreshToken(user);
			tokenString = refreshToken.getToken();
			user.setRefreshToken(refreshToken);
		}
		else { // User has an old token - update
			tokenString = UUID.randomUUID().toString();
			user.getRefreshToken().setToken(tokenString);
		}
		
		// Save refresh token through User
		userService.save(user);
		
		Cookie refreshCookie = generateCookie(tokenString);
		
		System.out.println("Generated Refresh Token: " + tokenString);
		
		return refreshCookie;
	}

	public RefreshToken validateAndRetrieveRefreshToken(String token) {
		return refreshTokenRepository.findByToken(token)
			.orElseThrow(() -> new IllegalStateException("Invalid refresh token: " + token));	
	}
	
	public void deleteRefreshToken(String token) {
		refreshTokenRepository.deleteByToken(token);
	}
}
