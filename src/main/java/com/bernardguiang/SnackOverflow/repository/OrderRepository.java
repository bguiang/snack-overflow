package com.bernardguiang.SnackOverflow.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bernardguiang.SnackOverflow.model.Order;
import com.bernardguiang.SnackOverflow.model.OrderStatus;
import com.bernardguiang.SnackOverflow.model.User;

public interface OrderRepository extends CrudRepository<Order, Long>{
	Optional<Order> findByClientSecret(String clientSecret);
	Iterable<Order> findAllByUser(User user);
	Iterable<Order> findAllByUserAndStatusNot(User user, OrderStatus status);
}
