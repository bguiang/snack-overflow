package com.bernardguiang.SnackOverflow.repository;

import java.time.Instant;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bernardguiang.SnackOverflow.model.OrderItem;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Long>{

	Iterable<OrderItem> findAllByProductIdAndOrderCreatedDateAfter(Long productId, Instant date);
}
