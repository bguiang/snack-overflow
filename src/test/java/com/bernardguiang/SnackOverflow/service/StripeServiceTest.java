package com.bernardguiang.SnackOverflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.env.Environment;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponse;
import com.bernardguiang.SnackOverflow.dto.response.CartInfoResponseItem;
import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Cart;
import com.bernardguiang.SnackOverflow.model.CartItem;
import com.bernardguiang.SnackOverflow.model.Order;
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
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;

class StripeServiceTest {

	private StripeService underTest;

	private Environment env;
	private OrderRepository orderRepository;
	private StripePaymentIntentRepository stripePaymentIntentRepository;
	private UserRepository userRepository;
	private ProductRepository productRepository;

	static MockedStatic<Webhook> stripeWebhook;

	private User user;
	private CartInfoResponse cartInfoResponse;
	private Product product1, product2;

	@BeforeEach
	void setUp() throws Exception {
		env = Mockito.mock(Environment.class);
		orderRepository = Mockito.mock(OrderRepository.class);
		stripePaymentIntentRepository = Mockito.mock(StripePaymentIntentRepository.class);
		userRepository = Mockito.mock(UserRepository.class);
		productRepository = Mockito.mock(ProductRepository.class);
		underTest = new StripeService(env, orderRepository, stripePaymentIntentRepository, userRepository,
				productRepository);

		user = new User();
		user.setUsername("username");
		user.setEmail("user@email.com");

		cartInfoResponse = new CartInfoResponse();
		cartInfoResponse.setTotal(new BigDecimal(50));

		List<CartInfoResponseItem> items = new ArrayList<>();
		CartInfoResponseItem item1 = new CartInfoResponseItem();
		ProductDTO productDTO1 = new ProductDTO();
		productDTO1.setId(1L);
		item1.setProduct(productDTO1);
		item1.setQuantity(2);

		CartInfoResponseItem item2 = new CartInfoResponseItem();
		ProductDTO productDTO2 = new ProductDTO();
		productDTO2.setId(1L);
		item2.setProduct(productDTO2);
		item2.setQuantity(6);
		items.add(item1);
		items.add(item2);
		cartInfoResponse.setItems(items);
		product1 = new Product();
		product1.setId(1L);
		product1.setPrice(new BigDecimal(10));
		product2 = new Product();
		product2.setId(2L);
		product2.setPrice(new BigDecimal(5));
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		stripeWebhook = Mockito.mockStatic(Webhook.class);
	}

