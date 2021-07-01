package com.bernardguiang.SnackOverflow.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.CategoryDTO;
import com.bernardguiang.SnackOverflow.dto.ProductDTO;
import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.model.Category;
import com.bernardguiang.SnackOverflow.model.Product;
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
	
	// TODO: Duplicate of AuthService.signup
	public void createNewUser(User user) {
		userRepository.save(user);
	}
	
	public List<UserDTO> findAll(){
		Iterable<User> userIterator = userRepository.findAll();
		
		List<UserDTO> userDTOs = new ArrayList<>();
		for(User user : userIterator)
		{
			UserDTO userDTO = userEntityToDTO(user);
			userDTOs.add(userDTO);
		}
		
		return userDTOs;
	}
	
	private UserDTO userEntityToDTO(User user) {
		UserDTO userDTO = new UserDTO();
		
		userDTO.setUsername(user.getUsername());
		userDTO.setEmail(user.getEmail());
		userDTO.setFullName(user.getFullName());
		userDTO.setId(user.getId());
		userDTO.setPassword(user.getPassword());
		userDTO.setRole(user.getRole());
		
		return userDTO;
	}
}
