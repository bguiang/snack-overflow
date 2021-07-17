package com.bernardguiang.SnackOverflow.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.CheckoutRequest;
import com.bernardguiang.SnackOverflow.dto.RegisterRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/checkout")
public class CheckoutController {
	
	@Autowired
	private Environment env;
	
	@GetMapping
	public String test() {
		return "Test test test test";
	}
	
	
	//@PostMapping
	//@PreAuthorize("hasAuthority('order:write')")
	public ResponseEntity<String> createCheckoutSession(@RequestBody CheckoutRequest checkoutRequest, HttpServletResponse response) throws IOException, StripeException {
	//public void createCheckoutSession(@RequestBody CheckoutRequest checkoutRequest, HttpServletResponse response) throws IOException, StripeException {
		
		System.out.println("createCheckoutSession");
		
		// This is your real test secret API key.
		String stripeAccessKey = env.getProperty("stripe_access_key");
	    Stripe.apiKey = stripeAccessKey;
	    
		String checkoutPage = "http://localhost:3000/checkout";
        SessionCreateParams params =
          SessionCreateParams.builder()
            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
            .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.AUTO)
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(checkoutPage + "/success")
            .setCancelUrl(checkoutPage + "/canceled")
            .addLineItem(
              SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                  SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("usd")
                    .setUnitAmount(2000L)
                    .setProductData(
                      SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName("Stubborn Attachments")
                        .build())
                    .build())
                .build())
            .build();
      Session session = Session.create(params);

      //response.sendRedirect(session.getUrl());
      return new ResponseEntity<>(session.getUrl(), HttpStatus.CREATED);
	}
	
	@PostMapping("/createPaymentIntent")
	public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody CheckoutRequest checkoutRequest) throws StripeException {
		// Set your secret key. Remember to switch to your live secret key in production.
		// See your keys here: https://dashboard.stripe.com/apikeys
		String stripeAccessKey = env.getProperty("stripe_access_key");
	    Stripe.apiKey = stripeAccessKey;

		PaymentIntentCreateParams params =
		  PaymentIntentCreateParams.builder()
		    .setCurrency("usd")
		    .setAmount(1099L)
		    // Verify your integration in this guide by including this parameter
		    .putMetadata("integration_check", "accept_a_payment")
		    .build();

		PaymentIntent intent = PaymentIntent.create(params);

		Map<String, String> map = new HashMap();
		map.put("client_secret", intent.getClientSecret());
		
		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}
}
