package com.bernardguiang.SnackOverflow.service;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;
import com.bernardguiang.SnackOverflow.security.JwtConfig;

import io.jsonwebtoken.Jwts;

@Service
public class JwtService {
	
	private final JwtConfig jwtConfig;
	
	@Autowired
	public JwtService(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}
	
	public String generateToken(Authentication authentication) {
		String token = Jwts.builder()
				.setSubject(authentication.getName()) //subject
				.claim("authorities", authentication.getAuthorities())// body
				.setIssuedAt(new Date()) // iat
				.setExpiration(Date.from(Instant.now().plusMillis(jwtConfig.getTokenExpirationMilliSeconds())))	// exp
				.signWith(jwtConfig.getSecretKeyForSigning()) // make sure this is exact same as in the JwtTokenVerifierFilter
				.compact();
		
		return token;
	}
	
	//TODO: how to generate new token from token refresh that contains roles/authorities? Currently no access to user info unless we do a manual lookup which defeats the purpose of jwt
	public String generateTokenWithUser(User user) {
		
		// Query db for user to get user roles
		Set<GrantedAuthority> authorities = null;
		if(user.getRole().equalsIgnoreCase("ADMIN")) {
			authorities = ApplicationUserRole.ADMIN.getGrantedAuthorities();
		} else if(user.getRole().equalsIgnoreCase("CUSTOMER")) {
			authorities = ApplicationUserRole.CUSTOMER.getGrantedAuthorities();
		}
		
		String token = Jwts.builder()
				.setSubject(user.getUsername()) //subject
				.claim("authorities", authorities)// body
				.setIssuedAt(new Date()) // iat
				.setExpiration(Date.from(Instant.now().plusMillis(jwtConfig.getTokenExpirationMilliSeconds())))	// exp
				.signWith(jwtConfig.getSecretKeyForSigning()) // make sure this is exact same as in the JwtTokenVerifierFilter
				.compact();
		
		return token;
	}

}
