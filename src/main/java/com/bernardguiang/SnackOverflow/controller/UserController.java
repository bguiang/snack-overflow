package com.bernardguiang.SnackOverflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

//	private final UserService userService;
//	
//	@Autowired
//	public UserController(UserService userService) {
//		this.userService = userService;
//	}
//	
//	@GetMapping
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
//	public List<UserDTO> getUsers() {
//		List<UserDTO> users = userService.findAll();
//	    
//		return users;
//	}
//	
}
