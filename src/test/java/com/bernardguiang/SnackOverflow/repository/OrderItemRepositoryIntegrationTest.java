package com.bernardguiang.SnackOverflow.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.Product;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

@DataJpaTest(properties = { "spring.jpa.properties.javax.persistence.validation.mode=none" })
class OrderItemRepositoryIntegrationTest {

	@Autowired
	private OrderItemRepository underTest;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	Product product1, product2;
	Order order1, order2;
	Instant orderCreateDate1, orderCreateDate2;
	
	@BeforeEach
	void setUp() throws Exception {
		
		Product p1 = new Product();
		p1.setName("product1");
		Product p2 = new Product();
		p2.setName("product2");
		
		product1 = productRepository.save(p1);
		product2 = productRepository.save(p2);
		
		User u = new User();
		u.setEmail("email1@email.com");
		u.setUsername("username1");
		u.setPassword("Password1!");
		u.setFullName("Full Name 1");
		u.setRole(ApplicationUserRole.CUSTOMER.name());
		u.setJoinDate(Instant.now());
		User user = userRepository.save(u);
		Order ord1 = new Order();
		Order ord2 = new Order();
		ord1.setUser(user);
		orderCreateDate1 = Instant.now();
		ord1.setCreatedDate(orderCreateDate1);
		ord1.setStatus(OrderStatus.CANCELLED);
		ord2.setUser(user);
		orderCreateDate2 = Instant.now();
		ord2.setCreatedDate(orderCreateDate2);
		ord2.setStatus(OrderStatus.COMPLETED);
	
		OrderItem item1 = new OrderItem();
		item1.setOrder(ord1);
		item1.setPrice(new BigDecimal(20));
		item1.setProduct(product1);
		item1.setQuantity(1);
		
		OrderItem item2 = new OrderItem();
		item2.setOrder(ord2);
		item2.setPrice(new BigDecimal(10));
		item2.setProduct(product1);
		item2.setQuantity(2);
		
		OrderItem item3 = new OrderItem();
		item3.setOrder(ord2);
		item3.setPrice(new BigDecimal(30));
		item3.setProduct(product2);
		item3.setQuantity(3);
		
		ord1.setItems(Arrays.asList(item1));
		ord2.setItems(Arrays.asList(item2, item3));
		
		order1 = orderRepository.save(ord1);
		order2 = orderRepository.save(ord2);
	}
	
	@Test
	void itShouldFindAllByProductIdAndOrderStatusNotIn() {
		// Given
		// When
		Iterable<OrderItem> resultIterable =  
			underTest.findAllByProductIdAndOrderStatusNotIn(product1.getId(), Arrays.asList(OrderStatus.CANCELLED));
		
		// Then
		List<OrderItem> result = new ArrayList<OrderItem>();
		resultIterable.forEach(result::add);
		
		assertEquals(1, result.size());
		assertEquals(order2, result.get(0).getOrder());
	}
	
	
	@Test
	void itShouldFindAllByProductIdAndOrderCreatedDateAfterAndOrderStatusNotIn() {
		// Given
		// When
		Iterable<OrderItem> resultIterable = 
			underTest.findAllByProductIdAndOrderCreatedDateAfterAndOrderStatusNotIn(product1.getId(), orderCreateDate1, Arrays.asList(OrderStatus.CANCELLED));
		
		// Then
		List<OrderItem> result = new ArrayList<OrderItem>();
		resultIterable.forEach(result::add);
		
		assertEquals(1, result.size());
		assertEquals(order2, result.get(0).getOrder());
	}

}
