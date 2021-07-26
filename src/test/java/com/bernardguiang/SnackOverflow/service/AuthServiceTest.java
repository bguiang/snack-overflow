package com.bernardguiang.SnackOverflow.service;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;

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
	private JwtService jwtService;
	private JwtConfig jwtConfig;

	@BeforeEach
	void setUp() throws Exception {
		userRepository = Mockito.mock(UserRepository.class);
		passwordEncoder = Mockito.mock(PasswordEncoder.class);
		refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
		jwtService = Mockito.mock(JwtService.class);
		jwtConfig = Mockito.mock(JwtConfig.class);

		underTest = new AuthService(userRepository, passwordEncoder, refreshTokenRepository, jwtService, jwtConfig);
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

		when(userRepository.save(Mockito.argThat((User u) -> u.getEmail().equalsIgnoreCase(email)
				&& u.getUsername().equalsIgnoreCase(username) && u.getPassword().equalsIgnoreCase(encodedPassword)
				&& u.getFullName().equalsIgnoreCase(fullName) && u.getRole().equalsIgnoreCase(role))))
						.thenReturn(userSaved);

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
		assertThrows(IllegalStateException.class, () -> underTest.customerSignup(registerRequest),
				"An account with this username already exists");

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
		assertThrows(IllegalStateException.class, () -> underTest.customerSignup(registerRequest),
				"An account with this username already exists");

	}

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
		String accessTokenResult = "access token";
		when(jwtService.generateJwt(
				Mockito.eq(user.getUsername()),
				Mockito.argThat((Collection<? extends GrantedAuthority> authorities) -> (authorities
						.containsAll(ApplicationUserRole.CUSTOMER.getGrantedAuthorities())
						&& ApplicationUserRole.CUSTOMER.getGrantedAuthorities().containsAll(authorities))), 
				Mockito.any(), 
				Mockito.any())
			)
			.thenReturn(accessTokenResult);

		AuthenticationResponse response = underTest.refreshToken(refreshTokenString);

		// Then
		assertEquals(user.getUsername(), response.getUsername());
		assertEquals(accessTokenResult, response.getAuthenticationToken());
	}

	@Test
	void itShouldThowAnExceptionWhenRefreshTokenIsInvalid() {
		// Given
		String refreshTokenString = "refresh-token";

		// When
		Optional<RefreshToken> refreshTokenOptional = Optional.ofNullable(null);
		when(refreshTokenRepository.findByToken(Mockito.anyString())).thenReturn(refreshTokenOptional);

		// Then
		assertThrows(IllegalStateException.class, () -> underTest.refreshToken(refreshTokenString),
				"Invalid refresh token: " + refreshTokenString);
	}

	@Test
	void itShouldGenerateEmptyRefreshCookie() {
		// Given

		// When
		Cookie emptyCookie = underTest.generateEmptyRefreshTokenCookie();

		// Then
		assertEquals(null, emptyCookie.getValue());
		assertEquals("/api/v1/auth", emptyCookie.getPath());
		assertEquals(true, emptyCookie.isHttpOnly());
		// assertEquals(false, emptyCookie.getSecure()); This is non-secure when local
		// testing. But secure on production
	}

	@Test
	void itShouldGenerateRefreshTokenCookieWithAnExistingRefreshToken() {
		// Given
		ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
		String username = "myUsername";
		String refreshTokenString = "myRefreshToken";

		// When
		User user = new User();
		RefreshToken token = new RefreshToken();
		token.setToken(refreshTokenString);
		user.setRefreshToken(token);
		Optional<User> userOptional = Optional.of(user);
		when(userRepository.findByUsername(username)).thenReturn(userOptional);

		Cookie refreshCookie = underTest.generateRefreshTokenCookie(username);
		Mockito.verify(userRepository).save(userArgumentCaptor.capture());
		User updatedUser = userArgumentCaptor.getValue();
		RefreshToken updatedRefreshToken = updatedUser.getRefreshToken();

		// Then
		assertEquals(updatedRefreshToken.getToken(), refreshCookie.getValue());
		assertEquals("/api/v1/auth", refreshCookie.getPath());
		assertEquals(true, refreshCookie.isHttpOnly());
		// assertEquals(false, emptyCookie.getSecure()); This is non-secure when local
		// testing. But secure on production
	}

	@Test
	void itShouldGenerateRefreshTokenCookieWithoutAnExistingRefreshToken() {
		// Given
		ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
		String username = "myUsername";
		String refreshTokenString = "myRefreshToken";

		// When
		User user = new User();
		RefreshToken token = new RefreshToken();
		Optional<User> userOptional = Optional.of(user);
		when(userRepository.findByUsername(username)).thenReturn(userOptional);

		Cookie refreshCookie = underTest.generateRefreshTokenCookie(username);
		Mockito.verify(userRepository).save(userArgumentCaptor.capture());
		User updatedUser = userArgumentCaptor.getValue();
		RefreshToken updatedRefreshToken = updatedUser.getRefreshToken();

		// Then
		assertEquals(updatedRefreshToken.getToken(), refreshCookie.getValue());
		assertEquals("/api/v1/auth", refreshCookie.getPath());
		assertEquals(true, refreshCookie.isHttpOnly());
		// assertEquals(false, emptyCookie.getSecure()); This is non-secure when local
		// testing. But secure on production
	}

	@Test
	void itShouldThrowAnExceptionWhenUsernameDoesNotExist() {
		// Given
		String username = "myUsername";

		// When
		Optional<User> userOptional = Optional.ofNullable(null);
		when(userRepository.findByUsername(username)).thenReturn(userOptional);

		// Then
		assertThrows(IllegalStateException.class, () -> underTest.generateRefreshTokenCookie(username),
				"Could not find user: " + username);
	}

}