	@Test
	void createPaymentIntent() throws StripeException {
		// Given
		MockedStatic<PaymentIntent> paymentIntentMockStatic = Mockito.mockStatic(PaymentIntent.class);
		PaymentIntent mockPaymentIntent = Mockito.mock(PaymentIntent.class);
		ArgumentCaptor<StripePaymentIntent> stripePaymentIntentCaptor = ArgumentCaptor
				.forClass(StripePaymentIntent.class);

		Optional<User> userOptional = Optional.ofNullable(user);

		// When
		when(userRepository.findByUsername("username")).thenReturn(userOptional);
		when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product1));
		when(productRepository.findById(2L)).thenReturn(Optional.ofNullable(product2));
		paymentIntentMockStatic.when(() -> PaymentIntent.create((PaymentIntentCreateParams) Mockito.any()))
				.thenAnswer(new Answer<PaymentIntent>() {
					public PaymentIntent answer(InvocationOnMock invocation) throws Throwable {
						paymentIntentMockStatic.close();
						return mockPaymentIntent;
					}
				});
		when(mockPaymentIntent.getId()).thenReturn("paymentIntentID");
		when(mockPaymentIntent.getClientSecret()).thenReturn("clientSecret");

		String clientSecret = underTest.createPaymentIntent("username", cartInfoResponse);

		// Then
		verify(stripePaymentIntentRepository).save(stripePaymentIntentCaptor.capture());

		assertEquals("clientSecret", clientSecret);
		assertEquals(user, stripePaymentIntentCaptor.getValue().getUser());
		assertEquals(new BigDecimal(50), stripePaymentIntentCaptor.getValue().getCart().getTotal());
		assertEquals(2, stripePaymentIntentCaptor.getValue().getCart().getItems().size());
	}

	@Test
	void createPaymentIntentShouldThrowAnExceptionIfUserDoesNotExist() throws StripeException {
		// Given
		Optional<User> userOptional = Optional.ofNullable(null);

		// When
		when(userRepository.findByUsername("username")).thenReturn(userOptional);

		// Then
		assertThrows(IllegalStateException.class, () -> underTest.createPaymentIntent("username", cartInfoResponse),
				"Could not find User with username: username");
	}

	@Test
	void createPaymentIntentShouldThrowAnExceptionIfProductNotFound() throws StripeException {
		// Given
		MockedStatic<PaymentIntent> paymentIntentMockStatic = Mockito.mockStatic(PaymentIntent.class);
		PaymentIntent mockPaymentIntent = Mockito.mock(PaymentIntent.class);

		Optional<User> userOptional = Optional.ofNullable(user);

		// When
		when(userRepository.findByUsername("username")).thenReturn(userOptional);
		when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
		paymentIntentMockStatic.when(() -> PaymentIntent.create((PaymentIntentCreateParams) Mockito.any()))
				.thenAnswer(new Answer<PaymentIntent>() {
					public PaymentIntent answer(InvocationOnMock invocation) throws Throwable {
						paymentIntentMockStatic.close();
						return mockPaymentIntent;
					}
				});
		when(mockPaymentIntent.getId()).thenReturn("paymentIntentID");
		when(mockPaymentIntent.getClientSecret()).thenReturn("clientSecret");

		// Then
		assertThrows(IllegalStateException.class, () -> underTest.createPaymentIntent("username", cartInfoResponse),
				"Could not find Product with id: 1L");
	}

	@Test
	void handleStripeWebhookEventProcessing() throws JsonSyntaxException, SignatureVerificationException {

		// Given
		String payload = "payload";

		Event event = Mockito.mock(Event.class);
		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);

		// When
		// ... return mocked event
		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(event);

		// ... return mocked EventDataObjectDeserializer
		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);

		// ... Return mocked PaymentIntent (StripeObject)
		PaymentIntent paymentIntent = new PaymentIntent();
		paymentIntent.setClientSecret("client secret");
		paymentIntent.setId("paymentIntentId");
		Optional<StripeObject> stripeObjectOptional = Optional.of(paymentIntent);
		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);

		// ... Return Event Type
		when(event.getType()).thenReturn("payment_intent.processing");

		// ... Find Existing Order by PaymentIntentId
		Order order = new Order();
		Optional<Order> orderOptional = Optional.of(order);
		when(orderRepository.findByPaymentIntentId("paymentIntentId")).thenReturn(orderOptional);

		// ... Update Order Status
		when(orderRepository.save(order)).thenReturn(order);

		// Then
		underTest.handleStripeWebhookEvent(payload, "signature header");
		assertEquals(OrderStatus.PAYMENT_PENDING, order.getStatus());
	}

	@Test
	void handleStripeWebhookEventSucceeded() throws JsonSyntaxException, SignatureVerificationException {

		// Given
		String payload = "payload";

		Event event = Mockito.mock(Event.class);
		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);

		// When
		// ... return mocked event
		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(event);

		// ... return mocked EventDataObjectDeserializer
		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);

		// ... Return mocked PaymentIntent (StripeObject)
		PaymentIntent paymentIntent = new PaymentIntent();
		paymentIntent.setClientSecret("client secret");
		paymentIntent.setId("paymentIntentId");
		Optional<StripeObject> stripeObjectOptional = Optional.of(paymentIntent);
		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);

		// ... Return Event Type
		when(event.getType()).thenReturn("payment_intent.succeeded");

		// ... Find Existing Order by PaymentIntentId
		Order order = new Order();
		Optional<Order> orderOptional = Optional.of(order);
		when(orderRepository.findByPaymentIntentId("paymentIntentId")).thenReturn(orderOptional);

		// ... Update Order Status
		when(orderRepository.save(order)).thenReturn(order);

		// Then
		underTest.handleStripeWebhookEvent(payload, "signature header");
		assertEquals(OrderStatus.PROCESSING, order.getStatus());
	}

	@Test
	void handleStripeWebhookEventFailed() throws JsonSyntaxException, SignatureVerificationException {

		// Given
		String payload = "payload";

		Event event = Mockito.mock(Event.class);
		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);

		// When
		// ... return mocked event
		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(event);

		// ... return mocked EventDataObjectDeserializer
		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);

		// ... Return mocked PaymentIntent (StripeObject)
		PaymentIntent paymentIntent = new PaymentIntent();
		paymentIntent.setClientSecret("client secret");
		paymentIntent.setId("paymentIntentId");
		Optional<StripeObject> stripeObjectOptional = Optional.of(paymentIntent);
		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);

		// ... Return Event Type
		when(event.getType()).thenReturn("payment_intent.failed");

		// ... Find Existing Order by PaymentIntentId
		Order order = new Order();
		Optional<Order> orderOptional = Optional.of(order);
		when(orderRepository.findByPaymentIntentId("paymentIntentId")).thenReturn(orderOptional);

		// ... Update Order Status
		when(orderRepository.save(order)).thenReturn(order);

		// Then
		underTest.handleStripeWebhookEvent(payload, "signature header");
		assertEquals(OrderStatus.FAILED, order.getStatus());
	}

	@Test
	void handleStripeWebhookEventCancelled() throws JsonSyntaxException, SignatureVerificationException {

		// Given
		String payload = "payload";

		Event event = Mockito.mock(Event.class);
		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);

		// When
		// ... return mocked event
		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(event);

		// ... return mocked EventDataObjectDeserializer
		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);

		// ... Return mocked PaymentIntent (StripeObject)
		PaymentIntent paymentIntent = new PaymentIntent();
		paymentIntent.setClientSecret("client secret");
		paymentIntent.setId("paymentIntentId");
		Optional<StripeObject> stripeObjectOptional = Optional.of(paymentIntent);
		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);

		// ... Return Event Type
		when(event.getType()).thenReturn("payment_intent.cancelled");

		// ... Find Existing Order by PaymentIntentId
		Order order = new Order();
		Optional<Order> orderOptional = Optional.of(order);
		when(orderRepository.findByPaymentIntentId("paymentIntentId")).thenReturn(orderOptional);

		// ... Update Order Status
		when(orderRepository.save(order)).thenReturn(order);

		// Then
		underTest.handleStripeWebhookEvent(payload, "signature header");
		assertEquals(OrderStatus.CANCELLED, order.getStatus());
	}

	@Test
	void itShouldNotUpdateIfUnhandledEventType() throws JsonSyntaxException, SignatureVerificationException {

		// Given
		String payload = "payload";

		Event event = Mockito.mock(Event.class);
		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);

		// When
		// ... return mocked event
		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(event);

		// ... return mocked EventDataObjectDeserializer
		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);

		// ... Return mocked PaymentIntent (StripeObject)
		PaymentIntent paymentIntent = new PaymentIntent();
		paymentIntent.setClientSecret("client secret");
		paymentIntent.setId("paymentIntentId");
		Optional<StripeObject> stripeObjectOptional = Optional.of(paymentIntent);
		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);

		// ... Return Event Type
		when(event.getType()).thenReturn("some unhandled event");

		// Then
		underTest.handleStripeWebhookEvent(payload, "signatureHeader");

		verify(orderRepository, Mockito.never()).findByPaymentIntentId(Mockito.anyString());
		verify(orderRepository, Mockito.never()).save(Mockito.any());
	}
	
	@Test
	void itShouldThrowAnExceptionWhenDeserializationFails() throws JsonSyntaxException, SignatureVerificationException {
		
		// Given
		String payload = "payload";
		
		Event event = Mockito.mock(Event.class);
		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);
		
		// When
		// ... return mocked event
		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(event);
		
		// ... return mocked EventDataObjectDeserializer
		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);
		
		// ... Return mocked PaymentIntent (StripeObject)
		Optional<StripeObject> stripeObjectOptional = Optional.ofNullable(null);
		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);

		// Then
		assertThrows(IllegalStateException.class, ()-> underTest.handleStripeWebhookEvent(payload, "signature header"),
				"Stripe Event Data Deserialization Failed");
	}
	
	
	@Test
	void handleStripeWebhookEventShouldCreateNewOrderIfOrderDoesNotExist() throws JsonSyntaxException, SignatureVerificationException {

		// Given
		String payload = "payload";
		ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

		Event event = Mockito.mock(Event.class);
		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);

		StripePaymentIntent stripePaymentIntent = new StripePaymentIntent();
		stripePaymentIntent.setId(2L);
		stripePaymentIntent.setPaymentIntentId("paymentIntentId");
		stripePaymentIntent.setUser(user);
		
		Cart cart = new Cart();
		cart.setId(4L);
		CartItem item1 = new CartItem();
		item1.setPrice(new BigDecimal(20));
		item1.setQuantity(2);
		item1.setProduct(product1);
		CartItem item2 = new CartItem();
		item2.setPrice(new BigDecimal(10));
		item2.setQuantity(1);
		item2.setProduct(product2);
		List<CartItem> items = Arrays.asList(item1, item2);
		cart.setItems(items);
		cart.setTotal(new BigDecimal(50));
		stripePaymentIntent.setCart(cart);
		
		com.stripe.model.PaymentMethod.BillingDetails stripeBillingDetails = new com.stripe.model.PaymentMethod.BillingDetails();
		com.stripe.model.Address stripeBillingAddress = new com.stripe.model.Address();
		stripeBillingAddress.setLine1("Billing Line 1");
		stripeBillingAddress.setLine2("Billing Line 2");
		stripeBillingAddress.setCity("BillingCity");
		stripeBillingAddress.setState("BillingState");
		stripeBillingAddress.setPostalCode("BillingPostalCode");
		stripeBillingAddress.setCountry("BillingCountry");
		stripeBillingDetails.setAddress(stripeBillingAddress);
		stripeBillingDetails.setName("Billing Name");
		stripeBillingDetails.setPhone("Billing Phone");
		stripeBillingDetails.setEmail("Billing Email");
		
		com.stripe.model.ShippingDetails stripeShippingDetails = new com.stripe.model.ShippingDetails();
		com.stripe.model.Address stripeShippingAddress = new com.stripe.model.Address();
		stripeShippingAddress.setLine1("Shipping Line 1");
		stripeShippingAddress.setLine2("Shipping Line 2");
		stripeShippingAddress.setCity("ShippingCity");
		stripeShippingAddress.setState("ShippingState");
		stripeShippingAddress.setPostalCode("ShippingPostalCode");
		stripeShippingAddress.setCountry("ShippingCountry");
		stripeShippingDetails.setAddress(stripeShippingAddress);
		stripeShippingDetails.setName("Shipping Name");
		stripeShippingDetails.setPhone("Shipping Phone");
		
		PaymentIntent paymentIntentMock = Mockito.mock(PaymentIntent.class);
		Optional<StripeObject> stripeObjectOptional = Optional.of(paymentIntentMock);
		ChargeCollection mockChargeCollection = Mockito.mock(ChargeCollection.class);
		List<Charge> mockChargeList = Mockito.mock(List.class);
		Charge mockCharge = Mockito.mock(Charge.class);
		Optional<Order> orderOptional = Optional.ofNullable(null);
		
		// When
		// ... return mocked event
		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(event);

		// ... return mocked EventDataObjectDeserializer
		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);
		// ... Return mocked PaymentIntent (StripeObject)
		when(paymentIntentMock.getClientSecret()).thenReturn("client secret");
		when(paymentIntentMock.getId()).thenReturn("paymentIntentId");
		when(paymentIntentMock.getCharges()).thenReturn(mockChargeCollection);
		when(mockChargeCollection.getData()).thenReturn(mockChargeList);
		when(mockChargeList.get(0)).thenReturn(mockCharge);
		when(mockCharge.getBillingDetails()).thenReturn(stripeBillingDetails);
		when(paymentIntentMock.getShipping()).thenReturn(stripeShippingDetails);
		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);
		// ... Return Event Type
		when(event.getType()).thenReturn("payment_intent.succeeded");
		// ... Find Existing Order by PaymentIntentId
		when(orderRepository.findByPaymentIntentId("paymentIntentId")).thenReturn(orderOptional);
		when(stripePaymentIntentRepository.findByPaymentIntentId("paymentIntentId")).thenReturn(Optional.of(stripePaymentIntent));

		underTest.handleStripeWebhookEvent(payload, "signature header");
		
		// Then
		verify(orderRepository).save(orderCaptor.capture());
		assertEquals(OrderStatus.PROCESSING, orderCaptor.getValue().getStatus());
		Order orderSaved = orderCaptor.getValue();
		BillingDetails savedBillingDetails = orderSaved.getBillingDetails();
		assertNotNull(savedBillingDetails);
		assertEquals("Billing Name", savedBillingDetails.getName());
		assertNotNull("Billing Email", savedBillingDetails.getEmail());
		assertNotNull("Billing Phone", savedBillingDetails.getPhone());
		Address savedBillingAddress = savedBillingDetails.getAddress();
		assertNotNull(savedBillingAddress);
		assertEquals("Billing Line 1", savedBillingAddress.getAddressLineOne());
		assertEquals("Billing Line 2", savedBillingAddress.getAddressLineTwo());
		assertEquals("BillingCity", savedBillingAddress.getCity());
		assertEquals("BillingState", savedBillingAddress.getState());
		assertEquals("BillingPostalCode", savedBillingAddress.getPostalCode());
		assertEquals("BillingCountry", savedBillingAddress.getCountry());
		
		ShippingDetails savedShippingDetails = orderSaved.getShippingDetails();
		assertNotNull(orderSaved.getShippingDetails());
		assertEquals("Shipping Name", savedShippingDetails.getName());
		assertNotNull("Shipping Phone", savedShippingDetails.getPhone());
		Address savedShippingAddress = savedShippingDetails.getAddress();
		assertNotNull(savedShippingAddress);
		assertEquals("Shipping Line 1", savedShippingAddress.getAddressLineOne());
		assertEquals("Shipping Line 2", savedShippingAddress.getAddressLineTwo());
		assertEquals("ShippingCity", savedShippingAddress.getCity());
		assertEquals("ShippingState", savedShippingAddress.getState());
		assertEquals("ShippingPostalCode", savedShippingAddress.getPostalCode());
		assertEquals("ShippingCountry", savedShippingAddress.getCountry());
	}
	
	@Test
	void handleStripeWebhookEventCreateNewOrderShouldThrowAnErrorIfStripePaymentIntentDoesNotExist() 
			throws JsonSyntaxException, SignatureVerificationException {

		// Given
		String payload = "payload";

		Event event = Mockito.mock(Event.class);
		EventDataObjectDeserializer dataObjectDeserializer = Mockito.mock(EventDataObjectDeserializer.class);
		
		com.stripe.model.PaymentMethod.BillingDetails stripeBillingDetails = new com.stripe.model.PaymentMethod.BillingDetails();
		com.stripe.model.Address stripeBillingAddress = new com.stripe.model.Address();
		stripeBillingAddress.setLine1("Billing Line 1");
		stripeBillingAddress.setLine2("Billing Line 2");
		stripeBillingAddress.setCity("BillingCity");
		stripeBillingAddress.setState("BillingState");
		stripeBillingAddress.setPostalCode("BillingPostalCode");
		stripeBillingAddress.setCountry("BillingCountry");
		stripeBillingDetails.setAddress(stripeBillingAddress);
		stripeBillingDetails.setName("Billing Name");
		stripeBillingDetails.setPhone("Billing Phone");
		stripeBillingDetails.setEmail("Billing Email");
		
		com.stripe.model.ShippingDetails stripeShippingDetails = new com.stripe.model.ShippingDetails();
		com.stripe.model.Address stripeShippingAddress = new com.stripe.model.Address();
		stripeShippingAddress.setLine1("Shipping Line 1");
		stripeShippingAddress.setLine2("Shipping Line 2");
		stripeShippingAddress.setCity("ShippingCity");
		stripeShippingAddress.setState("ShippingState");
		stripeShippingAddress.setPostalCode("ShippingPostalCode");
		stripeShippingAddress.setCountry("ShippingCountry");
		stripeShippingDetails.setAddress(stripeShippingAddress);
		stripeShippingDetails.setName("Shipping Name");
		stripeShippingDetails.setPhone("Shipping Phone");
		
		PaymentIntent paymentIntentMock = Mockito.mock(PaymentIntent.class);
		Optional<StripeObject> stripeObjectOptional = Optional.of(paymentIntentMock);
		ChargeCollection mockChargeCollection = Mockito.mock(ChargeCollection.class);
		List<Charge> mockChargeList = Mockito.mock(List.class);
		Charge mockCharge = Mockito.mock(Charge.class);
		Optional<Order> orderOptional = Optional.ofNullable(null);
		
		// When
		// ... return mocked event
		stripeWebhook.when(() -> Webhook.constructEvent(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(event);

		// ... return mocked EventDataObjectDeserializer
		when(event.getDataObjectDeserializer()).thenReturn(dataObjectDeserializer);
		// ... Return mocked PaymentIntent (StripeObject)
		when(paymentIntentMock.getClientSecret()).thenReturn("client secret");
		when(paymentIntentMock.getId()).thenReturn("paymentIntentId");
		when(paymentIntentMock.getCharges()).thenReturn(mockChargeCollection);
		when(mockChargeCollection.getData()).thenReturn(mockChargeList);
		when(mockChargeList.get(0)).thenReturn(mockCharge);
		when(mockCharge.getBillingDetails()).thenReturn(stripeBillingDetails);
		when(paymentIntentMock.getShipping()).thenReturn(stripeShippingDetails);
		when(dataObjectDeserializer.getObject()).thenReturn(stripeObjectOptional);
		// ... Return Event Type
		when(event.getType()).thenReturn("payment_intent.succeeded");
		// ... Find Existing Order by PaymentIntentId
		when(orderRepository.findByPaymentIntentId("paymentIntentId")).thenReturn(orderOptional);
		when(stripePaymentIntentRepository.findByPaymentIntentId("paymentIntentId")).thenReturn(Optional.ofNullable(null));
		
		// Then
		assertThrows(IllegalStateException.class, () -> underTest.handleStripeWebhookEvent(payload, "signature header"),
				"Could not find StripePaymentIntent with paymentIntentId: paymentIntentId");
	}

}
