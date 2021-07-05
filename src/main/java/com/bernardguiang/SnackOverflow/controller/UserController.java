package com.bernardguiang.SnackOverflow.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	//TODO: admin only
	@GetMapping
	public List<UserDTO> getUsers() {
		List<UserDTO> users = userService.findAll();
	    
		return users;
	}
	
}
