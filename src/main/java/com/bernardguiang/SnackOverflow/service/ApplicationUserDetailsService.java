package com.bernardguiang.SnackOverflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bernardguiang.SnackOverflow.model.User;
import com.bernardguiang.SnackOverflow.repository.UserRepository;
import com.bernardguiang.SnackOverflow.security.ApplicationUserDetails;

@Service
public class ApplicationUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	@Autowired
	public ApplicationUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername((username))
			.orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));
		ApplicationUserDetails userDetails = new ApplicationUserDetails(user);
		return userDetails;
	}
}
