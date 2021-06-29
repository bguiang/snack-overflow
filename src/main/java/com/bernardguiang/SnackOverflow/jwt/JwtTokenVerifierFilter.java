package com.bernardguiang.SnackOverflow.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JwtTokenVerifierFilter extends OncePerRequestFilter{
	
	private final JwtConfig jwtConfig;
	
	public JwtTokenVerifierFilter(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Get Token From Request Header
		String authorizationHeader = request.getHeader("Authorization");
		
		// Validate
		
		// ... check if token exists
		if(	authorizationHeader == null 
				|| authorizationHeader.isEmpty() 
				|| !authorizationHeader.startsWith("Bearer ")) 
		{
			// We don't reject here for the case where the User is getting his/her username and password authenticated to get a token back (login)
			
			// Pass on request and response to the next filter and initiate
			filterChain.doFilter(request, response);
			return;
		}
		
		// JWS is just a signed JWT
		String token = authorizationHeader.replace("Bearer ",  "");
		try {
			Jws<Claims> claimsJws = Jwts.parser()
				.setSigningKey(jwtConfig.getSecretKeyForSigning()) // make sure this is exactly the same as in the JwtUsernameAndPasswordAuthenticationFilter
				.parseClaimsJws(token);
			
			// Our token payload format
			//		{
			//			  "sub": "bernard",
			//			  "authorities": [
			//			    {
			//			      "authority": "product:write"
			//			    },
			//			    {
			//			      "authority": "category:read"
			//			    },
			//			    {
			//			      "authority": "ROLE_ADMIN"
			//			    },
			//			    {
			//			      "authority": "category:write"
			//			    },
			//			    {
			//			      "authority": "product:read"
			//			    }
			//			  ],
			//			  "iat": 1624931480,
			//			  "exp": 1626073200
			//		}
			
			// Get Body
			Claims body = claimsJws.getBody();
			// get Username
			String username = body.getSubject();
			// Get Authorities from the body
			List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");
			// Convert Authorities to SimpleGrantedAuthorities
			Set<SimpleGrantedAuthority> simpleGrantedAuthorities = 
					authorities.stream()
					.map(authMap -> new SimpleGrantedAuthority(authMap.get("authority")))
					.collect(Collectors.toSet());
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					username,
					null,
					simpleGrantedAuthorities
			);
			
			// Set this request as authenticated
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			// Pass on request and response to the next filter and initiate
			filterChain.doFilter(request, response);
			return;
			
		} catch(JwtException e) {
			throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
		}
	}

}
