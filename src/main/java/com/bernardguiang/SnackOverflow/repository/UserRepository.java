package com.bernardguiang.SnackOverflow.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bernardguiang.SnackOverflow.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
	@Override
	default <S extends User> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	Optional<User> findByUsername(String username);
	
}
