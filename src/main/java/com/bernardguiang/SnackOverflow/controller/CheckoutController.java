package com.bernardguiang.SnackOverflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.Cart;
import com.bernardguiang.SnackOverflow.dto.CartInfoRequestItem;
import com.bernardguiang.SnackOverflow.dto.CheckoutRequest;
import com.bernardguiang.SnackOverflow.dto.OrderDTO;
import com.bernardguiang.SnackOverflow.service.CartService;
import com.bernardguiang.SnackOverflow.service.OrderService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/checkout")
public class CheckoutController {
	
	private final Environment env;
	private final CartService cartService;
	private final OrderService orderService;
	
	@Autowired
	public CheckoutController(Environment env, CartService cartService, OrderService orderService) {
		this.env = env;
		this.cartService = cartService;
		this.orderService = orderService;
	}
	
	
	// TODO: start creating order here then return order id?
	@PostMapping("/startCheckout")
	@PreAuthorize("hasAuthority('order:write')")
	public ResponseEntity<Map<String, Object>> startCheckout(@RequestBody List<CartInfoRequestItem> cartItems) throws StripeException {
		
		// Get Cart Product Info and Total
		Cart cart = cartService.getCartInfo(cartItems);
		
		// Create partial order object here (no billing or shipping) to guarantee saved item prices won't change?
		
		// Create Payment Intent
		String stripeAccessKey = env.getProperty("stripe_access_key");
	    Stripe.apiKey = stripeAccessKey;
	    
	    Long amount = cart.getTotal().longValue() * 100;
	    
	    System.out.println(cart.toString());
	    System.out.println("Cart Total: " + cart.getTotal());
		PaymentIntentCreateParams params =
		  PaymentIntentCreateParams.builder()
		    .setCurrency("usd")
		    .setAmount(amount)
		    // Verify your integration in this guide by including this parameter
		    .putMetadata("integration_check", "accept_a_payment")
		    .build();

		PaymentIntent intent = PaymentIntent.create(params);

		Map<String, Object> map = new HashMap<>();
		map.put("client_secret", intent.getClientSecret());
		map.put("cart", cart);
		//map.put("orderId")
		
		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> createOrder(@RequestBody @Valid CheckoutRequest checkoutRequest) throws StripeException {
		
		//TODO: get userid
		OrderDTO savedOrderDTO = orderService.saveOrderFromCheckout(checkoutRequest, userId);
		
		Map<String, Object> data = new HashMap<>();
		data.put("orderId", savedOrderDTO.getId());
		
		return new ResponseEntity<>(data, HttpStatus.CREATED);
	}
	
	@PostMapping("/stripe/paymentIntent")
	public void stripeWebhook() throws StripeException {


		//TODO: update order status
	}
}
