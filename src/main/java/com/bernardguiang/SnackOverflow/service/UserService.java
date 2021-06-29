package com.bernardguiang.SnackOverflow.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.CategoryDTO;
import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.UserRepository;
import com.bernardguiang.SnackOverflow.security.ApplicationUserRole;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	public void createNewUser(User user) {
		userRepository.save(user);
	}
	
	@EventListener(classes = { ContextRefreshedEvent.class})
	public void setDefaultUsers() {
		
		User user = new User();
		user.setUsername("bernard");
		user.setPassword(passwordEncoder.encode("password"));
		user.setRole(ApplicationUserRole.ADMIN.name());
		user.setFullName("Bernard Guiang");
		User saved = userRepository.save(user);
		System.out.println(saved);
	}
}
