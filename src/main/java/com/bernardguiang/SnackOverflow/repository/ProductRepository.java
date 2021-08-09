package com.bernardguiang.SnackOverflow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bernardguiang.SnackOverflow.model.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long>{
	
	Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
