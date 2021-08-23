package com.bernardguiang.SnackOverflow.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bernardguiang.SnackOverflow.model.OrderItem;
import com.bernardguiang.SnackOverflow.model.OrderStatus;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Long>{
	
	Iterable<OrderItem> findAllByProductIdAndOrderStatusNotIn(Long productId, List<OrderStatus> excludeStatuses);
	
	Iterable<OrderItem> findAllByProductIdAndOrderCreatedDateAfterAndOrderStatusNotIn(Long productId, Instant date, List<OrderStatus> excludeStatuses);
}
