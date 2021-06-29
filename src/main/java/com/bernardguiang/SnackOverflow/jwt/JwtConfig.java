package com.bernardguiang.SnackOverflow.jwt;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

	// load constants from application.properties file
	private String secretKey;
	private String tokenPrefix;
	private int tokenExpirationAfterDays;
	
	public JwtConfig() {

	}
	
	@Bean
	public SecretKey getSecretKeyForSigning() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
	
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getTokenPrefix() {
		return tokenPrefix;
	}
	public void setTokenPrefix(String tokenPrefix) {
		this.tokenPrefix = tokenPrefix;
	}
	public int getTokenExpirationAfterDays() {
		return tokenExpirationAfterDays;
	}
	public void setTokenExpirationAfterDays(int tokenExpirationAfterDays) {
		this.tokenExpirationAfterDays = tokenExpirationAfterDays;
	}
	
	
}
