package com.bernardguiang.SnackOverflow.utils;

import org.springframework.data.jpa.domain.Specification;

import com.bernardguiang.SnackOverflow.model.Product;

//TODO: look into JPA specification api for more flexible queries
public class ProductSpecifications {
	
	public static Specification<Product> likeName(String name) {
		if(name == null)
			return null;
		//return (root, query, cb) -> cb.like(root.get(Product_.name), name);
		return null;
	}
}
