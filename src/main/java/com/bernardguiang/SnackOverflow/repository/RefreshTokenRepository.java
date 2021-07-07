package com.bernardguiang.SnackOverflow.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bernardguiang.SnackOverflow.model.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long>{

	void deleteByToken(String token);
	Optional<RefreshToken> findByToken(String token);
}
