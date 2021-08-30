package com.bernardguiang.SnackOverflow.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.request.CartRequest;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponseItem;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;
import com.bernardguiang.SnackOverflow.service.ApplicationUserDetailsService;
import com.bernardguiang.SnackOverflow.service.AuthService;
import com.bernardguiang.SnackOverflow.service.CartService;
import com.bernardguiang.SnackOverflow.service.JwtService;
import com.bernardguiang.SnackOverflow.service.StripeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;

import io.jsonwebtoken.Claims;

@WebMvcTest(StripeController.class)
class StripeControllerIntegrationTest {
	
	// StripeController Dependencies
	@MockBean
	private StripeService stripeService;
	@MockBean
	private CartService cartService;
	
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
	
	private static List<Map<String, String>> customerRoleAuthorities;

	private final Long customerId = 5L;
	private final String customerUsername = "customerUsername";
	private final String customerEmail = "customer@email.com";
	private final String customerRole = ApplicationUserRole.CUSTOMER.name();
	private final String customerAuthToken = "customerAuthToken";

	@BeforeAll
	static void setupBeforeAll() {
		customerRoleAuthorities = new ArrayList<>();
		
		Map<String, String> customerAuth1 = new HashMap<>();
		customerAuth1.put("authority", "ROLE_CUSTOMER");
		customerRoleAuthorities.add(customerAuth1);
		Map<String, String> customerAuth2 = new HashMap<>();
		customerAuth2.put("authority", "order:write");
		customerRoleAuthorities.add(customerAuth2);
		Map<String, String> customerAuth3 = new HashMap<>();
		customerAuth3.put("authority", "category:read");
		customerRoleAuthorities.add(customerAuth3);
		Map<String, String> customerAuth4 = new HashMap<>();
		customerAuth4.put("authority", "product:read");
		customerRoleAuthorities.add(customerAuth4);
		Map<String, String> customerAuth5 = new HashMap<>();
		customerAuth5.put("authority", "order:read");
		customerRoleAuthorities.add(customerAuth5);
	}

	void setCustomerAuthorization() {
		// ... Handle token authorization via filter
		Claims tokenPayload = Mockito.mock(Claims.class);
		when(tokenPayload.getSubject()).thenReturn(customerUsername);
		when(tokenPayload.get("authorities")).thenReturn(customerRoleAuthorities);
		when(jwtService.getTokenPayload(customerAuthToken)).thenReturn(tokenPayload);
	}
	
	@Test
	void createPaymentIntent() throws Exception {
		// Given
		CartRequest cartRequest = new CartRequest();
		List<CartInfoRequestItem> cartInfoRequestItems = new ArrayList<>();
		CartInfoRequestItem item = new CartInfoRequestItem();
		item.setProductId(1L);
		item.setQuantity(1);
		cartInfoRequestItems.add(item);
		cartRequest.setItems(cartInfoRequestItems);
		
		Product product = new Product();
		product.setId(3L);
		product.setName("Chips");
		product.setDescription("A Bag of Chips");
		product.setPrice(new BigDecimal(3));
		product.setCreatedDate(Instant.now());
		List<String> productImages = Arrays.asList("Image 1", "Image 2", "Image 3");
		product.setImages(productImages);
		Category category = new Category();
		category.setId(1L);
		category.setName("Junk Food");
		Set<Category> productCategories = new HashSet<>(
				Arrays.asList(category));
		product.setCategories(productCategories);
		
		CartInfoResponse cart = new CartInfoResponse();
		cart.setTotal(new BigDecimal(6));
		List<CartInfoResponseItem> cartItems = new ArrayList<>();
		CartInfoResponseItem cartItem = new CartInfoResponseItem();
		cartItem.setProduct(new ProductDTO(product));
		cartItem.setQuantity(2);
		cartItems.add(cartItem);
		cart.setItems(cartItems);
		
		// When
		setCustomerAuthorization();
		when(cartService.getCartInfo(Mockito.argThat(new ArgumentMatcher<CartRequest>() {

			@Override
			public boolean matches(CartRequest argument) {
				return argument.getItems().get(0).getProductId() == 1L && argument.getItems().get(0).getQuantity() == 1;
			}
		}))).thenReturn(cart);
		
		when(stripeService.createPaymentIntent(Mockito.eq(customerUsername), Mockito.eq(cart))).thenReturn("clientSecret");
		
		// Then
		mockMvc.perform(
				post("/api/v1/stripe/createPaymentIntent").content(asJsonString(cartRequest))
					.header("Authorization", "Bearer " + customerAuthToken)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$").exists())
					.andExpect(jsonPath("$.client_secret").value("clientSecret"))
					.andExpect(jsonPath("$.cart").isNotEmpty())
					;
	}
	
	@Test
	void stripeWebhook() throws Exception {
		// Given
		
		// When		
		// Then
		mockMvc.perform(
				post("/api/v1/stripe").content("payload")
					.header("Stripe-Signature", "headerSignature")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$").exists())
					;
		verify(stripeService).handleStripeWebhookEvent("payload", "headerSignature");
	}
	
	@Test
	void stripeWebhookShouldThrowAnExceptionWhenPayloadIsInvalid() throws Exception {
		// Given
		
		// When
		doThrow(new SignatureVerificationException("Invalid Signature From Test (Ignore)", "headerSignature"))
			.when(stripeService).handleStripeWebhookEvent("invalidpayload", "headerSignature");
		
		// Then
		mockMvc.perform(
				post("/api/v1/stripe").content("invalidpayload")
					.header("Stripe-Signature", "headerSignature")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					;
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
