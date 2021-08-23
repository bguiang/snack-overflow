package com.bernardguiang.SnackOverflow.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.ShippingDetails;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

@DataJpaTest(properties = { "spring.jpa.properties.javax.persistence.validation.mode=none" })
class OrderRepositoryIntegrationTest {

	@Autowired
	private OrderRepository underTest;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	private User user1, user2;
	private Order order1, order2, order3;

	@BeforeEach
	void setUp() throws Exception {
		// order1 by user1
		Order unsavedOrder1 = new Order();

		Address address = new Address("address line 1", "address line 2", "city", "state", "postal", "country");

		BillingDetails billingDetails = new BillingDetails();
		billingDetails.setName("First Last");
		billingDetails.setEmail("my@email.com");
		billingDetails.setPhone("1234567890");
		billingDetails.setAddress(address);

		ShippingDetails shippingDetails = null;

		unsavedOrder1.setTotal(new BigDecimal(10));
		unsavedOrder1.setBillingDetails(billingDetails);
		billingDetails.setOrder(unsavedOrder1);
		unsavedOrder1.setShippingDetails(shippingDetails);
		unsavedOrder1.setShippingSameAsBilling(true);
		unsavedOrder1.setStatus(OrderStatus.COMPLETED);
		unsavedOrder1.setCreatedDate(Instant.now());
		unsavedOrder1.setPaymentIntentId("payment_intent_1");

		Product p1 = new Product();
		p1.setName("Product 1");
		Product p2 = new Product();
		p2.setName("Product 2");
		Product product1 = productRepository.save(p1);
		Product product2 = productRepository.save(p2);

		List<OrderItem> items1 = new ArrayList<>();
		OrderItem item1 = new OrderItem();
		item1.setOrder(unsavedOrder1);
		item1.setPrice(new BigDecimal(2));
		item1.setProduct(product1);
		item1.setQuantity(5);
		items1.add(item1);

		unsavedOrder1.setItems(items1);
		User u1 = new User();
		u1.setEmail("u1@email.com");
		u1.setFullName("user one");
		u1.setUsername("user1");
		u1.setPassword("asdf");
		u1.setRole(ApplicationUserRole.CUSTOMER.name());
		user1 = userRepository.save(u1);
		unsavedOrder1.setUser(user1);
		order1 = underTest.save(unsavedOrder1);

		// order2 by user1
		Order unsavedOrder2 = new Order();
		unsavedOrder2.setTotal(new BigDecimal(30));
		unsavedOrder2.setBillingDetails(billingDetails);
		billingDetails.setOrder(unsavedOrder2);
		unsavedOrder2.setShippingDetails(shippingDetails);
		unsavedOrder2.setShippingSameAsBilling(true);
		unsavedOrder2.setStatus(OrderStatus.PAYMENT_PENDING);
		unsavedOrder2.setCreatedDate(Instant.now());
		unsavedOrder2.setPaymentIntentId("payment_intent_2");

		List<OrderItem> items2 = new ArrayList<>();
		OrderItem item2 = new OrderItem();
		item2.setOrder(unsavedOrder2);
		item2.setPrice(new BigDecimal(5));
		item2.setProduct(product2);
		item2.setQuantity(4);
		items2.add(item2);

		unsavedOrder2.setItems(items2);
		unsavedOrder2.setUser(user1);
		order2 = underTest.save(unsavedOrder2);

		// order3 by user2
		Order unsavedOrder3 = new Order();

		unsavedOrder3.setTotal(new BigDecimal(20));
		unsavedOrder3.setBillingDetails(billingDetails);
		billingDetails.setOrder(unsavedOrder3);
		unsavedOrder3.setShippingDetails(shippingDetails);
		unsavedOrder3.setShippingSameAsBilling(true);
		unsavedOrder3.setStatus(OrderStatus.COMPLETED);
		unsavedOrder3.setCreatedDate(Instant.now());
		unsavedOrder3.setPaymentIntentId("payment_intent_3");

		List<OrderItem> items3 = new ArrayList<>();
		OrderItem item3 = new OrderItem();
		item3.setOrder(unsavedOrder3);
		item3.setPrice(new BigDecimal(8));
		item3.setProduct(product2);
		item3.setQuantity(3);
		items3.add(item3);

		unsavedOrder3.setItems(items3);
		User u2 = new User();
		u2.setEmail("u2@email.com");
		u2.setFullName("user two");
		u2.setUsername("user2");
		u2.setPassword("fdsa");
		u2.setRole(ApplicationUserRole.CUSTOMER.name());
		user2 = userRepository.save(u2);
		unsavedOrder3.setUser(user2);
		order3 = underTest.save(unsavedOrder3);
	}

	@Test
	void itShouldFindAll() {
		// Given
		// When
		Iterable<Order> resultIterable = underTest.findAll();

		// Then
		List<Order> result = new ArrayList<Order>();
		resultIterable.forEach(result::add);

		assertEquals(3, result.size());
		List<Order> expected = Arrays.asList(order1, order2, order3);
		assertTrue(result.containsAll(expected));
		assertTrue(expected.containsAll(result));
	}

	@Test
	void itShouldFindAllByCreatedDateAfter() {
		// Given
		Instant beforeAll = Instant.ofEpochMilli(0);

		// When
		Iterable<Order> resultIterable = underTest.findAllByCreatedDateAfter(beforeAll);

		// Then
		List<Order> result = new ArrayList<Order>();
		resultIterable.forEach(result::add);

		assertEquals(3, result.size());
		List<Order> expected = Arrays.asList(order1, order2, order3);
		assertTrue(result.containsAll(expected));
		assertTrue(expected.containsAll(result));
	}

	@Test
	void itShouldFindByPaymentIntentId() {
		// Given
		// When
		Optional<Order> result = underTest.findByPaymentIntentId("payment_intent_1");

		// Then
		assertEquals(order1, result.get());
	}

	@Test
	void itShouldFindAllByUserId() {
		// Given
		// When
		Iterable<Order> resultIterable = underTest.findAllByUserId(user1.getId());

		// Then
		List<Order> result = new ArrayList<Order>();
		resultIterable.forEach(result::add);

		assertEquals(2, result.size());
		List<Order> expected = Arrays.asList(order1, order2);
		assertTrue(result.containsAll(expected));
		assertTrue(expected.containsAll(result));
	}

	@Test
	void itShouldFindByIdAndUserId() {
		// Given
		// When
		Optional<Order> resultOptional = underTest.findByIdAndUserId(order3.getId(), user2.getId());

		// Then
		Order result = resultOptional.get();
		assertEquals(order3, result);
	}
	
	@Test
	void itShouldFindAllByUserUsernameContainingIgnoreCase() {
		// Given
		Sort sort = Sort.by(Sort.Direction.DESC, "total");
		Pageable pageable = PageRequest.of(0, 3, sort);
		
		// When
		Page<Order> result = underTest.findAllByUserUsernameContainingIgnoreCase("user1", pageable);

		// Then
		List<Order> resultList = result.getContent();
		List<Order> expected = Arrays.asList(order2, order1);
		assertEquals(2, resultList.size());
		assertEquals(expected, resultList);
	}
}
