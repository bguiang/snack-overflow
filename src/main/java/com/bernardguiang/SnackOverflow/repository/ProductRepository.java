package com.bernardguiang.SnackOverflow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long>{
	
	Optional<Product> findByName(String name);
	Iterable<Product> findAllByName(String name);
	
	Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
