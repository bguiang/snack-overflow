package com.bernardguiang.SnackOverflow.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.StatsRequest;
import com.bernardguiang.SnackOverflow.dto.request.UserPage;
import com.bernardguiang.SnackOverflow.dto.response.FullUserDTO;
import com.bernardguiang.SnackOverflow.dto.response.UserStatsResponse;
import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.RefreshToken;
import com.bernardguiang.SnackOverflow.model.ShippingDetails;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;
import com.bernardguiang.SnackOverflow.service.ApplicationUserDetailsService;
import com.bernardguiang.SnackOverflow.service.AuthService;
import com.bernardguiang.SnackOverflow.service.JwtService;
import com.bernardguiang.SnackOverflow.service.UserService;

import io.jsonwebtoken.Claims;

@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {

	@MockBean
	private UserService userService;

	// JWT Auth Filter Dependencies
	@MockBean
	private AuthService authService;
	@MockBean
	private PasswordEncoder passwordEncoder;
	@MockBean
	private ApplicationUserDetailsService applicationUserDetailsService;
	@MockBean
	private JwtService jwtService;

	@Autowired
	private MockMvc mockMvc;

	private static List<Map<String, String>> adminRoleAuthorities;

	private final Long adminId = 1L;
	private final String adminUsername = "adminUsername";
	private final String adminEmail = "admin@email.com";
	private final String adminRole = ApplicationUserRole.ADMIN.name();
	private final String adminAuthToken = "adminAuthToken";

	@BeforeAll
	static void setupBeforeAll() {
		adminRoleAuthorities = new ArrayList<>();

		Map<String, String> adminAuth1 = new HashMap<>();
		adminAuth1.put("authority", "ROLE_ADMIN");
		adminRoleAuthorities.add(adminAuth1);
		Map<String, String> adminAuth2 = new HashMap<>();
		adminAuth2.put("authority", "order:write");
		adminRoleAuthorities.add(adminAuth2);
		Map<String, String> adminAuth3 = new HashMap<>();
		adminAuth3.put("authority", "category:read");
		adminRoleAuthorities.add(adminAuth3);
		Map<String, String> adminAuth4 = new HashMap<>();
		adminAuth4.put("authority", "product:read");
		adminRoleAuthorities.add(adminAuth4);
		Map<String, String> adminAuth5 = new HashMap<>();
		adminAuth5.put("authority", "order:read");
		adminRoleAuthorities.add(adminAuth5);

		Map<String, String> adminAuth6 = new HashMap<>();
		adminAuth6.put("authority", "product:write");
		adminRoleAuthorities.add(adminAuth6);
		Map<String, String> adminAuth7 = new HashMap<>();
		adminAuth7.put("authority", "category:write");
		adminRoleAuthorities.add(adminAuth7);
	}

	void setAdminAuthorization() {
		// ... Handle token authorization via filter
		Claims tokenPayload = Mockito.mock(Claims.class);
		when(tokenPayload.getSubject()).thenReturn(adminUsername);
		when(tokenPayload.get("authorities")).thenReturn(adminRoleAuthorities);
		when(jwtService.getTokenPayload(adminAuthToken)).thenReturn(tokenPayload);
	}

	@Test
	void getUsersPaginated() throws Exception {
		// Given
		UserPage userPage = new UserPage();
		userPage.setSearch("admin");
		List<UserDTO> results = new ArrayList<>();
		UserDTO dto = new UserDTO();
		dto.setFullName("Admin Guy");
		results.add(dto);
		Page<UserDTO> resultPage = new PageImpl<UserDTO>(results);

		// When
		setAdminAuthorization();
		when(userService.findUsersPaginated(Mockito.argThat(new ArgumentMatcher<UserPage>() {

			@Override
			public boolean matches(UserPage argument) {
				return argument.getPageNumber() == 0 && argument.getPageSize() == 9
						&& argument.getSearch().equals("admin");
			}
		}))).thenReturn(resultPage);

		// Then
		mockMvc.perform(get("/api/v1/admin/users").param("search", "admin")
				.header("Authorization", "Bearer " + adminAuthToken).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$.content").isNotEmpty())
				.andExpect(jsonPath("$.content[0].fullName").value("Admin Guy"));
	}

	@Test
	void getUser() throws Exception {
		// Given
		Long id = 1L;
		String email = "user@user.com";
		String username = "user123";
		String password = "Password123!";
		String fullName = "User FullName";
		String role = ApplicationUserRole.CUSTOMER.name();
		RefreshToken refreshToken = null;
		Instant joinDate = Instant.now();

		String addressLine1 = "Address Line 1";
		String addressLine2 = null;
		String city = "city";
		String state = "state";
		String postalCode = "postal code";
		String country = "country";
		Address address = new Address(addressLine1, addressLine2, city, state, postalCode, country);

		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(password);
		user.setFullName(fullName);
		user.setRole(role);
		user.setAddress(address);
		user.setRefreshToken(refreshToken);
		user.setJoinDate(joinDate);
		user.setAddress(address);

		Order order = new Order();
		order.setId(1L);
		order.setTotal(new BigDecimal(20));
		order.setCreatedDate(Instant.now());
		order.setShippingSameAsBilling(false);
		order.setStatus(OrderStatus.COMPLETED);

		List<OrderItem> items = new ArrayList<>();
		OrderItem item1 = new OrderItem();
		Product product1 = new Product();
		product1.setId(1L);
		item1.setProduct(product1);
		OrderItem item2 = new OrderItem();
		Product product2 = new Product();
		product2.setId(2L);
		item2.setProduct(product2);
		items.add(item1);
		items.add(item2);
		order.setItems(items);
		order.setUser(user);

		BillingDetails billingDetails = new BillingDetails();
		billingDetails.setOrder(order);
		order.setBillingDetails(billingDetails);

		ShippingDetails shippingDetails = new ShippingDetails();
		shippingDetails.setOrder(order);
		order.setShippingDetails(shippingDetails);

		List<Order> orders = new ArrayList<>();
		orders.add(order);
		user.setOrders(orders);

		FullUserDTO fullUserDTO = new FullUserDTO(user);
		
		// When
		setAdminAuthorization();
		when(userService.findById(1L)).thenReturn(fullUserDTO);

		// Then
		mockMvc.perform(get("/api/v1/admin/users/1")
				.header("Authorization", "Bearer " + adminAuthToken).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$.fullName").value(fullName));
	}
	
	@Test
	void getNewUsers() throws Exception {
		// Given
		UserStatsResponse userStats = new UserStatsResponse(20);

		// When
		setAdminAuthorization();
		when(userService.getUserStats(Mockito.argThat(new ArgumentMatcher<StatsRequest>() {

			@Override
			public boolean matches(StatsRequest argument) {
				return argument.getRange().equals("all");
			}
		}))).thenReturn(userStats);

		// Then
		mockMvc.perform(get("/api/v1/admin/users/stats").param("range", "all")
				.header("Authorization", "Bearer " + adminAuthToken).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$.newUsers").value(20));
	}
}
