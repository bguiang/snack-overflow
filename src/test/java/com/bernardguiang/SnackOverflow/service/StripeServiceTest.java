package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.repository.OrderRepository;
import com.bernardguiang.SnackOverflow.repository.ProductRepository;
import com.bernardguiang.SnackOverflow.repository.StripePaymentIntentRepository;
import com.bernardguiang.SnackOverflow.repository.UserRepository;
import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;

class StripeServiceTest {

	private StripeService underTest;
	
	private Environment env;
	private OrderRepository orderRepository;
	private StripePaymentIntentRepository stripePaymentIntentRepository;
	private UserRepository userRepository;
	private ProductRepository productRepository;
	
	static MockedStatic<Webhook> stripeWebhook;

	@BeforeEach
	void setUp() throws Exception {
		env = Mockito.mock(Environment.class);
		orderRepository = Mockito.mock(OrderRepository.class);
		stripePaymentIntentRepository = Mockito.mock(StripePaymentIntentRepository.class);
		userRepository = Mockito.mock(UserRepository.class);
		productRepository = Mockito.mock(ProductRepository.class);
		
		
		underTest = new StripeService(env, orderRepository, stripePaymentIntentRepository, userRepository, productRepository);
	}
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		stripeWebhook = Mockito.mockStatic(Webhook.class);
	}

