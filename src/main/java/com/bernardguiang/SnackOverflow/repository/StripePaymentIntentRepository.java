package com.bernardguiang.SnackOverflow.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bernardguiang.SnackOverflow.model.StripePaymentIntent;

// TODO: test
public interface StripePaymentIntentRepository extends CrudRepository<StripePaymentIntent, Long> {

	Optional<StripePaymentIntent> findByClientSecret(String clientSecret);
}
