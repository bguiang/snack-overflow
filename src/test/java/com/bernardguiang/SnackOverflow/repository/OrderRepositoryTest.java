package com.bernardguiang.SnackOverflow.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bernardguiang.SnackOverflow.dto.Address;
import com.bernardguiang.SnackOverflow.model.BillingDetails;
import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.ShippingDetails;
import com.bernardguiang.SnackOverflow.model.User;

@DataJpaTest(properties = { "spring.jpa.properties.javax.persistence.validation.mode=none" })
class OrderRepositoryTest {

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
		BigDecimal total1 = new BigDecimal(10);
		Instant createDate1 = Instant.now();
		String clientSecret1 = "client secret";
		OrderStatus status1 = OrderStatus.COMPLETED;
		String name = "First Last";
		String email = "my@email.com";
		String phone = "1234567890";

		Address address = new Address("address line 1", "address line 2", "city", "state", "postal", "country");

		BillingDetails billingDetails = new BillingDetails();
		billingDetails.setName(name);
		billingDetails.setEmail(email);
		billingDetails.setPhone(phone);
		billingDetails.setAddress(address);

		ShippingDetails shippingDetails = null;

		unsavedOrder1.setTotal(total1);
		unsavedOrder1.setCreatedDate(createDate1);
		unsavedOrder1.setBillingDetails(billingDetails);
		billingDetails.setOrder(unsavedOrder1);
		unsavedOrder1.setShippingDetails(shippingDetails);
		unsavedOrder1.setShippingSameAsBilling(true);
		unsavedOrder1.setClientSecret(clientSecret1);
		unsavedOrder1.setStatus(status1);

		Product product1 = productRepository.save(new Product("Product 1"));
		Product product2 = productRepository.save(new Product("Product 2"));

		List<OrderItem> items1 = new ArrayList<>();
		OrderItem item1 = new OrderItem();
		item1.setOrder(unsavedOrder1);
		item1.setPrice(new BigDecimal(2));
		item1.setProduct(product1);
		item1.setQuantity(5);
		items1.add(item1);

		unsavedOrder1.setItems(items1);
		user1 = userRepository.save(new User());
		unsavedOrder1.setUser(user1);
		order1 = underTest.save(unsavedOrder1);

		// order2 by user1
		Order unsavedOrder2 = new Order();
		BigDecimal total2 = new BigDecimal(20);
		Instant createDate2 = Instant.now();
		String clientSecret2 = "client secret 2";
		OrderStatus status2 = OrderStatus.PAYMENT_PENDING;

		unsavedOrder2.setTotal(total2);
		unsavedOrder2.setCreatedDate(createDate2);
		unsavedOrder2.setBillingDetails(billingDetails);
		billingDetails.setOrder(unsavedOrder2);
		unsavedOrder2.setShippingDetails(shippingDetails);
		unsavedOrder2.setShippingSameAsBilling(true);
		unsavedOrder2.setClientSecret(clientSecret2);
		unsavedOrder2.setStatus(status2);

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
		BigDecimal total3 = new BigDecimal(20);
		Instant createDate3 = Instant.now();
		String clientSecret3 = "client secret 3";
		OrderStatus status3 = OrderStatus.CREATED;

		unsavedOrder3.setTotal(total3);
		unsavedOrder3.setCreatedDate(createDate3);
		unsavedOrder3.setBillingDetails(billingDetails);
		billingDetails.setOrder(unsavedOrder3);
		unsavedOrder3.setShippingDetails(shippingDetails);
		unsavedOrder3.setShippingSameAsBilling(true);
		unsavedOrder3.setClientSecret(clientSecret3);
		unsavedOrder3.setStatus(status3);

		List<OrderItem> items3 = new ArrayList<>();
		OrderItem item3 = new OrderItem();
		item3.setOrder(unsavedOrder3);
		item3.setPrice(new BigDecimal(8));
		item3.setProduct(product2);
		item3.setQuantity(3);
		items3.add(item3);

		unsavedOrder3.setItems(items3);
		user2 = userRepository.save(new User());
		unsavedOrder3.setUser(user2);
		order3 = underTest.save(unsavedOrder3);
	}

	@Test
	void itShouldFindByClientSecret() {
		// Given
		String clientSecret = order1.getClientSecret();

		// When
		Optional<Order> resultOptional = underTest.findByClientSecret(clientSecret);

		// Then
		Order result = resultOptional.get();
		assertEquals(order1, result);
	}

	@Test
	void itShouldFindAllByUser() {
		// Given
		// When
		Iterable<Order> resultIterable = underTest.findAllByUser(user1);

		// Then
		List<Order> result = new ArrayList<Order>();
		resultIterable.forEach(result::add);

		assertEquals(2, result.size());
		List<Order> expected = Arrays.asList(order1, order2);
		assertTrue(result.containsAll(expected));
		assertTrue(expected.containsAll(result));
	}

	@Test
	void itShouldFindAllByUserIdAndStatusNot() {
		// Given
		// When
		Iterable<Order> resultIterable = underTest.findAllByUserIdAndStatusNot(user1.getId(), OrderStatus.COMPLETED);
		// Then
		List<Order> result = new ArrayList<Order>();
		resultIterable.forEach(result::add);
		assertEquals(1, result.size());
		assertEquals(order2, result.get(0));
	}

	@Test
	void itShouldFindByIdAndUserId() {
		// Given
		// When
		Optional<Order> resultOptional =  underTest.findByIdAndUserId(order3.getId(), user2.getId());
		
		// Then
		Order result = resultOptional.get();
		assertEquals(order3, result);
	}
	
	@Test
	void itShouldFindByIdAndUserIdAndStatusNot() {
		// Given
		// When
		Optional<Order> resultOptional =  underTest.findByIdAndUserIdAndStatusNot(order1.getId(), user1.getId(), OrderStatus.CREATED);
		
		// Then
		Order result = resultOptional.get();
		assertEquals(order1, result);
	}

}
