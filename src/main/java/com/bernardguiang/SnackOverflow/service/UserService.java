package com.bernardguiang.SnackOverflow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	
	@Autowired
	public UserService (UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void save(User user) {
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
	
	public User findById(long id) {
		Optional<User> user = userRepository.findById(id);
		user.orElseThrow(() -> new IllegalStateException("Could not find user with id: " + id));
		return user.get();
	}
	
	public UserDTO findUserDTOByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		user.orElseThrow(() -> new IllegalStateException("Could not find user: " + username));
		UserDTO userDTO = userEntityToDTO(user.get());
		return userDTO;
	}
	
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalStateException("Could not find user: " + username));
	}
	
	private UserDTO userEntityToDTO(User user) {
		UserDTO userDTO = new UserDTO();
		
		userDTO.setUsername(user.getUsername());
		userDTO.setEmail(user.getEmail());
		userDTO.setFullName(user.getFullName());
		userDTO.setId(user.getId());
		userDTO.setRole(user.getRole());
		
		return userDTO;
	}
}
