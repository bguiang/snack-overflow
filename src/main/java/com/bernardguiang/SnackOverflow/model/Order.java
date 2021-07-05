package com.bernardguiang.SnackOverflow.model;

import java.time.Instant;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Order {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@OneToMany
	List<OrderItem> items;
	
	private Instant createdDate;
}
