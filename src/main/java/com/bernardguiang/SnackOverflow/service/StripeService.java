package com.bernardguiang.SnackOverflow.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponseItem;
import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Cart;
import com.bernardguiang.SnackOverflow.model.CartItem;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.ShippingDetails;
import com.bernardguiang.SnackOverflow.model.StripePaymentIntent;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.OrderRepository;
import com.bernardguiang.SnackOverflow.repository.ProductRepository;
import com.bernardguiang.SnackOverflow.repository.StripePaymentIntentRepository;
import com.bernardguiang.SnackOverflow.repository.UserRepository;
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
	private final StripePaymentIntentRepository stripePaymentIntentRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	
	@Autowired
	public StripeService(Environment env, OrderRepository orderRepository, StripePaymentIntentRepository stripePaymentIntentRepository,
			UserRepository userRepository, ProductRepository productRepository) {
		this.env = env;
		this.orderRepository = orderRepository;
		this.stripePaymentIntentRepository = stripePaymentIntentRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}
	
	public String createPaymentIntent(String username, CartInfoResponse cartInfoResponse) throws StripeException {
		
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalStateException("Could not find User with username: " + username));
		String userEmail = user.getEmail();
		
		// Create Payment Intent and Client Secret
		Long amount = cartInfoResponse.getTotal().longValue() * 100;
		
		String stripeAccessKey = env.getProperty("stripe_access_key");
		Stripe.apiKey = stripeAccessKey;

		PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
				.setCurrency("usd")
				.setAmount(amount)
				.setReceiptEmail(userEmail) // stripe will automatically send receipts but only when using live keys
				.putMetadata("integration_check", "accept_a_payment")
				.setDescription("Test payment to SnackOverflow. This is not a real transaction").build();

		PaymentIntent intent = PaymentIntent.create(params);
		String clientSecret = intent.getClientSecret();
		
		
		StripePaymentIntent stripePaymentIntent = new StripePaymentIntent();

		String paymentIntentId = intent.getId();
		stripePaymentIntent.setPaymentIntentId(paymentIntentId);
		System.out.println("stripePaymentIntent created with paymentIntentId: " + paymentIntentId);
		
		// Convert CartInfoResponse to Cart
		Cart cart = new Cart();
		List<CartItem> items = new ArrayList<>();
		for(CartInfoResponseItem item : cartInfoResponse.getItems()) {
			Long productId = item.getProduct().getId();
			Product product = productRepository.findById(item.getProduct().getId())
				.orElseThrow(() -> new IllegalStateException("Could not find Product with id: " + productId));
			int quantity = item.getQuantity();
			BigDecimal price = product.getPrice();
			
			CartItem newItem = new CartItem();
			newItem.setCart(cart);
			newItem.setProduct(product);
			newItem.setPrice(price);
			newItem.setQuantity(quantity);
			
			items.add(newItem);
		}
		cart.setItems(items);
		cart.setTotal(cartInfoResponse.getTotal());
		cart.setStripePaymentIntent(stripePaymentIntent);
		stripePaymentIntent.setCart(cart);
		
		stripePaymentIntent.setUser(user);
		stripePaymentIntentRepository.save(stripePaymentIntent);
		
		return clientSecret;
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
			System.out.println("payment_intent.processing for paymentIntentId: " + paymentIntentProcessing.getId());
			System.out.println("setting order status to: " + OrderStatus.PAYMENT_PENDING.name());
			
			createOrUpdateOrder(paymentIntentProcessing, OrderStatus.PAYMENT_PENDING);
			break;
		case "payment_intent.succeeded":
			PaymentIntent paymentIntentSucceeded = (PaymentIntent) stripeObject;
			System.out.println("payment_intent.succeeded for paymentIntentId: " + paymentIntentSucceeded.getId());
			System.out.println("setting order status to: " + OrderStatus.PROCESSING.name());
			createOrUpdateOrder(paymentIntentSucceeded, OrderStatus.PROCESSING);
			break;
		case "payment_intent.failed":
			PaymentIntent paymentIntentFailed = (PaymentIntent) stripeObject;
			System.out.println("payment_intent.failed for paymentIntentId: " + paymentIntentFailed.getId());
			System.out.println("setting order status to: " + OrderStatus.FAILED.name());
			createOrUpdateOrder(paymentIntentFailed, OrderStatus.FAILED);
			break;
		case "payment_intent.cancelled":
			PaymentIntent paymentIntentCancelled = (PaymentIntent) stripeObject;
			System.out.println("payment_intent.failed for paymentIntentId: " + paymentIntentCancelled.getId());
			System.out.println("setting order status to: " + OrderStatus.CANCELLED.name());
			createOrUpdateOrder(paymentIntentCancelled, OrderStatus.CANCELLED);
			break;
		default:
			System.out.println("Unhandled event type: " + event.getType());
		}
	}
	
	private void createOrUpdateOrder(PaymentIntent paymentIntent, OrderStatus status) {
		
//		System.out.println("createOrUpdateOrder()");
//		System.out.println("PaymentIntent: " + paymentIntent.toString());
		// Check if Order with paymentIntentId already exists
		String paymentIntentId = paymentIntent.getId();
		System.out.println("paymentIntentId: " + paymentIntentId);
		Optional<Order> orderOptional = orderRepository.findByPaymentIntentId(paymentIntentId);
		
		// Check first if Order already exists
		Order order = null;
		if(orderOptional.isPresent()) {
			System.out.println("Order for paymentIntentId exists. Update existing order");
			// Order exists, just need to update status
			order = orderOptional.get();
		} else {
			System.out.println("Order for paymentIntentId does not exists. Create new order");
			// Order does not yet exist, create new order with saved StripePaymentIntent and Billing and Shipping info from the event PaymentIntent payload
			order = new Order();
			
			// Find Saved StripePaymentIntent to retrieve Cart and User
			StripePaymentIntent stripePaymentIntent = stripePaymentIntentRepository.findByPaymentIntentId(paymentIntentId)
				.orElseThrow(() -> new IllegalStateException("Could not StripePaymentIntent with paymentIntentId: " + paymentIntentId));
			
			User user = stripePaymentIntent.getUser();
			order.setUser(user); // TODO: might need to save User instead of the order? Since User is the owner
			
			// Set order total
			Cart cart = stripePaymentIntent.getCart();
			order.setTotal(cart.getTotal());
			
			// Set order items
			List<OrderItem> orderItems = new ArrayList<>();
			for(CartItem cartItem : cart.getItems()) {
				OrderItem orderItem = new OrderItem();
				orderItem.setOrder(order);
				orderItem.setProduct(cartItem.getProduct());
				orderItem.setQuantity(cartItem.getQuantity());
				orderItem.setPrice(cartItem.getPrice());
				
				orderItems.add(orderItem);
			}
			order.setItems(orderItems);
			
			// Set Order Billing
			BillingDetails billing = new BillingDetails();
			billing.setOrder(order);
			
			com.stripe.model.PaymentMethod.BillingDetails stripeBillingDetails = paymentIntent.getCharges().getData().get(0).getBillingDetails();
			billing.setEmail(stripeBillingDetails.getEmail());
			billing.setName(stripeBillingDetails.getName());
			billing.setPhone(stripeBillingDetails.getPhone());
			Address billingAddress = new Address(
					stripeBillingDetails.getAddress().getLine1(),
					stripeBillingDetails.getAddress().getLine2(), 
					stripeBillingDetails.getAddress().getCity(),
					stripeBillingDetails.getAddress().getState(),
					stripeBillingDetails.getAddress().getPostalCode(),
					stripeBillingDetails.getAddress().getCountry()
			);
			billing.setAddress(billingAddress);
			order.setBillingDetails(billing);
			
			// Set Order Shipping
			ShippingDetails shipping = new ShippingDetails();
			shipping.setOrder(order);
			
			com.stripe.model.ShippingDetails stripeShippingDetails = paymentIntent.getShipping();
			shipping.setName(stripeShippingDetails.getName());
			shipping.setPhone(stripeShippingDetails.getPhone());
			Address shippingAddress = new Address(
					stripeShippingDetails.getAddress().getLine1(),
					stripeShippingDetails.getAddress().getLine2(), 
					stripeShippingDetails.getAddress().getCity(),
					stripeShippingDetails.getAddress().getState(),
					stripeShippingDetails.getAddress().getPostalCode(),
					stripeShippingDetails.getAddress().getCountry()
			);
			shipping.setAddress(shippingAddress);
			order.setShippingDetails(shipping);
			
			// Set Order CreateDate
			order.setCreatedDate(Instant.now());
			order.setPaymentIntentId(paymentIntentId);
		}
		
		// Set/Update Status
		order.setStatus(status);
		
		orderRepository.save(order);
	}
}
