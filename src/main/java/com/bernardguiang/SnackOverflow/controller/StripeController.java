package com.bernardguiang.SnackOverflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.service.CartService;
import com.bernardguiang.SnackOverflow.service.OrderService;
import com.bernardguiang.SnackOverflow.service.UserService;
import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/api/v1/stripe")
public class StripeController {

	private final Environment env;
	private final OrderService orderService;

	@Autowired
	public StripeController(Environment env, OrderService orderService) {
		this.env = env;
		this.orderService = orderService;
	}

	// https://stripe.com/docs/webhooks/test
	@PostMapping
	public ResponseEntity<String> stripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader)
			throws StripeException {
		System.out.println("STRIPE EVENT");
		System.out.println("Payload: " + payload);
		System.out.println("Header: " + sigHeader);
		Event event = null;

		// Verify Stripe Signature and Payload Structure
		try {
			event = Webhook.constructEvent(payload, sigHeader, env.getProperty("stripe_webhook_secret"));
		} catch (JsonSyntaxException e) {
			// Invalid payload
			System.out.println("INVALID PAYLOAD");
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Payload", HttpStatus.BAD_REQUEST);
		} catch (SignatureVerificationException e) {
			// Invalid signature
			System.out.println("INVALID SIGNATURE");
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Payload", HttpStatus.BAD_REQUEST);
		}

		// Deserialize the nested object inside the event
		EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
		StripeObject stripeObject = null;
		if (dataObjectDeserializer.getObject().isPresent()) {
			stripeObject = dataObjectDeserializer.getObject().get();
		} else {
			// Deserialization failed, probably due to an API version mismatch.
			// Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
			// instructions on how to handle this case, or return an error here.
			System.out.println("EVENT OBEJCT DESERIALIZATION FAILED");
		}

		// Handle the event
		// The stripe webhook is setup to receive only these 4 paymentIntent events

		switch (event.getType()) {
		case "payment_intent.processing":
			PaymentIntent paymentIntentProcessing = (PaymentIntent) stripeObject;
			System.out.println("payment_intent.processing for clientSecret: " + paymentIntentProcessing.getClientSecret());
			System.out.println("setting order status to: " + OrderStatus.PAYMENT_PENDING.name());
			orderService.updateStatusByClientSecret(paymentIntentProcessing.getClientSecret(), OrderStatus.PAYMENT_PENDING);
			break;
		case "payment_intent.succeeded":
			PaymentIntent paymentIntentSucceeded = (PaymentIntent) stripeObject;
			System.out.println("payment_intent.succeeded for clientSecret: " + paymentIntentSucceeded.getClientSecret());
			System.out.println("setting order status to: " + OrderStatus.PROCESSING.name());
			orderService.updateStatusByClientSecret(paymentIntentSucceeded.getClientSecret(), OrderStatus.PROCESSING);
			break;
		case "payment_intent.failed":
			PaymentIntent paymentIntentFailed = (PaymentIntent) stripeObject;
			System.out.println("payment_intent.failed for clientSecret: " + paymentIntentFailed.getClientSecret());
			System.out.println("setting order status to: " + OrderStatus.FAILED.name());
			orderService.updateStatusByClientSecret(paymentIntentFailed.getClientSecret(), OrderStatus.FAILED);
			break;
		case "payment_intent.cancelled":
			PaymentIntent paymentIntentCancelled = (PaymentIntent) stripeObject;
			System.out.println("payment_intent.failed for clientSecret: " + paymentIntentCancelled.getClientSecret());
			System.out.println("setting order status to: " + OrderStatus.CANCELLED.name());
			orderService.updateStatusByClientSecret(paymentIntentCancelled.getClientSecret(), OrderStatus.CANCELLED);
			break;
		default:
			System.out.println("Unhandled event type: " + event.getType());
		}

		return new ResponseEntity<>("Received", HttpStatus.OK);
	}
}
