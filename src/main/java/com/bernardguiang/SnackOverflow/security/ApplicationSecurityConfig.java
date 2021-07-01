package com.bernardguiang.SnackOverflow.security;

import static com.bernardguiang.SnackOverflow.security.ApplicationUserPermission.*;
import static com.bernardguiang.SnackOverflow.security.ApplicationUserRole.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.bernardguiang.SnackOverflow.auth.ApplicationUserDetailsService;
import com.bernardguiang.SnackOverflow.jwt.JwtConfig;
import com.bernardguiang.SnackOverflow.jwt.JwtProvider;
import com.bernardguiang.SnackOverflow.jwt.JwtTokenVerifierFilter;
import com.bernardguiang.SnackOverflow.jwt.JwtUsernameAndPasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	private final PasswordEncoder passwordEncoder;
	private final ApplicationUserDetailsService applicationUserService;
	private final JwtProvider jwtProvider;
	private final JwtConfig jwtConfig;
	
	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserDetailsService applicationUserService, JwtProvider jwtProvider, JwtConfig jwtConfig) {
		this.passwordEncoder = passwordEncoder;
		this.applicationUserService = applicationUserService;
		this.jwtProvider = jwtProvider;
		this.jwtConfig = jwtConfig;
	}
	
	// Authentication vs Authorization
	// - Authentication: Who are you?
	// - Authorization: What do you want? Are you allowed?
	
	// Authentication
	// - there are many ways to implement authentication. 
	// - HTTP Basic or basic authentication is the simplest and just passes the username and password as base64 on every request and you can't logout
	// - Form based authentication is the standard in most websites.
	// 		- client submits username and password via form post and server returns a cookie with the session ID
	//		- when making requests, the session id is passed and the server validates if the session id is still valid
	//		- HTTPS is recommended
	// - JSON Web Token or JWT
	//		- fast
	//		- stateless. no sessions
	//		- used across many services
	//		- compromised secret key (very bad)
	//		- no visibility to logged in users
	//		- token can be stolen
	//		- to verify credentials for authentication and the JWT, we use Request Filters
	//			- When a request is sent to the server, it goes through the Request Filters first before it is even passed into the API endpoint
	//			- For this, we extend existing request filters and add them into the configuration via .addFilter(Filter)
	//		- steps:
	//			1. Client sends credentials to server
	//			2. Server validates credentials then sends back JWT (UsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter)
	//			3. Client sends token for each request
	//			4. Server validates token (JwtTokenVerifierFilter extends OncePerRequestFilter)
	//			5. Server sends response to client
	// TODO: maybe add token refresh, store tokens and invalidate when user re-authenticates
	
	// Authorization
	// - once the user is authenticated, now we need to know if they have access to what they want to access
	// - Spring Security provides 2 ways to handle authorization/access control (what can a user access)
	
	// Role-Based and Permission-Based Authorization/Access Control
	// - Role-Based access control is done by matching the roles to specific urls, methods, and method types
	// - Permission-Based access is done by matching the permissions (of each role) to specific urls, methods, and method types
	// There are TWO methods to implement Authorization and Access Control with Spring Security
	//		- Set up antMatcher with .antmatcher(HttypMethod.type:optional, StringURL)
	//		- Followed by
	//			- .hasRole("ROLE), hasAnyRole("ROLE1", "ROLE2"), hasAuthority("PERMISSION")
	// 		- IMPORTANT: Order matters when implementing these antmatchers and role/authority matchers
	//			- Lets say you have 2 sets of matchers for the same url, one for matching the role type and one for matching the permissions
	//			  	if you match the role first, it will immediately approve the request and not need to go to the next matcher 
	//				to match the permissions (read/write). So even if the role doesn't have the permission to do a write on that url, it will still work
	// - The Second method is access control on the method level
	//		- To apply access control on the method level we add the @PreAuthorize annotation before the request methods in the RestController
	//  	- But we need to add @EnableGlobalMethodSecurity(prePostEnabled = true) annotation to the security configuration file (this file) to enable @PreAuthorize
	//		- The @PreAuthorize takes a string parameter for either role- or permission-based access control
	//			- hasRole('ROLE_') hasAnyRole('ROLE_') hasAuthority('permission') hasAnyAuthority('permission')
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		
			// X-XSRF-TOKEN cookie (not readable by JS) will be returned and you need to pass it in with your requests
			// Attachment should be automatic with Axios
			//.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) 
			//.and()
		
			// Disable CSRF protection provided by default by spring security
			.csrf().disable() // csrf attacks mainly happen when there are sessions and when using cookies for authentication
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWTs are stateless
			.and() // then add JWT Authentication by UsernamePasswordAuthenticationFilter created
			.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtProvider))
			.addFilterAfter(new JwtTokenVerifierFilter(jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class) // username/password check first before trying to verify token
			.authorizeRequests() // we want to authorize requests
			.antMatchers("/", "index", "/css/*", "/js/**")
				.permitAll()	// permit matched patterns above
			.antMatchers(HttpMethod.GET, "/api/v1/products/**")
				.permitAll()
			.antMatchers("/api/v1/auth/**") //signup, refresh, signout
				.permitAll()
			.antMatchers("/api/v1/users/**") // TODO: remove this later
				.permitAll()
			.anyRequest()	// any request (secures all routes)
			.authenticated(); // must be authenticated
			//.and()
			//.httpBasic();	// Basic Authentication or HTTP Basic
			//.formLogin(); // Form authentication
			// replaced these Authentication with and Authentication Filter for JWT Authentication (JwtUsernamePasswordAuthenticationFilter) before .authorizeRequests()
	}	
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(applicationUserService);
		return provider;
	}
	
	@Bean
	public JwtConfig configureJwt() {
		return new JwtConfig();
	}
}