//	@Test
//	void itShouldUpdateStatusByClientSecretIfEventPaymentIntentProcessing() throws JsonSyntaxException, SignatureVerificationException {
//		
//		// Given
//		String payload = "payload";
//		String signatureHeader = "signature header";
//		
//		// When
//		// ... return mocked event
//		Event event = Mockito.mock(Event.class);
//		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any()))
//        .thenReturn(event);
//		
//		// ... return mocked EventDataObjectDeserializer
//		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);
//		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);
//		
//		// ... Return mocked PaymentIntent (StripeObject)
//		String intentClientSecret = "client secret";
//		PaymentIntent paymentIntent = new PaymentIntent();
//		paymentIntent.setClientSecret(intentClientSecret);
//		Optional<StripeObject> stripeObjectOptional = Optional.of(paymentIntent);
//		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);
//		
//		// ... Return Event Type
//		when(event.getType()).thenReturn("payment_intent.processing");
//		
//		// ... Find Existing Order by Client Secret
//		Order order = new Order(); // Default status of OrderStatus.Created
//		Optional<Order> orderOptional = Optional.of(order);
//		when(orderRepository.findByClientSecret(intentClientSecret)).thenReturn(orderOptional);
//		
//		// ... Update Order Status
//		when(orderRepository.save(order)).thenReturn(order);
//
//		// Then
//		underTest.handleStripeWebhookEvent(payload, signatureHeader);
//		assertEquals(OrderStatus.PAYMENT_PENDING, order.getStatus());
//	}
//	
//	@Test
//	void itShouldUpdateStatusByClientSecretIfEventPaymentIntentSucceeded() throws JsonSyntaxException, SignatureVerificationException {
//		
//		// Given
//		String payload = "payload";
//		String signatureHeader = "signature header";
//		
//		// When
//		// ... return mocked event
//		Event event = Mockito.mock(Event.class);
//		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any()))
//        .thenReturn(event);
//
//		// ... return mocked EventDataObjectDeserializer
//		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);
//		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);
//		
//		// ... Return mocked PaymentIntent (StripeObject)
//		String intentClientSecret = "client secret";
//		PaymentIntent paymentIntent = new PaymentIntent();
//		paymentIntent.setClientSecret(intentClientSecret);
//		Optional<StripeObject> stripeObjectOptional = Optional.of(paymentIntent);
//		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);
//		
//		// ... Return Event Type
//		when(event.getType()).thenReturn("payment_intent.succeeded");
//		
//		// ... Find Existing Order by Client Secret
//		Order order = new Order(); // Default status of OrderStatus.Created
//		Optional<Order> orderOptional = Optional.of(order);
//		when(orderRepository.findByClientSecret(intentClientSecret)).thenReturn(orderOptional);
//		
//		// ... Update Order Status
//		when(orderRepository.save(order)).thenReturn(order);
//
//		// Then
//		underTest.handleStripeWebhookEvent(payload, signatureHeader);
//		assertEquals(OrderStatus.PROCESSING, order.getStatus());
//	}
//	
//	@Test
//	void itShouldUpdateStatusByClientSecretIfEventPaymentIntentFailed() throws JsonSyntaxException, SignatureVerificationException {
//		
//		// Given
//		String payload = "payload";
//		String signatureHeader = "signature header";
//		
//		// When
//		// ... return mocked event
//		Event event = Mockito.mock(Event.class);
//		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any()))
//        .thenReturn(event);
//		
//		// ... return mocked EventDataObjectDeserializer
//		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);
//		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);
//		
//		// ... Return mocked PaymentIntent (StripeObject)
//		String intentClientSecret = "client secret";
//		PaymentIntent paymentIntent = new PaymentIntent();
//		paymentIntent.setClientSecret(intentClientSecret);
//		Optional<StripeObject> stripeObjectOptional = Optional.of(paymentIntent);
//		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);
//		
//		// ... Return Event Type
//		when(event.getType()).thenReturn("payment_intent.failed");
//		
//		// ... Find Existing Order by Client Secret
//		Order order = new Order(); // Default status of OrderStatus.Created
//		Optional<Order> orderOptional = Optional.of(order);
//		when(orderRepository.findByClientSecret(intentClientSecret)).thenReturn(orderOptional);
//		
//		// ... Update Order Status
//		when(orderRepository.save(order)).thenReturn(order);
//
//		// Then
//		underTest.handleStripeWebhookEvent(payload, signatureHeader);
//		assertEquals(OrderStatus.FAILED, order.getStatus());
//	}
//	
//	@Test
//	void itShouldUpdateStatusByClientSecretIfEventPaymentIntentCancelled() throws JsonSyntaxException, SignatureVerificationException {
//		
//		// Given
//		String payload = "payload";
//		String signatureHeader = "signature header";
//		
//		// When
//		// ... return mocked event
//		Event event = Mockito.mock(Event.class);
//		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any()))
//        .thenReturn(event);
//		
//		// ... return mocked EventDataObjectDeserializer
//		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);
//		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);
//		
//		// ... Return mocked PaymentIntent (StripeObject)
//		String intentClientSecret = "client secret";
//		PaymentIntent paymentIntent = new PaymentIntent();
//		paymentIntent.setClientSecret(intentClientSecret);
//		Optional<StripeObject> stripeObjectOptional = Optional.of(paymentIntent);
//		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);
//		
//		// ... Return Event Type
//		when(event.getType()).thenReturn("payment_intent.cancelled");
//		
//		// ... Find Existing Order by Client Secret
//		Order order = new Order(); // Default status of OrderStatus.Created
//		Optional<Order> orderOptional = Optional.of(order);
//		when(orderRepository.findByClientSecret(intentClientSecret)).thenReturn(orderOptional);
//		
//		// ... Update Order Status
//		when(orderRepository.save(order)).thenReturn(order);
//
//		// Then
//		underTest.handleStripeWebhookEvent(payload, signatureHeader);
//		assertEquals(OrderStatus.CANCELLED, order.getStatus());
//	}
//	
//	@Test
//	void itShouldNotUpdateIfUnhandledEventType() throws JsonSyntaxException, SignatureVerificationException {
//		
//		// Given
//		String payload = "payload";
//		String signatureHeader = "signature header";
//		
//		// When
//		// ... return mocked event
//		Event event = Mockito.mock(Event.class);
//		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any()))
//        .thenReturn(event);
//		
//		// ... return mocked EventDataObjectDeserializer
//		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);
//		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);
//		
//		// ... Return mocked PaymentIntent (StripeObject)
//		String intentClientSecret = "client secret";
//		PaymentIntent paymentIntent = new PaymentIntent();
//		paymentIntent.setClientSecret(intentClientSecret);
//		Optional<StripeObject> stripeObjectOptional = Optional.of(paymentIntent);
//		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);
//		
//		// ... Return Event Type
//		when(event.getType()).thenReturn("some unhandled event");
//		
//		// Then
//		underTest.handleStripeWebhookEvent(payload, signatureHeader);
//		
//		verify(orderRepository, Mockito.never()).findByClientSecret(Mockito.anyString());
//		verify(orderRepository, Mockito.never()).save(Mockito.any());
//	}
//	
//	@Test
//	void itShouldThrowAnExceptionWhenDeserializationFails() throws JsonSyntaxException, SignatureVerificationException {
//		
//		// Given
//		String payload = "payload";
//		String signatureHeader = "signature header";
//		
//		// When
//		// ... return mocked event
//		Event event = Mockito.mock(Event.class);
//		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any()))
//        .thenReturn(event);
//		
//		// ... return mocked EventDataObjectDeserializer
//		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);
//		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);
//		
//		// ... Return mocked PaymentIntent (StripeObject)
//		String intentClientSecret = "client secret";
//		Optional<StripeObject> stripeObjectOptional = Optional.ofNullable(null);
//		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);
//
//		// Then
//		assertThrows(IllegalStateException.class, ()-> underTest.handleStripeWebhookEvent(payload, signatureHeader),
//				"Stripe Event Data Deserialization Failed");
//	}
//	
//	
//	@Test
//	void itShouldThrowAnExceptionWhenOrderWithClientSecretNotFound() throws JsonSyntaxException, SignatureVerificationException {
//		
//		// Given
//		String payload = "payload";
//		String signatureHeader = "signature header";
//		
//		// When
//		// ... return mocked event
//		Event event = Mockito.mock(Event.class);
//		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any()))
//        .thenReturn(event);
//		
//		// ... return mocked EventDataObjectDeserializer
//		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);
//		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);
//		
//		// ... Return mocked PaymentIntent (StripeObject)
//		String intentClientSecret = "client secret";
//		PaymentIntent paymentIntent = new PaymentIntent();
//		paymentIntent.setClientSecret(intentClientSecret);
//		Optional<StripeObject> stripeObjectOptional = Optional.of(paymentIntent);
//		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);
//		
//		// ... Return Event Type
//		when(event.getType()).thenReturn("payment_intent.failed");
//		
//		// ... Find Existing Order by Client Secret
//		Optional<Order> orderOptional = Optional.ofNullable(null);
//		when(orderRepository.findByClientSecret(intentClientSecret)).thenReturn(orderOptional);
//
//		// Then
//		assertThrows(IllegalStateException.class, ()-> underTest.handleStripeWebhookEvent(payload, signatureHeader),
//				"Could not find Order with Client Secret: " + intentClientSecret);
//	}

}
