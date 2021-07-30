package com.bernardguiang.SnackOverflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.repository.OrderRepository;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class StripeService {
	
	private final Environment env;
	private final OrderRepository orderRepository;
	
	@Autowired
	public StripeService(Environment env, OrderRepository orderRepository) {
		this.env = env;
		this.orderRepository = orderRepository;
	}

	public PaymentIntent createPaymentIntent(Long amount, String userEmail) throws StripeException {
		String stripeAccessKey = env.getProperty("stripe_access_key");
		Stripe.apiKey = stripeAccessKey;

		PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setCurrency("usd").setAmount(amount)
				.setReceiptEmail(userEmail) // stripe will automatically send receipts but only when using live keys
				.putMetadata("integration_check", "accept_a_payment")
				.setDescription("Test payment to SnackOverflow. This is not a real transaction").build();

		PaymentIntent intent = PaymentIntent.create(params);

		return intent;
	}
	
	// https://stripe.com/docs/webhooks/test
	public void handleStripeWebhookEvent(String payload, String signatureHeader) throws JsonSyntaxException, SignatureVerificationException {
		
		// Verify Stripe Signature and Payload Structure
		// Construction will throw JsonSyntaxException, SignatureVerificationException if invalid
		Event event = Webhook.constructEvent(payload, signatureHeader, env.getProperty("stripe_webhook_secret"));;

		// Deserialize the nested object inside the event
		EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
		StripeObject stripeObject = null;
		if (dataObjectDeserializer.getObject().isPresent()) {
			stripeObject = dataObjectDeserializer.getObject().get();
		} else {
			// Deserialization failed, probably due to an API version mismatch.
			// Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
			// instructions on how to handle this case, or return an error here.
			System.out.println("EVENT OBEJCT DESERIALIZATION FAILED, CHECK IF API VERSION MISMATCHED");
			throw new IllegalStateException("Stripe Event Data Deserialization Failed");
		}
		
		// Handle the event
		// We set up the stripe webhook to receive only these 4 paymentIntent events

		switch (event.getType()) {
		case "payment_intent.processing":
			PaymentIntent paymentIntentProcessing = (PaymentIntent) stripeObject;
			System.out.println("payment_intent.processing for clientSecret: " + paymentIntentProcessing.getClientSecret());
			System.out.println("setting order status to: " + OrderStatus.PAYMENT_PENDING.name());
			updateOrderStatusByClientSecret(paymentIntentProcessing.getClientSecret(), OrderStatus.PAYMENT_PENDING);
			break;
		case "payment_intent.succeeded":
			PaymentIntent paymentIntentSucceeded = (PaymentIntent) stripeObject;
			System.out.println("payment_intent.succeeded for clientSecret: " + paymentIntentSucceeded.getClientSecret());
			System.out.println("setting order status to: " + OrderStatus.PROCESSING.name());
			updateOrderStatusByClientSecret(paymentIntentSucceeded.getClientSecret(), OrderStatus.PROCESSING);
			break;
		case "payment_intent.failed":
			PaymentIntent paymentIntentFailed = (PaymentIntent) stripeObject;
			System.out.println("payment_intent.failed for clientSecret: " + paymentIntentFailed.getClientSecret());
			System.out.println("setting order status to: " + OrderStatus.FAILED.name());
			updateOrderStatusByClientSecret(paymentIntentFailed.getClientSecret(), OrderStatus.FAILED);
			break;
		case "payment_intent.cancelled":
			PaymentIntent paymentIntentCancelled = (PaymentIntent) stripeObject;
			System.out.println("payment_intent.failed for clientSecret: " + paymentIntentCancelled.getClientSecret());
			System.out.println("setting order status to: " + OrderStatus.CANCELLED.name());
			updateOrderStatusByClientSecret(paymentIntentCancelled.getClientSecret(), OrderStatus.CANCELLED);
			break;
		default:
			System.out.println("Unhandled event type: " + event.getType());
		}
	}
	
	private void updateOrderStatusByClientSecret(String clientSecret, OrderStatus status) {
		Order order = orderRepository.findByClientSecret(clientSecret)
			.orElseThrow(() -> new IllegalStateException("Could not find Order with Client Secret: " + clientSecret));
		
		order.setStatus(status);
		
		orderRepository.save(order);
	}
}
