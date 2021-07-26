package com.bernardguiang.SnackOverflow.service;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.security.JwtConfig;

import io.jsonwebtoken.Jwts;

@Service
public class JwtService {
	
	private final JwtConfig jwtConfig;
	
	@Autowired
	public JwtService(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	public String generateJwt(String username, Collection<? extends GrantedAuthority> authorities) {
		String token = Jwts.builder()
				.setSubject(username) //subject
				.claim("authorities", authorities)// body
				.setIssuedAt(new Date()) // iat
				.setExpiration(Date.from(Instant.now().plusMillis(jwtConfig.getTokenExpirationMilliSeconds())))	// exp
				.signWith(jwtConfig.getSecretKeyForSigning())
				.compact();
		
		return token;
	}
	

}
