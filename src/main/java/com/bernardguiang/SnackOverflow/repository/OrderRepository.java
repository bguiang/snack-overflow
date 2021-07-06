package com.bernardguiang.SnackOverflow.repository;

import org.springframework.data.repository.CrudRepository;

import com.bernardguiang.SnackOverflow.model.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{

}
