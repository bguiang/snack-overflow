package com.bernardguiang.SnackOverflow.jwt;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

@Service
public class JwtProvider {
	
	private final JwtConfig jwtConfig;
	
	@Autowired
	public JwtProvider(JwtConfig jwtConfig) {
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
//	public String generateTokenWithUsername(String username) {
//		String token = Jwts.builder()
//				.setSubject(username) //subject
//				.claim("authorities", authentication.getAuthorities())// body
//				.setIssuedAt(new Date()) // iat
//				.setExpiration(Date.from(Instant.now().plusMillis(jwtConfig.getTokenExpirationMilliSeconds())))	// exp
//				.signWith(jwtConfig.getSecretKeyForSigning()) // make sure this is exact same as in the JwtTokenVerifierFilter
//				.compact();
//		
//		return token;
//	}

}
