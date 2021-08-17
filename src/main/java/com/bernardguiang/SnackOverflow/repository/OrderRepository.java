package com.bernardguiang.SnackOverflow.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bernardguiang.SnackOverflow.model.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>{
	Optional<Order> findByPaymentIntentId(String paymentIntentId);
	Iterable<Order> findAllByUserId(Long userId);
	Optional<Order> findByIdAndUserId(Long id, Long userId);
	
	Page<Order> findAllByUserUsernameContainingIgnoreCase(String search, Pageable pageable);
}
