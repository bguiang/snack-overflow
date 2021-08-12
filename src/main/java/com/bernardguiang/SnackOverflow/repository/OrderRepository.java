package com.bernardguiang.SnackOverflow.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.User;

public interface OrderRepository extends CrudRepository<Order, Long>{
	Optional<Order> findByPaymentIntentId(String paymentIntentId);
	Iterable<Order> findAllByUser(User user);
	Iterable<Order> findAllByUserIdAndStatusNot(Long id, OrderStatus status);
	Optional<Order> findByIdAndUserId(Long id, Long userId);
	Optional<Order> findByIdAndUserIdAndStatusNot(Long id, Long userId, OrderStatus status);
	Iterable<Order> findAllByStatusNot(OrderStatus status); //TODO: test
	Optional<Order> findByIdAndStatusNot(Long id, OrderStatus status); // TODO: test
}
