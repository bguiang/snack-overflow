package com.bernardguiang.SnackOverflow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.response.FullUserDTO;
import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	
	@Autowired
	public UserService (UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	// TODO: test
	public List<UserDTO> findAll(){
		Iterable<User> userIterator = userRepository.findAll();
		
		List<UserDTO> userDTOs = new ArrayList<>();
		for(User user : userIterator)
		{
			UserDTO userDTO = new UserDTO(user);
			userDTOs.add(userDTO);
		}
		
		return userDTOs;
	}
	
	// TODO: test
	public UserDTO findByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		user.orElseThrow(() -> new IllegalStateException("Could not find user: " + username));
		UserDTO userDTO = new UserDTO(user.get());
		return userDTO;
	}
	
	// TODO: test. Is FullUserDTO necessary?
	public FullUserDTO findById(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		user.orElseThrow(() -> new IllegalStateException("Could not find user with id: " + userId));
		FullUserDTO userDTO = new FullUserDTO(user.get());
		return userDTO;
	}
}
