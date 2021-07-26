package com.bernardguiang.SnackOverflow.service;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.RegisterRequest;
import com.bernardguiang.SnackOverflow.dto.response.AuthenticationResponse;
import com.bernardguiang.SnackOverflow.model.RefreshToken;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.RefreshTokenRepository;
import com.bernardguiang.SnackOverflow.repository.UserRepository;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;
import com.bernardguiang.SnackOverflow.security.JwtConfig;

import io.jsonwebtoken.security.Keys;

class AuthServiceTest {

	private AuthService underTest;
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private RefreshTokenRepository refreshTokenRepository;
	private JwtConfig jwtConfig;

	@Captor
	private ArgumentCaptor<User> userArgumentCaptor;
	
	@BeforeEach
	void setUp() throws Exception {
		userRepository = Mockito.mock(UserRepository.class);
		passwordEncoder = Mockito.mock(PasswordEncoder.class);
		refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
		jwtConfig = Mockito.mock(JwtConfig.class);
		
		underTest = new AuthService(userRepository, passwordEncoder, refreshTokenRepository, jwtConfig);
	}

	@Test
	void itShouldSignUpCustomer() {
		// Given
		
		String email = "my@email.com";
		String fullName = "My Name";
		String password = "MyPassword123!";
		String username = "myUsername123";
		
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setEmail(email);
		registerRequest.setFullName(fullName);
		registerRequest.setPassword(password);
		registerRequest.setUsername(username);
		
		String encodedPassword = "Encoded-Password";
		String role = ApplicationUserRole.CUSTOMER.name();
		
		// When
		Optional<User> userCheckOptional = Optional.ofNullable(null);
		Optional<User> emailCheckOptional = Optional.ofNullable(null);
		when(userRepository.findByUsername(Mockito.any())).thenReturn(userCheckOptional);
		when(userRepository.findByEmail(Mockito.any())).thenReturn(emailCheckOptional);
		
		when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
		
		Long savedId = 5L;
		User userSaved = new User();
		userSaved.setId(savedId);
		userSaved.setUsername(registerRequest.getUsername());
		userSaved.setPassword(encodedPassword);
		userSaved.setRole(ApplicationUserRole.CUSTOMER.name());
		userSaved.setFullName(registerRequest.getFullName());
		userSaved.setEmail(email);
		
		when(userRepository.save(Matchers.argThat((User u) -> 
				u.getEmail().equalsIgnoreCase(email)
				&& u.getUsername().equalsIgnoreCase(username)
				&& u.getPassword().equalsIgnoreCase(encodedPassword)
				&& u.getFullName().equalsIgnoreCase(fullName)
				&& u.getRole().equalsIgnoreCase(role)
				))).thenReturn(userSaved);
		
		// Then
		UserDTO response = underTest.customerSignup(registerRequest);
		assertEquals(savedId, response.getId());
		assertEquals(email, response.getEmail());
		assertEquals(fullName, response.getFullName());
		assertEquals(username, response.getUsername());
		assertEquals(ApplicationUserRole.CUSTOMER.name(), response.getRole());
	}
	
	@Test
	void itShouldThrowAnExceptionIfUsernameAlreadyExists() {
		// Given
		RegisterRequest registerRequest = new RegisterRequest();
		
		
		// When
		User user = new User();
		Optional<User> userCheckOptional = Optional.ofNullable(user);
		Optional<User> emailCheckOptional = Optional.ofNullable(null);
		when(userRepository.findByUsername(Mockito.any())).thenReturn(userCheckOptional);
		when(userRepository.findByEmail(Mockito.any())).thenReturn(emailCheckOptional);
		
		// Then
		assertThrows(
				IllegalStateException.class,
				()->underTest.customerSignup(registerRequest),
				"An account with this username already exists"
		);
		
	}
	
	@Test
	void itShouldThrowAnExceptionIfEmailAlreadyExists() {
		// Given
		RegisterRequest registerRequest = new RegisterRequest();
		
		
		// When
		User user = new User();
		Optional<User> userCheckOptional = Optional.ofNullable(null);
		Optional<User> emailCheckOptional = Optional.ofNullable(user);
		when(userRepository.findByUsername(Mockito.any())).thenReturn(userCheckOptional);
		when(userRepository.findByEmail(Mockito.any())).thenReturn(emailCheckOptional);
		
		// Then
		assertThrows(
				IllegalStateException.class,
				()->underTest.customerSignup(registerRequest),
				"An account with this username already exists"
		);
		
	}
	
	@Captor
	ArgumentCaptor<Collection<? extends GrantedAuthority>> authoritiesCaptor;
	
	@Captor
	ArgumentCaptor<String> usernameCaptor;
	
	@Test
	void itShouldRefreshToken() {
		// Given
		String refreshTokenString = "refresh-token";
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(refreshTokenString);
		User user = new User();
		user.setRole(ApplicationUserRole.CUSTOMER.name());
		user.setUsername("username");
		refreshToken.setUser(user);
		
		// When
		Optional<RefreshToken> refreshTokenOptional = Optional.ofNullable(refreshToken);
		when(refreshTokenRepository.findByToken(refreshTokenString)).thenReturn(refreshTokenOptional);
		when(jwtConfig.getTokenExpirationMilliSeconds()).thenReturn(1000L);
		String secret = "shhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";
		SecretKey sk = Keys.hmacShaKeyFor(secret.getBytes());
		when(jwtConfig.getSecretKeyForSigning()).thenReturn(sk);
		
		Mockito.verify(underTest).generateJwt(usernameCaptor.capture(), authoritiesCaptor.capture());
		String usernameUsed =  usernameCaptor.getValue();
		Collection<? extends GrantedAuthority> authoritiesUsed = authoritiesCaptor.capture();
		
		
		// Then
		AuthenticationResponse response = underTest.refreshToken(refreshTokenString);
		//TODO:set up argumentcaptor and assess the properties used in the token builder
		assertEquals(user.getUsername(), response.getUsername());
	}

}
