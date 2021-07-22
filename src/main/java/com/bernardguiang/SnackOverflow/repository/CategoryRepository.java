package com.bernardguiang.SnackOverflow.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bernardguiang.SnackOverflow.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Long>
{
	Iterable<Category> findAll();
	Optional<Category> findByName(String name);
}
