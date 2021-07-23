package com.bernardguiang.SnackOverflow.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.request.RegisterRequest;
import com.bernardguiang.SnackOverflow.dto.response.AuthenticationResponse;
import com.bernardguiang.SnackOverflow.model.RefreshToken;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.RefreshTokenRepository;
import com.bernardguiang.SnackOverflow.repository.UserRepository;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;
import com.bernardguiang.SnackOverflow.security.JwtConfig;

import io.jsonwebtoken.Jwts;

@Service
public class AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtConfig jwtConfig;
	
	@Autowired
	public AuthService(
			UserRepository userRepository, 
			PasswordEncoder passwordEncoder, 
			RefreshTokenRepository refreshTokenRepository, 
			JwtConfig jwtConfig) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.refreshTokenRepository = refreshTokenRepository;
		this.jwtConfig = jwtConfig;
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
		RefreshToken refreshToken = validateAndRetrieveRefreshToken(refreshTokenString);
		
		System.out.println("Validated Token: " + refreshToken.toString());
		
		// Generate new Access token
		String accessToken = generateJwtWithUser(refreshToken.getUser());
		
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		authenticationResponse.setAuthenticationToken(accessToken);
		authenticationResponse.setUsername(refreshToken.getUser().getUsername());
		
		// Return Access Token and username in response object
		return authenticationResponse;
	}
	
	public String generateJwt(String username, Collection<? extends GrantedAuthority> authorities) {
		String token = Jwts.builder()
				.setSubject(username) //subject
				.claim("authorities", authorities)// body
				.setIssuedAt(new Date()) // iat
				.setExpiration(Date.from(Instant.now().plusMillis(jwtConfig.getTokenExpirationMilliSeconds())))	// exp
				.signWith(jwtConfig.getSecretKeyForSigning()) // make sure this is exact same as in the JwtTokenVerifierFilter
				.compact();
		
		return token;
	}
	
	public String generateJwtWithUser(User user) {
		
		// Query db for user to get user roles
		Set<GrantedAuthority> authorities = null;
		
		List<ApplicationUserRole> roles = Arrays.asList(ApplicationUserRole.values());
		for(ApplicationUserRole role : roles) {
			if(role.name().equalsIgnoreCase(user.getRole())) {
				authorities = role.getGrantedAuthorities();
			}
		}

		return generateJwt(user.getUsername(), authorities);
	}
	
	public Cookie generateEmptyRefreshTokenCookie() {
		Cookie emptyRefreshCookie = new Cookie("refresh-token", null);
		emptyRefreshCookie.setSecure(false);
		emptyRefreshCookie.setHttpOnly(true);
		emptyRefreshCookie.setPath("/api/v1/auth");
		
		return emptyRefreshCookie;
	}
	
	public Cookie generateRefreshTokenCookie(String username) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new IllegalStateException("Could not find user: " + username));
		
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
		userRepository.save(user);
		
		Cookie refreshCookie = generateCookie(tokenString);
		
		System.out.println("Generated Refresh Token: " + tokenString);
		
		return refreshCookie;
	}
	
	private RefreshToken validateAndRetrieveRefreshToken(String token) {
		return refreshTokenRepository.findByToken(token)
			.orElseThrow(() -> new IllegalStateException("Invalid refresh token: " + token));	
	}
	
	private RefreshToken generateRefreshToken(User user) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setCreatedDate(Instant.now());
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setUser(user);

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
	
	@EventListener(classes = { ContextRefreshedEvent.class})
	public void setDefaultUsers() {
		
		User user = new User();
		user.setUsername("bernard");
		user.setEmail("bernardguiang@gmail.com");
		user.setPassword(passwordEncoder.encode("password"));
		user.setRole(ApplicationUserRole.ADMIN.name());
		user.setFullName("Bernard Guiang");
		User saved = userRepository.save(user);
		System.out.println(saved);
	}
}
