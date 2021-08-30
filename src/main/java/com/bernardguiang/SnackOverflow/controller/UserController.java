package com.bernardguiang.SnackOverflow.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bernardguiang.SnackOverflow.dto.UserDTO;
import com.bernardguiang.SnackOverflow.dto.request.StatsRequest;
import com.bernardguiang.SnackOverflow.dto.request.UserPage;
import com.bernardguiang.SnackOverflow.dto.response.FullUserDTO;
import com.bernardguiang.SnackOverflow.dto.response.UserStatsResponse;
import com.bernardguiang.SnackOverflow.service.UserService;

@RestController
public class UserController {

	private final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/api/v1/admin/users")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Page<UserDTO> getUsersPaginated(UserPage userPage) {
		return userService.findUsersPaginated(userPage);
	}
	
	@GetMapping("/api/v1/admin/users/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public FullUserDTO getUser(@PathVariable long userId) {
		FullUserDTO user = userService.findById(userId);
		return user;
	}
	
	@GetMapping("/api/v1/admin/users/stats")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public UserStatsResponse getNewUsers(@Valid StatsRequest request) {
		return userService.getUserStats(request);
	}
	
}
