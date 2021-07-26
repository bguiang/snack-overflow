package com.bernardguiang.SnackOverflow.service;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.security.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;



@Service
public class JwtService {
	
	private final JwtConfig jwtConfig;
	
	@Autowired
	public JwtService(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}
	
//	 Our token payload format
//	{
//		"sub": "bernard",
//		"authorities": [
//			{
//				"authority": "product:write"
//			},
//			{
//				"authority": "category:read"
//			},
//			{
//				"authority": "ROLE_ADMIN"
//			},
//			{
//				"authority": "category:write"
//			},
//			{
//				"authority": "product:read"
//			}
//		],
//		"iat": 1624931480,
//		"exp": 1626073200
//	}

	public String generateJwt(
			String username, 
			Collection<? extends GrantedAuthority> authorities,
			Date iat, //new Date()
			Date exp // Date.from(Instant.now().plusMillis(jwtConfig.getTokenExpirationMilliSeconds()))
	) {
		String token = Jwts.builder()
				.setSubject(username) //subject
				.claim("authorities", authorities)// body
				.setIssuedAt(iat) // iat
				.setExpiration(exp)	// exp
				.signWith(jwtConfig.getSecretKeyForSigning())
				.compact();
		
		return token;
	}
	
	public Claims getTokenPayload(String token) throws JwtException {
		Jws<Claims> claimsJws = Jwts.parser()
				.setSigningKey(jwtConfig.getSecretKeyForSigning()) // make sure this is exactly the same as in the JwtUsernameAndPasswordAuthenticationFilter
				.parseClaimsJws(token);
			
		// Get Body
		Claims body = claimsJws.getBody();
		return body;
	}
}
