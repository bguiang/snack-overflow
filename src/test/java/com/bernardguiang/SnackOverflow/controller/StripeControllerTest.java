package com.bernardguiang.SnackOverflow.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.request.CartRequest;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.service.CartService;
import com.bernardguiang.SnackOverflow.service.StripeService;
import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

class StripeControllerTest {

	private StripeController underTest;
	private StripeService stripeService;
	private CartService cartService;


	@BeforeEach
	void setUp() throws Exception {
		stripeService = Mockito.mock(StripeService.class);
		cartService = Mockito.mock(CartService.class);
		underTest = new StripeController(stripeService, cartService);
	}
	
	@Test
	void createPaymentIntent() throws StripeException {
		// Given
		CartRequest cartRequest = new CartRequest();
		List<CartInfoRequestItem> emptyList = new ArrayList<>();
		CartInfoRequestItem item = new CartInfoRequestItem();
		item.setProductId(1L);
		item.setQuantity(1);
		emptyList.add(item);
		cartRequest.setItems(emptyList);
		
		Authentication authenticationMock = Mockito.mock(Authentication.class);
		
		CartInfoResponse cart = new CartInfoResponse();
		cart.setTotal(new BigDecimal(2));
		
		// When
		when(authenticationMock.getName()).thenReturn("username");
		when(cartService.getCartInfo(cartRequest)).thenReturn(cart);
		when(stripeService.createPaymentIntent("username", cart)).thenReturn("clientSecret");
				
		ResponseEntity<Map<String, Object>> response = underTest.createPaymentIntent(cartRequest, authenticationMock);
		
		// Then
		String clientSecretResponse = (String) response.getBody().get("client_secret");
		CartInfoResponse cartResponse = (CartInfoResponse) response.getBody().get("cart");
		assertEquals("clientSecret", clientSecretResponse);
		assertEquals(new BigDecimal(2), cartResponse.getTotal());
	}

	@Test
	void StripeWebhookShouldReturnOkStatus() throws Exception {
		// Given
		String payload = "any";
		String signatureHeader = "any";
		
		// When
		doNothing().when(stripeService).handleStripeWebhookEvent(Mockito.any(), Mockito.any());
		
		// Then
		ResponseEntity<String> response = underTest.stripeWebhook(payload, signatureHeader);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Received", response.getBody());
	}
	
	@Test
	void StripeWebhookShouldReturnBadRequestIfInvalidJsonPayload() throws Exception {
		// Given
		String payload = "any";
		String signatureHeader = "any";
		
		// When
		doThrow(new JsonSyntaxException("Invalid json")).when(stripeService).handleStripeWebhookEvent(Mockito.any(), Mockito.any());
		
		// Then
		ResponseEntity<String> response = underTest.stripeWebhook(payload, signatureHeader);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Invalid Payload", response.getBody());
	}
	
	@Test
	void StripeWebhookShouldReturnBadRequestIfInvalidStripeSignature() throws Exception {
		// Given
		String payload = "any";
		String signatureHeader = "any";
		
		// When
		doThrow(new SignatureVerificationException("Invalid", "Signature")).when(stripeService).handleStripeWebhookEvent(Mockito.any(), Mockito.any());
		
		// Then
		ResponseEntity<String> response = underTest.stripeWebhook(payload, signatureHeader);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Invalid Payload", response.getBody());
	}
	
	
//	TODO: these are transferred from OrderControllerTest
//	@Test
//	void itShouldStartCheckout() throws StripeException {
//		// Given
//		String authUsername = "authUsername";
//		Authentication authentication = Mockito.mock(Authentication.class);
//		
//		UserDTO user = new UserDTO();
//		String userEmail = "user@user.com";
//		user.setEmail(userEmail);
//		Long userId = 3L;
//		user.setId(userId);
//		
//		List<CartInfoRequestItem> cartItems = new ArrayList<>();
//		Long productId = 2L;
//		int quantity = 5;
//		CartInfoRequestItem cartItem = new CartInfoRequestItem();
//		cartItem.setProductId(productId);
//		cartItem.setQuantity(quantity);
//		cartItems.add(cartItem);
//		CartRequest request = new CartRequest();
//		request.setItems(cartItems);
//		
//		CartInfoResponse cartInfoResponse = new CartInfoResponse();
//		BigDecimal cartTotal = new BigDecimal(10);
//		cartInfoResponse.setTotal(cartTotal);
//		
//		PaymentIntent intent = Mockito.mock(PaymentIntent.class);
//		String clientSecret = "clientSecret";
//		
//		Long savedOrderId = 10L;
//		Long amount = 1000L;
//		
//		// When
//		when(authentication.getName()).thenReturn(authUsername);
//		when(userService.findByUsername(authUsername)).thenReturn(user);
//		when(cartService.getCartInfo(request)).thenReturn(cartInfoResponse);
//		when(intent.getClientSecret()).thenReturn(clientSecret);
//		when(stripeService.createPaymentIntent(amount, userEmail)).thenReturn(intent);
//		when(orderService.createOrderWithCartItemsAndPaymentIntentId(request, clientSecret, user.getId())).thenReturn(savedOrderId);
//		
//		ResponseEntity<Map<String, Object>> response = underTest.startOrder(request, authentication);
//		
//		// Then
//		assertEquals(HttpStatus.CREATED, response.getStatusCode());
//		Map<String, Object> responseBody = response.getBody();
//		assertEquals(clientSecret, (String) responseBody.get("client_secret"));
//		CartInfoResponse responseCart = (CartInfoResponse) responseBody.get("cart");
//		assertEquals(cartInfoResponse.getTotal(), responseCart.getTotal());
//		assertEquals(savedOrderId, (Long) responseBody.get("orderId"));
//	}
//	
//	@Test
//	void updateOrderBillingAndShippingShouldReturnStatusOk( ) throws StripeException {
//		// Given
//		UpdateBillingAndShippingRequest updateBillingAndShippingRequest = null;
//		Authentication authentication = Mockito.mock(Authentication.class);
//		String authUsername = "authUsername";
//		
//		UserDTO user = new UserDTO();
//		String userEmail = "user@user.com";
//		user.setEmail(userEmail);
//		Long userId = 3L;
//		user.setId(userId);
//		
//		// When
//		when(userService.findByUsername(authUsername)).thenReturn(user);
//
//		when(orderService.updateBillingAndShipping(updateBillingAndShippingRequest, user)).thenReturn(null);
//		
//		ResponseEntity<String> response = underTest.updateOrderBillingAndShipping(
//				updateBillingAndShippingRequest, authentication);
//		// Then
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//	}

}
