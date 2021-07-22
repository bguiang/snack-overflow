package com.bernardguiang.SnackOverflow.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{
	
	Optional<Product> findByName(String name);
	Iterable<Product> findAllByName(String name);
}
