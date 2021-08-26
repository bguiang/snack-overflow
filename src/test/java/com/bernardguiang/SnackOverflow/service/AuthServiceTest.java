package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

class AuthServiceTest {

	private AuthService underTest;

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private RefreshTokenRepository refreshTokenRepository;
	private JwtService jwtService;

	@BeforeEach
	void setUp() throws Exception {
		userRepository = Mockito.mock(UserRepository.class);
		passwordEncoder = Mockito.mock(PasswordEncoder.class);
		refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
		jwtService = Mockito.mock(JwtService.class);

		underTest = new AuthService(userRepository, passwordEncoder, refreshTokenRepository, jwtService);
	}

	@Test
	void itShouldSignUpCustomer() {
		// Given
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setEmail("my@email.com");
		registerRequest.setFullName("My Name");
		registerRequest.setPassword("MyPassword123!");
		registerRequest.setUsername("myUsername123");

		String role = ApplicationUserRole.CUSTOMER.name();

		// When
		Optional<User> userCheckOptional = Optional.ofNullable(null);
		Optional<User> emailCheckOptional = Optional.ofNullable(null);
		when(userRepository.findByUsername(Mockito.any())).thenReturn(userCheckOptional);
		when(userRepository.findByEmail(Mockito.any())).thenReturn(emailCheckOptional);

		when(passwordEncoder.encode("MyPassword123!")).thenReturn("Encoded-Password");

		User userSaved = new User();
		userSaved.setId(5L);
		userSaved.setUsername(registerRequest.getUsername());
		userSaved.setPassword("Encoded-Password");
		userSaved.setRole(ApplicationUserRole.CUSTOMER.name());
		userSaved.setFullName(registerRequest.getFullName());
		userSaved.setEmail("my@email.com");

		when(userRepository.save(Mockito.argThat((User u) -> u.getEmail().equalsIgnoreCase("my@email.com")
				&& u.getUsername().equalsIgnoreCase("myUsername123") && u.getPassword().equalsIgnoreCase("Encoded-Password")
				&& u.getFullName().equalsIgnoreCase("My Name") && u.getRole().equalsIgnoreCase(role))))
						.thenReturn(userSaved);

		// Then
		UserDTO response = underTest.customerSignup(registerRequest);
		assertEquals(5L, response.getId());
		assertEquals("my@email.com", response.getEmail());
		assertEquals("My Name", response.getFullName());
		assertEquals("myUsername123", response.getUsername());
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
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken("refresh-token");
		User user = new User();
		user.setRole(ApplicationUserRole.CUSTOMER.name());
		user.setUsername("username");
		refreshToken.setUser(user);

		// When
		Optional<RefreshToken> refreshTokenOptional = Optional.ofNullable(refreshToken);
		when(refreshTokenRepository.findByToken("refresh-token")).thenReturn(refreshTokenOptional);
		when(jwtService.generateJwt(
				Mockito.eq(user.getUsername()),
				Mockito.argThat((Collection<? extends GrantedAuthority> authorities) -> (authorities
						.containsAll(ApplicationUserRole.CUSTOMER.getGrantedAuthorities())
						&& ApplicationUserRole.CUSTOMER.getGrantedAuthorities().containsAll(authorities))), 
				Mockito.any())
			)
			.thenReturn("access token");

		AuthenticationResponse response = underTest.refreshToken("refresh-token");

		// Then
		assertEquals(user.getUsername(), response.getUsername());
		assertEquals("access token", response.getAuthenticationToken());
	}

	@Test
	void itShouldThowAnExceptionWhenRefreshTokenIsInvalid() {
		// Given
		// When
		Optional<RefreshToken> refreshTokenOptional = Optional.ofNullable(null);
		when(refreshTokenRepository.findByToken(Mockito.anyString())).thenReturn(refreshTokenOptional);

		// Then
		assertThrows(IllegalStateException.class, () -> underTest.refreshToken("refresh-token"),
				"Invalid refresh token: " + "refresh-token");
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

		// When
		User user = new User();
		RefreshToken token = new RefreshToken();
		token.setToken("myRefreshToken");
		user.setRefreshToken(token);
		Optional<User> userOptional = Optional.of(user);
		when(userRepository.findByUsername("myUsername")).thenReturn(userOptional);

		Cookie refreshCookie = underTest.generateRefreshTokenCookie("myUsername");
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

		// When
		User user = new User();
		RefreshToken token = new RefreshToken();
		Optional<User> userOptional = Optional.of(user);
		when(userRepository.findByUsername("myUsername")).thenReturn(userOptional);

		Cookie refreshCookie = underTest.generateRefreshTokenCookie("myUsername");
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
		// When
		Optional<User> userOptional = Optional.ofNullable(null);
		when(userRepository.findByUsername("myUsername")).thenReturn(userOptional);

		// Then
		assertThrows(IllegalStateException.class, () -> underTest.generateRefreshTokenCookie("myUsername"),
				"Could not find user: " + "myUsername");
	}

}
