package com.bernardguiang.SnackOverflow.security;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.jsonwebtoken.security.Keys;

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig{// implements EnvironmentAware{

	// load constants from application.properties file
	private String secretKey;
	private String tokenPrefix;
	private long tokenExpirationMilliSeconds;
	
	private final Environment env;
	
	public JwtConfig(Environment env){
		this.env = env;
		this.secretKey = env.getProperty("jwt_secret_key");
	}
	
	@Bean
	public SecretKey getSecretKeyForSigning() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
	
	public String getTokenPrefix() {
		return tokenPrefix;
	}
	public void setTokenPrefix(String tokenPrefix) {
		this.tokenPrefix = tokenPrefix;
	}

	public long getTokenExpirationMilliSeconds() {
		return tokenExpirationMilliSeconds;
	}

	public void setTokenExpirationMilliSeconds(long tokenExpirationMilliSeconds) {
		this.tokenExpirationMilliSeconds = tokenExpirationMilliSeconds;
	}
}
